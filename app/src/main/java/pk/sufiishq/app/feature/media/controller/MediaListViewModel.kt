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

package pk.sufiishq.app.feature.media.controller

import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import pk.sufiishq.app.feature.app.MediaType
import pk.sufiishq.app.feature.app.data.repository.MediaRepository
import pk.sufiishq.app.feature.app.model.Media
import javax.inject.Inject

@HiltViewModel
class MediaListViewModel @Inject constructor(
    private val mediaRepository: MediaRepository,
) : ViewModel(), MediaListController {

    private var mediaPagingData: Flow<PagingData<Media>>? = null

    override fun loadMedia(referenceId: String, mediaType: MediaType): Flow<PagingData<Media>> {
        return mediaPagingData ?: Pager(
            PagingConfig(pageSize = 20),
            pagingSourceFactory = pagingSource(referenceId, mediaType),
        )
            .flow
            .also {
                mediaPagingData = it
            }
    }

    private fun pagingSource(
        referenceId: String,
        mediaType: MediaType,
    ): () -> PagingSource<Int, Media> {
        return { mediaRepository.loadAllMedia(referenceId, mediaType) }
    }
}
