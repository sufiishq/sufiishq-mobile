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

package pk.sufiishq.app.utils.extension


import VideoHandle.EpEditor
import VideoHandle.OnEditorListener
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.verify
import java.io.File
import java.io.FilenameFilter
import org.apache.commons.io.FileUtils
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import pk.sufiishq.app.SufiIshqTest
import pk.sufiishq.app.feature.kalam.splitter.SplitStatus
import pk.sufiishq.app.utils.extention.appendPath
import pk.sufiishq.app.utils.extention.deleteContent
import pk.sufiishq.app.utils.extention.moveTo
import pk.sufiishq.app.utils.extention.split
import pk.sufiishq.app.utils.extention.toFile

class FileExtensionTest : SufiIshqTest() {

    @Test
    fun testMoveTo_should_createThenCopySourceFile() {
        val file = File(appContext.cacheDir.appendPath("/source.txt").absolutePath)
        FileUtils.copyInputStreamToFile(testHelpFileInputStream(), file)

        val destFile = File(appContext.filesDir.appendPath("/target.txt").absolutePath)
        file.moveTo(destFile)

        assertEquals(
            file.inputStream().bufferedReader().readText(),
            destFile.inputStream().bufferedReader().readText()
        )
    }

    @Test
    fun testDeleteContent_shouldDelete_allMP3Files() {
        mockkStatic(String::toFile)

        val mp3File = mockk<File> { every { delete() } returns true }

        val file =
            mockk<File> {
                every { list(any()) } answers
                        {
                            val filter = firstArg<FilenameFilter>()
                            val data = mutableListOf<String>()
                            arrayOf("first.mp3", "unknown.txt", "second.mp3").forEach {
                                if (filter.accept(mockk(), it)) {
                                    data.add(it)
                                }
                            }
                            data.toTypedArray()
                        }
                every { absolutePath } returns ""
                every { absolutePath.toFile(any()) } returns mp3File
            }

        file.deleteContent()

        verify(exactly = 2) { mp3File.delete() }
    }

    @Test
    fun testToFile_shouldReturn_FileObject() {
        val file = File(appContext.filesDir.absolutePath)
        assertEquals(
            "${file.absolutePath}${File.separator}file.mp3",
            file.absolutePath.toFile("file.mp3").absolutePath,
        )
    }

    @Test
    fun testSplit_should_verifyAllStatus() {
        mockkStatic(EpEditor::execCmd)

        val command = slot<String>()
        val slot = slot<OnEditorListener>()

        every { EpEditor.execCmd(capture(command), any(), capture(slot)) } returns Unit

        val file = mockk<File> { every { absolutePath } returns "kalam/input.mp3" }

        val outFile = mockk<File> { every { absolutePath } returns "kalam/output.mp3" }

        val start = "00:10:20"
        val end = "00:03:40"

        var status: SplitStatus = SplitStatus.Start
        file.split(outFile, start, end) { status = it }

        assertEquals(
            "-y -i kalam/input.mp3 -ss 00:10:20 -codec copy -t 00:03:40 kalam/output.mp3",
            command.captured,
        )

        slot.captured.onSuccess()
        assertTrue(status is SplitStatus.Done)

        slot.captured.onFailure()
        assertTrue(status is SplitStatus.Error)

        slot.captured.onProgress(0f)
        assertTrue(status is SplitStatus.Error)
    }

    @Test
    fun testVerify_fileAppendPath() {
        val file = File(
            appContext.filesDir.appendPath("/path").appendPath("/to")
                .appendPath("/append").absolutePath
        )

        assertEquals(
            "${appContext.filesDir.absolutePath}${File.separator}path${File.separator}to${File.separator}append",
            file.absolutePath
        )
    }
}
