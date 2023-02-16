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

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import io.github.esentsov.PackagePrivate
import pk.sufiishq.aurora.components.SIIcon
import pk.sufiishq.aurora.components.SIText
import pk.sufiishq.aurora.components.TextSize
import pk.sufiishq.aurora.layout.SIBox
import pk.sufiishq.aurora.layout.SIColumn
import pk.sufiishq.aurora.layout.SIRow
import pk.sufiishq.aurora.theme.AuroraColor

@PackagePrivate
@SuppressLint("ModifierParameter")
@Composable
fun DashboardButton(
    title: String,
    count: Int,
    icon: Int,
    paddingModifier: Modifier,
    navigate: () -> Unit,
) {
    SIBox(
        modifier = paddingModifier.clip(RoundedCornerShape(5.dp)).clickable { navigate() },
        bgColor = AuroraColor.Background,
    ) { textColor ->
        SIBox(
            modifier = Modifier.padding(20.dp).fillMaxWidth().height(60.dp),
            contentAlignment = Alignment.Center,
        ) {
            SIRow(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                SIIcon(
                    modifier = Modifier.padding(10.dp),
                    resId = icon,
                )

                SIColumn(
                    verticalArrangement = Arrangement.Center,
                ) {
                    SIText(
                        text = "$count",
                        textSize = TextSize.ExtraLarge,
                        textColor = textColor,
                    )
                    SIText(
                        modifier = Modifier.padding(start = 2.dp),
                        text = title,
                        textSize = TextSize.Small,
                        textColor = textColor,
                    )
                }
            }
        }
    }
}
