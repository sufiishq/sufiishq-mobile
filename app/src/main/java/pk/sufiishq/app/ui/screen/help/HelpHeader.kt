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

package pk.sufiishq.app.ui.screen.help

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import io.github.esentsov.PackagePrivate
import pk.sufiishq.app.utils.ImageRes
import pk.sufiishq.aurora.components.SIImage
import pk.sufiishq.aurora.layout.SIBox
import pk.sufiishq.aurora.layout.SIRow
import pk.sufiishq.aurora.theme.AuroraColor

@PackagePrivate
@Composable
fun HelpHeader(
    firstItemTranslationY: State<Float>,
    scaleAndVisibility: State<Float>,
) {
    SIBox(
        modifier = Modifier.fillMaxWidth().height(240.dp),
    ) {
        SIRow(
            modifier = Modifier.fillMaxSize(),
        ) {
            SIBox(
                padding = 6,
                modifier = Modifier.fillMaxSize().weight(1f),
            ) {
                SIBox(
                    modifier =
                    Modifier.align(Alignment.BottomEnd).graphicsLayer {
                        translationY = firstItemTranslationY.value * 0.25f
                        translationX = (firstItemTranslationY.value * 0.67f) * -1f
                        scaleX = (1f - scaleAndVisibility.value).coerceIn(0f, 1f)
                        scaleY = (1f - scaleAndVisibility.value).coerceIn(0f, 1f)
                        alpha = scaleX
                    },
                ) {
                    SIImage(
                        resId = ImageRes.help_left_hand,
                        tintColor = AuroraColor.SecondaryVariant,
                    )
                    SIImage(resId = ImageRes.help_left_hand_front)
                }
            }
            SIBox(
                padding = 6,
                modifier = Modifier.fillMaxSize().weight(1f),
            ) {
                SIBox(
                    modifier =
                    Modifier.graphicsLayer {
                        translationY = firstItemTranslationY.value * 0.25f
                        translationX = firstItemTranslationY.value * 0.67f
                        scaleX = (1f - scaleAndVisibility.value).coerceIn(0f, 1f)
                        scaleY = (1f - scaleAndVisibility.value).coerceIn(0f, 1f)
                        alpha = scaleX
                    }
                        .align(Alignment.BottomStart),
                ) {
                    SIImage(
                        resId = ImageRes.help_right_hand,
                        tintColor = AuroraColor.SecondaryVariant,
                    )
                    SIImage(resId = ImageRes.help_right_hand_front)
                }
            }
        }

        SIBox(
            modifier =
            Modifier.graphicsLayer {
                translationY = firstItemTranslationY.value * 0.6f
                scaleX = (1f - scaleAndVisibility.value).coerceIn(0f, 1f)
                scaleY = (1f - scaleAndVisibility.value).coerceIn(0f, 1f)
                alpha = scaleX
            }
                .align(Alignment.TopCenter)
                .padding(30.dp),
        ) {
            SIImage(resId = ImageRes.help_main, tintColor = AuroraColor.SecondaryVariant)
            SIImage(resId = ImageRes.help_main_front)
        }
    }
}
