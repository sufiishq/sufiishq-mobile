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

package pk.sufiishq.app.ui.components.widgets

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pk.sufiishq.app.R
import pk.sufiishq.app.core.app.controller.MainController
import pk.sufiishq.app.utils.optString
import pk.sufiishq.aurora.components.SIImage
import pk.sufiishq.aurora.components.SIText
import pk.sufiishq.aurora.components.SIWidthSpace
import pk.sufiishq.aurora.components.TextSize
import pk.sufiishq.aurora.layout.SIBox
import pk.sufiishq.aurora.layout.SIRow
import pk.sufiishq.aurora.theme.AuroraColor

@Composable
fun HijriDate(
    mainController: MainController,
) {
    val hijriDate = mainController.getHijriDate().observeAsState()

    SIBox(
        bgColor = AuroraColor.SecondaryVariant,
        padding = 12,
        modifier = Modifier.fillMaxWidth().height(120.dp),
    ) { textColor ->
        hijriDate.value?.apply {
            SIRow {
                SIText(
                    text = day,
                    textColor = textColor,
                    textSize = TextSize.Banner,
                )
                SIWidthSpace(value = 12)
                SIText(
                    text = monthAr,
                    textColor = textColor,
                    textSize = TextSize.Banner,
                )
            }
            SIText(
                modifier = Modifier.align(Alignment.BottomEnd),
                text = optString(R.string.dynamic_hijri_year, year),
                textColor = textColor,
                textSize = TextSize.Small,
            )
        }
            ?: SIImage(resId = R.drawable.caligraphi)
    }
}
