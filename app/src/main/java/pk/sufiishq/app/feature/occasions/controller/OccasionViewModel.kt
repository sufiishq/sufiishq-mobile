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

package pk.sufiishq.app.feature.occasions.controller

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import pk.sufiishq.app.di.qualifier.IoDispatcher
import pk.sufiishq.app.feature.app.MediaType
import pk.sufiishq.app.feature.app.data.repository.MediaRepository
import pk.sufiishq.app.feature.occasions.OccasionType
import pk.sufiishq.app.feature.occasions.data.repository.OccasionPagingSource
import pk.sufiishq.app.feature.occasions.data.repository.OccasionRepository
import pk.sufiishq.app.feature.occasions.model.Occasion
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class OccasionViewModel @Inject constructor(
    private val occasionRepository: OccasionRepository,
    private val mediaRepository: MediaRepository,
    @IoDispatcher private val dispatcher: CoroutineContext,
) : ViewModel(), OccasionController {

    private var occasions: Flow<PagingData<Occasion>>? = null

    private fun pagingSource(occasionType: OccasionType): () -> OccasionPagingSource {
        return { occasionRepository.load(occasionType) }
    }

    override fun getOccasions(occasionType: OccasionType): Flow<PagingData<Occasion>> {
        return occasions ?: Pager(
            PagingConfig(pageSize = 10),
            pagingSourceFactory = pagingSource(occasionType),
        )
            .flow
            .also {
                occasions = it
            }
    }

    override fun searchOccasion(searchKeyword: String) {
        occasionRepository.setSearchKeyword(searchKeyword)
    }

    override fun getMediaCount(referenceId: String, mediaType: MediaType): LiveData<Int> {
        val mediaCountLiveData = MutableLiveData(0)
        viewModelScope.launch(dispatcher) {
            mediaRepository.getCount(referenceId, mediaType).let(mediaCountLiveData::postValue)
        }
        return mediaCountLiveData
    }
}
