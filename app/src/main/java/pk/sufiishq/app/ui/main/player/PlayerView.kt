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

package pk.sufiishq.app.ui.main.player

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import pk.sufiishq.app.annotations.ExcludeFromJacocoGeneratedReport
import pk.sufiishq.app.feature.player.controller.PlayerController
import pk.sufiishq.app.feature.player.controller.PlayerViewModel
import pk.sufiishq.app.utils.extention.formatTime
import pk.sufiishq.app.utils.fakePlayerController
import pk.sufiishq.app.utils.rem
import pk.sufiishq.aurora.components.SIText
import pk.sufiishq.aurora.layout.SIAuroraSurface
import pk.sufiishq.aurora.layout.SIBox
import pk.sufiishq.aurora.layout.SIRow
import pk.sufiishq.aurora.theme.AuroraColor
import pk.sufiishq.aurora.theme.AuroraDark
import pk.sufiishq.aurora.theme.AuroraLight

@Composable
fun PlayerView(
    playerController: PlayerController = hiltViewModel<PlayerViewModel>(),
) {
    val sliderThumbPressed = rem(false)
    val showSliderLabel = remember { derivedStateOf { sliderThumbPressed.value } }
    val kalamInfo = playerController.getKalamInfo().observeAsState()

    SIAuroraSurface(modifier = Modifier.fillMaxWidth().height(90.dp)) {
        SIBox(modifier = Modifier.fillMaxSize()) {
            SIBox(modifier = Modifier.padding(top = 6.dp)) {
                TrackInfo(kalamInfo = kalamInfo.value)

                PlayPauseButton(
                    playerController = playerController,
                    kalamInfo = kalamInfo,
                    boxScope = this,
                )
            }

            TrackSlider(
                playerController = playerController,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .height(0.dp),
                kalamInfo = kalamInfo.value,
                onValueChange = { sliderThumbPressed.value = true },
                onValueChangeFinished = { sliderThumbPressed.value = false },
            )
        }
    }

    if (showSliderLabel.value) {
        val localDensity = LocalDensity.current

        SIBox(
            modifier =
            Modifier
                .fillMaxWidth()
                .graphicsLayer {
                    translationY = localDensity.run { 80.dp.toPx() * -1 }
                },
        ) {
            SIRow(
                bgColor = AuroraColor.SecondaryVariant,
                padding = 8,
                radius = 4,
            ) {
                SIText(
                    text = kalamInfo.value?.currentProgress?.formatTime ?: 0.formatTime,
                    textColor = it,
                )
            }
        }
    }
}

@ExcludeFromJacocoGeneratedReport
@Preview(showBackground = true)
@Composable
fun PlayerPreviewLight() {
    AuroraLight {
        PlayerView(
            playerController = fakePlayerController(),
        )
    }
}

@ExcludeFromJacocoGeneratedReport
@Preview(showBackground = true)
@Composable
fun PlayerPreviewDark() {
    AuroraDark {
        PlayerView(
            playerController = fakePlayerController(),
        )
    }
}
