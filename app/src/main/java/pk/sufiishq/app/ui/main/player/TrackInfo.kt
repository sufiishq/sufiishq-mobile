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
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.Dimension
import io.github.esentsov.PackagePrivate
import pk.sufiishq.app.core.kalam.model.KalamInfo
import pk.sufiishq.app.utils.formatDateAs
import pk.sufiishq.app.utils.formatTime
import pk.sufiishq.aurora.components.SIMarqueeText
import pk.sufiishq.aurora.components.SIText
import pk.sufiishq.aurora.components.TextSize
import pk.sufiishq.aurora.layout.SICard
import pk.sufiishq.aurora.layout.SIConstraintLayout
import pk.sufiishq.aurora.theme.AuroraColor

@PackagePrivate
@Composable
fun TrackInfo(
    kalamInfo: KalamInfo?,
) {
    SICard(
        modifier =
        Modifier.fillMaxSize().padding(start = 30.dp, top = 12.dp, end = 12.dp, bottom = 12.dp),
        elevation = 0.dp,
        bgColor = AuroraColor.Background,
    ) { textColor ->
        SIConstraintLayout(
            modifier =
            Modifier.fillMaxSize().padding(top = 8.dp, start = 66.dp, end = 10.dp, bottom = 8.dp),
        ) {
            val (titleRef, recordedDateRef, currentProgressRef, totalDurationRef) = createRefs()

            SIMarqueeText(
                text = (kalamInfo?.kalam?.title ?: ""),
                textColor = textColor,
                backgroundColor = AuroraColor.Background,
                modifier =
                Modifier.constrainAs(titleRef) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    width = Dimension.fillToConstraints
                },
            )

            SIText(
                modifier =
                Modifier.constrainAs(recordedDateRef) {
                    top.linkTo(titleRef.top)
                    end.linkTo(parent.end)
                    bottom.linkTo(titleRef.bottom)
                },
                text = (kalamInfo?.kalam?.recordeDate?.formatDateAs() ?: ""),
                textSize = TextSize.Small,
                textColor = textColor,
            )

            SIText(
                modifier =
                Modifier.constrainAs(currentProgressRef) {
                    start.linkTo(titleRef.start)
                    bottom.linkTo(parent.bottom)
                },
                text = kalamInfo?.currentProgress?.formatTime ?: 0.formatTime,
                textColor = textColor,
                textSize = TextSize.ExtraSmall,
            )

            SIText(
                modifier =
                Modifier.constrainAs(totalDurationRef) {
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                },
                text = kalamInfo?.totalDuration?.formatTime ?: 0.formatTime,
                textColor = textColor,
                textSize = TextSize.ExtraSmall,
            )
        }
    }
}
