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

package pk.sufiishq.app.feature.app

import android.content.Intent
import androidx.activity.ComponentActivity
import pk.sufiishq.app.feature.kalam.extension.metaInfo
import pk.sufiishq.app.feature.kalam.model.Kalam
import pk.sufiishq.app.utils.TextRes
import pk.sufiishq.app.utils.getString
import javax.inject.Inject

class AppManager @Inject constructor() {

    fun shareKalam(kalam: Kalam, componentActivity: ComponentActivity) {
        val appName = componentActivity.getString(TextRes.app_name)
        val shareText = getString(TextRes.msg_kalam_meta_info, kalam.title, kalam.metaInfo())
        Intent(Intent.ACTION_SEND)
            .apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, kalam.title)
                putExtra(Intent.EXTRA_TEXT, shareText)
            }
            .also {
                componentActivity.startActivity(
                    Intent.createChooser(
                        it,
                        getString(TextRes.dynamic_share_kalam, appName),
                    ),
                )
            }
    }

    fun shareApp(activity: ComponentActivity) {
        val appName = activity.getString(TextRes.app_name)
        Intent(Intent.ACTION_SEND)
            .apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, appName)
                putExtra(
                    Intent.EXTRA_TEXT,
                    "https://play.google.com/store/apps/details?id=pk.sufiishq.app",
                )
            }
            .also {
                activity.startActivity(
                    Intent.createChooser(
                        it,
                        getString(TextRes.dynamic_share_kalam, appName),
                    ),
                )
            }
    }
}
