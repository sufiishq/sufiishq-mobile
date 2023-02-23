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

package pk.sufiishq.app.ui.screen.theme

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.esentsov.PackagePrivate
import pk.sufiishq.app.ui.components.ContentBackground
import pk.sufiishq.app.utils.ImageRes
import pk.sufiishq.app.utils.TextRes
import pk.sufiishq.app.utils.extention.optString
import pk.sufiishq.aurora.components.SIImage
import pk.sufiishq.aurora.components.SIText
import pk.sufiishq.aurora.components.SIWidthSpace
import pk.sufiishq.aurora.components.TextSize
import pk.sufiishq.aurora.layout.SIBox
import pk.sufiishq.aurora.layout.SICard
import pk.sufiishq.aurora.layout.SIColumn
import pk.sufiishq.aurora.layout.SIRow
import pk.sufiishq.aurora.theme.AuroraColor

@PackagePrivate
@Composable
fun DayNightThemeControl(
    firstItemTranslationY: State<Float>,
    scaleAndVisibility: State<Float>,
    onLightModeCLick: () -> Unit,
    onDarkModeClick: () -> Unit,
    onAutoModeClick: () -> Unit,
) {
    SIBox(
        modifier =
        Modifier.height(220.dp).graphicsLayer {
            translationY = firstItemTranslationY.value * 0.5f
            alpha = 1f - scaleAndVisibility.value
        },
    ) {
        SIRow(modifier = Modifier.fillMaxSize()) {
            SICard(
                bgColor = AuroraColor.Light,
                modifier = Modifier.weight(1f).fillMaxHeight().clickable { onLightModeCLick() },
            ) {
                SIBox(modifier = Modifier.padding(30.dp, 50.dp)) {
                    SIColumn(
                        modifier = Modifier.fillMaxHeight().align(Alignment.Center),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        SIImage(
                            modifier =
                            Modifier.width(60.dp).graphicsLayer {
                                translationY = firstItemTranslationY.value * 0.12f
                                scaleX = 1f - scaleAndVisibility.value * 0.5f
                                scaleY = 1f - scaleAndVisibility.value * 0.5f
                                rotationZ = firstItemTranslationY.value * 0.25f
                                alpha = 0.8f
                            },
                            resId = ImageRes.day,
                            tintColor = AuroraColor.Dark,
                        )
                        SIText(
                            text = optString(TextRes.label_light),
                            textColor = AuroraColor.Dark,
                            textSize = TextSize.Large,
                        )
                    }
                }
            }
            SIWidthSpace(value = 12)
            SICard(
                bgColor = AuroraColor.Dark,
                modifier = Modifier.weight(1f).fillMaxHeight().clickable { onDarkModeClick() },
            ) {
                SIBox(modifier = Modifier.padding(30.dp, 50.dp)) {
                    SIColumn(
                        modifier = Modifier.fillMaxHeight().align(Alignment.Center),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        SIImage(
                            modifier =
                            Modifier.width(60.dp).graphicsLayer {
                                translationY = firstItemTranslationY.value * 0.12f
                                scaleX = 1f - scaleAndVisibility.value * 0.5f
                                scaleY = 1f - scaleAndVisibility.value * 0.5f
                                rotationZ = (firstItemTranslationY.value * 0.25f) * -1f
                                alpha = 0.8f
                            },
                            resId = ImageRes.night,
                            tintColor = AuroraColor.Light,
                        )
                        SIText(
                            text = optString(TextRes.label_dark),
                            textColor = AuroraColor.Light,
                            textSize = TextSize.Large,
                        )
                    }
                }
            }
        }

        SICard(
            modifier = Modifier.size(90.dp).clip(CircleShape),
            bgColor = AuroraColor.Background,
        ) {
            ContentBackground {
                SICard(
                    modifier =
                    Modifier.fillMaxSize(0.75f).clip(CircleShape).clickable { onAutoModeClick() },
                    bgColor = AuroraColor.SecondaryVariant,
                ) { contentColor ->
                    SIBox {
                        SIText(
                            textAlign = TextAlign.Center,
                            text = optString(TextRes.label_auto),
                            textColor = contentColor,
                            textSize = TextSize.Small,
                        )
                    }
                }
            }
        }
    }
}
