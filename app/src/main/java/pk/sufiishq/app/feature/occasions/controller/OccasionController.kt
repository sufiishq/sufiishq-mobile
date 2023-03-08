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
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import pk.sufiishq.app.feature.app.MediaType
import pk.sufiishq.app.feature.occasions.OccasionType
import pk.sufiishq.app.feature.occasions.model.Occasion

interface OccasionController {

    fun getOccasions(occasionType: OccasionType): Flow<PagingData<Occasion>>
    fun searchOccasion(searchKeyword: String)
    fun getMediaCount(referenceId: String, mediaType: MediaType): LiveData<Int>
}
