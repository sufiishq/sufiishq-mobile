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

package pk.sufiishq.app.ui.screen.applock

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.esentsov.PackagePrivate
import pk.sufiishq.app.utils.ImageRes
import pk.sufiishq.app.utils.TextRes
import pk.sufiishq.app.utils.extention.optString
import pk.sufiishq.aurora.components.SIButton
import pk.sufiishq.aurora.components.SIImage
import pk.sufiishq.aurora.components.SIText
import pk.sufiishq.aurora.components.SIWidthSpace
import pk.sufiishq.aurora.layout.SIRow
import pk.sufiishq.aurora.theme.AuroraColor

@PackagePrivate
@Composable
fun AppLockHeader(
    modifier: Modifier = Modifier,
    title: String = optString(TextRes.title_app_lock),
    buttonTitle: String = optString(TextRes.label_cancel),
    onButtonClick: () -> Unit,
) {
    SIRow(
        modifier = modifier.fillMaxWidth().padding(12.dp, 6.dp),
        bgColor = AuroraColor.Background,
        radius = 4,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SIRow(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SIImage(
                modifier = Modifier.height(30.dp),
                resId = ImageRes.shield,
            )
            SIWidthSpace(value = 8)
            SIText(
                text = title,
                textColor = it,
            )
        }
        SIButton(text = buttonTitle, onClick = onButtonClick)
    }
}
