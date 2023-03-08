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

package pk.sufiishq.app.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import pk.sufiishq.app.feature.occasions.model.Occasion
import pk.sufiishq.app.utils.ImageRes
import pk.sufiishq.app.utils.extention.formatTime
import pk.sufiishq.aurora.components.SIHeightSpace
import pk.sufiishq.aurora.components.SIImage
import pk.sufiishq.aurora.components.SIText
import pk.sufiishq.aurora.components.SIWidthSpace
import pk.sufiishq.aurora.components.TextSize
import pk.sufiishq.aurora.layout.SICard
import pk.sufiishq.aurora.layout.SIColumn
import pk.sufiishq.aurora.layout.SIConstraintLayout
import pk.sufiishq.aurora.layout.SIRow
import pk.sufiishq.aurora.theme.AuroraColor

@Composable
fun OccasionCard(
    occasion: Occasion,
    onCardClick: ((occasion: Occasion) -> Unit)? = null,
) {
    var clickableModifier: Modifier = Modifier

    onCardClick?.let {
        clickableModifier =
            Modifier.clickable {
                it.invoke(occasion)
            }
    }

    SICard(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .then(clickableModifier),
    ) {
        SIConstraintLayout(
            modifier = Modifier
                .fillMaxSize(),
            bgColor = AuroraColor.Background,
        ) {
            val (coverRef, titleRef, metaRef) = createRefs()

            NetworkImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .constrainAs(coverRef) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                    },
                url = occasion.cover,
                contentScale = ContentScale.Crop,
            )

            SIRow(
                modifier = Modifier.constrainAs(titleRef) {
                    start.linkTo(parent.start, 30.dp)
                    bottom.linkTo(parent.bottom, 78.dp)
                },
                bgColor = AuroraColor.SecondaryVariant,
                padding = 12,
                radius = 4,
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                SIText(
                    text = occasion.title,
                    textColor = it,
                    textSize = TextSize.Regular,
                    fontWeight = FontWeight.Bold,
                )
            }

            SIColumn(
                modifier = Modifier
                    .constrainAs(metaRef) {
                        start.linkTo(titleRef.start)
                        top.linkTo(titleRef.bottom)
                        bottom.linkTo(parent.bottom)
                    },
            ) {
                SIRow(verticalAlignment = Alignment.CenterVertically) {
                    SIImage(resId = ImageRes.mini_clock, tintColor = it)
                    SIWidthSpace(value = 6)
                    SIText(text = getTime(occasion), textColor = it, textSize = TextSize.Small)
                }
                SIHeightSpace(value = 8)
                SIRow(verticalAlignment = Alignment.CenterVertically) {
                    SIImage(resId = ImageRes.mini_location, tintColor = it)
                    SIWidthSpace(value = 6)
                    SIText(text = occasion.address, textColor = it, textSize = TextSize.Small)
                }
            }
        }
    }
}

@Composable
private fun getTime(occasion: Occasion): String {
    return buildString {
        append(occasion.startTimestamp.formatTime("MMMM dd, yyyy"))
        if (occasion.endTimestamp > occasion.startTimestamp) {
            append(" - ")
            append(occasion.endTimestamp.formatTime("MMMM dd, yyyy"))
        }
    }
}
