/*
 * Copyright 2022-2023 SufiIshq
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pk.sufiishq.app.utils.extention

import VideoHandle.EpEditor
import VideoHandle.OnEditorListener
import org.apache.commons.io.IOUtils
import pk.sufiishq.app.R
import pk.sufiishq.app.feature.kalam.splitter.SplitStatus
import pk.sufiishq.app.utils.getString
import timber.log.Timber
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

fun File.moveTo(destination: File) {
    IOUtils.copy(this.asInputStream(), destination.asOutputStream()).also { this.delete() }
}

fun File.deleteContent() {
    list { _, name -> name.endsWith("mp3") }
        ?.forEach {
            try {
                absolutePath.toFile(it).delete()
            } catch (ex: Exception) {
                Timber.e(ex)
            }
        }
}

fun String.toFile(filename: String): File {
    return File(this, filename)
}

fun File.split(
    output: File,
    start: String,
    end: String,
    onComplete: (splitStatus: SplitStatus) -> Unit,
) {
    EpEditor.execCmd(
        "-y -i $absolutePath -ss $start -codec copy -t $end ${output.absolutePath}",
        0,
        object : OnEditorListener {
            override fun onSuccess() {
                onComplete(SplitStatus.Done)
            }

            override fun onFailure() {
                onComplete(SplitStatus.Error(getString(R.string.msg_execution_failed)))
            }

            override fun onProgress(progress: Float) {}
        },
    )
}

fun File.appendPath(path: String): File = File("$absolutePath/$path")

fun File.asInputStream() = FileInputStream(this)
fun File.asOutputStream() = FileOutputStream(this)
