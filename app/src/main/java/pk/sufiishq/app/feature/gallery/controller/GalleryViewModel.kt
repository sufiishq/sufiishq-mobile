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

package pk.sufiishq.app.feature.gallery.controller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import pk.sufiishq.app.di.qualifier.IoDispatcher
import pk.sufiishq.app.feature.app.MediaType
import pk.sufiishq.app.feature.app.data.repository.MediaRepository
import pk.sufiishq.app.feature.gallery.data.GalleryPagingSource
import pk.sufiishq.app.feature.gallery.model.Section
import pk.sufiishq.app.utils.Constants.FEATURE_VIDEO_REFERENCE_ID
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class GalleryViewModel @Inject constructor(
    @IoDispatcher private val dispatcher: CoroutineContext,
    private val mediaRepository: MediaRepository,
) : ViewModel(), GalleryController {

    init {
        loadFeatureVideos()
    }

    private val listData = Pager(PagingConfig(pageSize = Int.MAX_VALUE), pagingSourceFactory = {
        GalleryPagingSource()
    }).flow.cachedIn(viewModelScope)

    override fun getData(): Flow<PagingData<Section>> {
        return listData
    }

    private fun loadFeatureVideos() {
        viewModelScope.launch(dispatcher) {
            mediaRepository
                .loadAllMediaWithSuspend(FEATURE_VIDEO_REFERENCE_ID, MediaType.Video)
                .takeIf { it.isEmpty() }
                ?.apply {
                    mediaRepository.fetchMedia(FEATURE_VIDEO_REFERENCE_ID)
                }
        }
    }
}
