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

package pk.sufiishq.app.ui.screen.gallery

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import pk.sufiishq.app.feature.gallery.controller.GalleryController
import pk.sufiishq.app.feature.gallery.controller.GalleryViewModel
import pk.sufiishq.app.utils.ImageRes
import pk.sufiishq.app.utils.TextRes
import pk.sufiishq.app.utils.extention.optString
import pk.sufiishq.aurora.layout.SIParallaxLazyColumn
import pk.sufiishq.aurora.widgets.SIServiceCard

@Composable
fun GalleryScreen(
    navController: NavController,
    galleryViewModel: GalleryController = hiltViewModel<GalleryViewModel>(),
) {
    SIParallaxLazyColumn(
        leadingIcon = ImageRes.gallery_full,
        title = optString(TextRes.title_gallery),
        data = galleryViewModel.getData().collectAsLazyPagingItems(),
    ) { _, item ->
        SIServiceCard(
            infoDrawableId = item.leadingIcon,
            title = item.title,
            detail = item.detail,
            onCardClick = {
                navController.navigate(item.route)
            },
        )
    }
}
