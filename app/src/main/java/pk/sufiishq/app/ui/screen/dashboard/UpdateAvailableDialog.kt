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

package pk.sufiishq.app.ui.screen.dashboard

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import io.github.esentsov.PackagePrivate
import pk.sufiishq.app.feature.app.controller.MainController
import pk.sufiishq.app.utils.TextRes
import pk.sufiishq.app.utils.extention.optString
import pk.sufiishq.app.utils.optValue
import pk.sufiishq.aurora.components.SIButton
import pk.sufiishq.aurora.components.SIHeightSpace
import pk.sufiishq.aurora.components.SIText
import pk.sufiishq.aurora.components.TextSize
import pk.sufiishq.aurora.layout.SIDialog

@PackagePrivate
@Composable
fun UpdateAvailableDialog(
    mainController: MainController,
) {
    val updateAvailable = mainController.showUpdateDialog().observeAsState().optValue(false)
    if (updateAvailable) {
        val context = LocalContext.current

        SIDialog { textColor ->
            SIText(
                modifier = Modifier.fillMaxWidth(),
                text = optString(TextRes.app_name),
                textColor = textColor,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )
            SIHeightSpace(value = 16)
            SIText(
                text = optString(TextRes.msg_update_available),
                textColor = textColor,
                textSize = TextSize.Regular,
            )
            SIHeightSpace(value = 12)
            SIButton(
                modifier = Modifier.fillMaxWidth(),
                text = optString(TextRes.label_update_now),
                textSize = TextSize.Regular,
                onClick = { mainController.handleUpdate(context) },
            )
        }
    }
}
