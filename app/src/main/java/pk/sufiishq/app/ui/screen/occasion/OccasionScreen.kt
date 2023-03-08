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

package pk.sufiishq.app.ui.screen.occasion

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import pk.sufiishq.app.feature.app.MediaType
import pk.sufiishq.app.feature.occasions.controller.OccasionController
import pk.sufiishq.app.feature.occasions.controller.OccasionViewModel
import pk.sufiishq.app.feature.occasions.model.Occasion
import pk.sufiishq.app.helpers.ScreenType
import pk.sufiishq.app.ui.components.OccasionCard
import pk.sufiishq.app.utils.ImageRes
import pk.sufiishq.app.utils.TextRes
import pk.sufiishq.app.utils.extention.optString
import pk.sufiishq.aurora.components.SIHeightSpace
import pk.sufiishq.aurora.components.SIText
import pk.sufiishq.aurora.components.SIWidthSpace
import pk.sufiishq.aurora.layout.SIColumn
import pk.sufiishq.aurora.layout.SIRow
import pk.sufiishq.aurora.theme.AuroraColor

@Composable
fun OccasionScreen(
    navController: NavController,
    occasion: Occasion,
    occasionController: OccasionController = hiltViewModel<OccasionViewModel>(),
) {
    val photosCount =
        occasionController.getMediaCount(occasion.uuid, MediaType.Image).observeAsState().value
    val videosCount =
        occasionController.getMediaCount(occasion.uuid, MediaType.Video).observeAsState().value

    SIColumn(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(start = 12.dp, top = 12.dp, end = 12.dp, bottom = 18.dp),
    ) {
        OccasionCard(occasion = occasion)

        occasion.description?.takeIf { it.isNotEmpty() }?.let {
            SIHeightSpace(value = 12)
            SIText(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AuroraColor.Background.color())
                    .padding(12.dp),
                text = occasion.description,
                textColor = AuroraColor.OnBackground,
            )
        }

        SIHeightSpace(value = 12)
        SIRow(
            modifier = Modifier.fillMaxWidth(),
        ) {
            OccasionMediaButton(
                rowScope = this,
                leadingIcon = ImageRes.photo_small,
                label = optString(TextRes.dynamic_photo_list_count, photosCount),
                onClick = {
                    navController.navigate(ScreenType.PhotoList.buildRoute(occasion.uuid))
                },
            )
            SIWidthSpace(value = 12)
            OccasionMediaButton(
                rowScope = this,
                label = optString(TextRes.dynamic_video_list_count, videosCount),
                leadingIcon = ImageRes.video_camera_small,
                onClick = {
                    navController.navigate(ScreenType.VideoList.buildRoute(occasion.uuid))
                },
            )
        }
    }
}
