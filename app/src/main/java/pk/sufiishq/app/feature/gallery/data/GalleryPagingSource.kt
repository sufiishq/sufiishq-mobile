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

package pk.sufiishq.app.feature.gallery.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import pk.sufiishq.app.feature.gallery.model.Section
import pk.sufiishq.app.feature.occasions.OccasionType
import pk.sufiishq.app.helpers.ScreenType
import pk.sufiishq.app.utils.FEATURE_VIDEO_REFERENCE_ID
import pk.sufiishq.app.utils.ImageRes
import pk.sufiishq.app.utils.TextRes
import pk.sufiishq.app.utils.getString

class GalleryPagingSource : PagingSource<Int, Section>() {

    override fun getRefreshKey(state: PagingState<Int, Section>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Section> {
        val data = listOf(
            Section(
                leadingIcon = ImageRes.niaz_small,
                title = getString(TextRes.title_niaz),
                detail = getString(TextRes.detail_card_niaz),
                route = ScreenType.OccasionList.buildRoute(OccasionType.Niaz),
            ),
            Section(
                leadingIcon = ImageRes.urs_small,
                title = getString(TextRes.title_urs),
                detail = getString(TextRes.detail_card_urs),
                route = ScreenType.OccasionList.buildRoute(OccasionType.Urs),
            ),
            Section(
                leadingIcon = ImageRes.video_small,
                title = getString(TextRes.title_videos),
                detail = getString(TextRes.detail_card_videos),
                route = ScreenType.VideoList.buildRoute(FEATURE_VIDEO_REFERENCE_ID),
            ),
        )

        return LoadResult.Page(data, null, null)
    }
}
