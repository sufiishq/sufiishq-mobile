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

package pk.sufiishq.app.ui.screen.location

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import kotlinx.coroutines.launch
import pk.sufiishq.app.R
import pk.sufiishq.app.utils.extention.launchGoogleMap
import pk.sufiishq.app.utils.extention.optString
import pk.sufiishq.aurora.components.SIButton
import pk.sufiishq.aurora.components.SIHeightSpace
import pk.sufiishq.aurora.components.SIImage
import pk.sufiishq.aurora.components.SIText
import pk.sufiishq.aurora.layout.SIBox
import pk.sufiishq.aurora.layout.SICard
import pk.sufiishq.aurora.layout.SIColumn
import pk.sufiishq.aurora.layout.SIRow
import pk.sufiishq.aurora.theme.AuroraColor

@Composable
fun DarbarLocationScreen() {
    ConstraintLayout(
        modifier =
        Modifier
            .fillMaxSize()
            .padding(start = 12.dp, top = 12.dp, end = 12.dp, bottom = 18.dp),
    ) {
        val (darbarPhoto, locationButtons) = createRefs()

        SICard(
            modifier =
            Modifier.constrainAs(darbarPhoto) {
                start.linkTo(parent.start)
                top.linkTo(parent.top)
                end.linkTo(parent.end)
                bottom.linkTo(locationButtons.top)
                height = Dimension.fillToConstraints
            },
        ) {
            SIImage(
                resId = R.drawable.darbar,
                contentScale = ContentScale.Crop,
            )
        }

        SIColumn(
            modifier =
            Modifier.constrainAs(locationButtons) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            },
        ) {
            SIHeightSpace(value = 12)
            LocationButton(
                location = optString(R.string.loc_pakpattan),
                Pair(30.344721262825235, 73.40368348568946),
            )
            SIHeightSpace(value = 12)
            LocationButton(
                location = optString(R.string.loc_karachi),
                Pair(24.843744, 67.198691),
            )
        }
    }
}

@Composable
private fun LocationButton(
    location: String,
    latLng: Pair<Double, Double>,
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    SICard(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        border = BorderStroke(0.5.dp, AuroraColor.Primary.color()),
    ) {
        SIBox(
            bgColor = AuroraColor.Background,
        ) {
            SIRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp, 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                SIText(
                    text = location,
                    textColor = it,
                    fontWeight = FontWeight.Bold,
                )
                SIButton(
                    text = optString(R.string.label_open),
                    onClick = { coroutineScope.launch { context.launchGoogleMap(latLng) } },
                )
            }
        }
    }
}
