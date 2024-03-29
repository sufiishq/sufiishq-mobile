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

package pk.sufiishq.app.ui.components.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import pk.sufiishq.app.utils.TextRes
import pk.sufiishq.app.utils.extention.optString
import pk.sufiishq.aurora.components.SIText
import pk.sufiishq.aurora.layout.SIDialog

class ConfirmDialogParam(
    val message: String,
    val onDismissed: () -> Unit = {},
    val onConfirmed: () -> Unit,
)

@Composable
fun ConfirmationDialog(
    state: MutableState<ConfirmDialogParam?>,
) {
    state.value?.apply {
        SIDialog(
            onDismissRequest = { onDismissed() },
            onNoText = optString(TextRes.label_no),
            onNoClick = {
                onDismissed()
                state.value = null
            },
            onYesText = optString(TextRes.label_yes),
            onYesClick = {
                state.value = null
                onConfirmed()
            },
        ) {
            SIText(
                text = message,
                textColor = it,
            )
        }
    }
}
