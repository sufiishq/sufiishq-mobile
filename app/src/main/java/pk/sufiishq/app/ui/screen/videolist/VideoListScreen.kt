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

package pk.sufiishq.app.ui.screen.videolist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import pk.sufiishq.app.feature.app.MediaType
import pk.sufiishq.app.feature.media.controller.MediaListController
import pk.sufiishq.app.feature.media.controller.MediaListViewModel
import pk.sufiishq.app.helpers.ScreenType
import pk.sufiishq.app.ui.components.NetworkImage
import pk.sufiishq.app.utils.ImageRes
import pk.sufiishq.app.utils.TextRes
import pk.sufiishq.app.utils.extention.optString
import pk.sufiishq.aurora.components.SIImage
import pk.sufiishq.aurora.layout.SIBox
import pk.sufiishq.aurora.layout.SICard
import pk.sufiishq.aurora.layout.SIParallaxLazyColumn

@Composable
fun VideoListScreen(
    navController: NavController,
    referenceId: String,
    mediaListController: MediaListController = hiltViewModel<MediaListViewModel>(),
) {
    SIParallaxLazyColumn(
        leadingIcon = ImageRes.video_full,
        title = optString(TextRes.title_videos),
        noItemText = optString(TextRes.msg_no_videos),
        data = mediaListController.loadMedia(referenceId, MediaType.Video)
            .collectAsLazyPagingItems(),
    ) { _, item ->
        SICard(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    navController.navigate(ScreenType.VideoPlay.buildRoute(item))
                },
        ) {
            SIBox(modifier = Modifier.fillMaxWidth()) {
                NetworkImage(
                    modifier = Modifier.fillMaxWidth(),
                    url = item.thumbnail ?: "",
                    contentScale = ContentScale.FillWidth,
                )
                SIImage(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp),
                    resId = ImageRes.video_small,
                )
            }
        }
    }
}
