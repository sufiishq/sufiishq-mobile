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

package pk.sufiishq.app.feature.kalam.extension

import android.content.Context
import pk.sufiishq.app.R
import java.io.File
import pk.sufiishq.app.feature.kalam.model.Kalam
import pk.sufiishq.app.utils.extention.isNetworkAvailable
import pk.sufiishq.app.utils.getApp
import pk.sufiishq.app.utils.quickToast

fun Kalam?.hasOfflineSource() = this?.offlineSource?.isNotEmpty() ?: false

fun Kalam?.canPlay(context: Context): Boolean {
    return if (!this.hasOfflineSource() && !context.isNetworkAvailable()) {
        quickToast(R.string.msg_no_network_connection)
        false
    } else {
        true
    }
}

fun Kalam.isOfflineFileExists(): Boolean {
    return File(getApp().filesDir.absolutePath + File.separator + offlineSource).exists()
}

fun Kalam.offlineFile(): File? {
    return takeIf { offlineSource.isNotEmpty() }
        ?.let {
            File(
                buildString {
                    append(getApp().filesDir)
                    append(File.separator)
                    append(it.offlineSource)
                },
            )
        }
}

fun Kalam.metaInfo(): String {
    return buildString {
        append(location)
        if (recordeDate.isNotEmpty()) append(" - $recordeDate")
    }
}
