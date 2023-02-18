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

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.esentsov.PackagePrivate
import pk.sufiishq.app.core.player.controller.PlayerController
import pk.sufiishq.app.core.kalam.model.KalamInfo
import pk.sufiishq.aurora.components.SISlider

@PackagePrivate
@Composable
fun TrackSlider(
    playerController: PlayerController,
    modifier: Modifier,
    kalamInfo: KalamInfo?,
    onValueChange: (Float) -> Unit = {},
    onValueChangeFinished: (() -> Unit)? = null,
) {
    SISlider(
        modifier = modifier,
        value = kalamInfo?.currentProgress?.toFloat() ?: 0f,
        valueRange = 0f..(kalamInfo?.totalDuration?.toFloat() ?: 0f),
        enabled = kalamInfo?.enableSeekbar ?: false,
        onValueChange = {
            onValueChange(it)
            playerController.updateSeekbarValue(it)
        },
        onValueChangeFinished = {
            onValueChangeFinished?.invoke()
            playerController.onSeekbarChanged(kalamInfo?.currentProgress ?: 0)
        },
    )
}
