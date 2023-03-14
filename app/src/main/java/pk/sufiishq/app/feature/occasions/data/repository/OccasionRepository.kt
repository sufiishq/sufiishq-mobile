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

package pk.sufiishq.app.feature.occasions.data.repository

import androidx.paging.PagingSource
import org.json.JSONObject
import pk.sufiishq.app.feature.app.MediaType
import pk.sufiishq.app.feature.app.data.dao.MediaDao
import pk.sufiishq.app.feature.app.model.Media
import pk.sufiishq.app.feature.occasions.OccasionType
import pk.sufiishq.app.feature.occasions.api.OccasionService
import pk.sufiishq.app.feature.occasions.data.dao.OccasionDao
import pk.sufiishq.app.feature.occasions.model.Occasion
import pk.sufiishq.app.feature.occasions.model.OccasionResponse
import pk.sufiishq.app.feature.occasions.model.OccasionWithMedia
import pk.sufiishq.app.utils.asObjectList
import timber.log.Timber
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton

typealias OccasionPagingSource = PagingSource<Int, Occasion>

@Singleton
class OccasionRepository @Inject constructor(
    private val occasionDao: OccasionDao,
    private val mediaDao: MediaDao,
    private val occasionService: OccasionService,
) {
    private var searchKeyword = ""

    fun load(occasionType: OccasionType): OccasionPagingSource {
        return occasionDao.getAll(occasionType, "%${getSearchKeyword()}%")
    }

    suspend fun getCount(): Int {
        return occasionDao.getCount()
    }

    suspend fun getHighestTimestamp(): Long? {
        return occasionDao.getHighestTimestamp()
    }

    suspend fun getLowestTimestamp(): Long? {
        return occasionDao.getLowestTimestamp()
    }

    fun setSearchKeyword(searchKeyword: String) {
        this.searchKeyword = searchKeyword
    }

    private fun getSearchKeyword(): String {
        return searchKeyword
    }

    suspend fun fetchOccasions(
        offset: Int,
        requestType: String,
        timestamp: Long,
        pageSize: Int = 1,
    ) {
        try {
            occasionService
                .fetchOccasions(offset, pageSize, requestType, timestamp)
                .execute()
                .takeIf { it.isSuccessful && it.body() != null }
                ?.let { it.body()!!.string() }
                ?.let(::transform)
                ?.let { insertAllData(it) }
                ?.takeIf { it.hasNext }
                ?.apply {
                    fetchOccasions(this.offset + 1, requestType, timestamp)
                }
        } catch (ex: UnknownHostException) {
            Timber.e(ex)
        }
    }

    private suspend fun insertAllData(occasionResponse: OccasionResponse): OccasionResponse {
        occasionResponse.data.onEach {
            mediaDao.addAll(it.mediaList)
            occasionDao.add(it.occasion)
        }
        return occasionResponse
    }

    private fun transform(response: String): OccasionResponse {
        return with(JSONObject(response)) {
            OccasionResponse(
                status = getBoolean("status"),
                totalRecord = getInt("total_record"),
                pageSize = getInt("page_size"),
                offset = getInt("offset"),
                hasNext = getBoolean("has_next"),
                hasPrevious = getBoolean("has_previous"),
                data = getJSONArray("data").asObjectList().map(::jsonToOccasionWithMedia),
            )
        }
    }

    private fun jsonToOccasionWithMedia(jsonObject: JSONObject): OccasionWithMedia {
        return with(jsonObject) {
            OccasionWithMedia(
                occasion = Occasion(
                    id = getLong("id"),
                    uuid = getString("uuid"),
                    cover = getString("cover"),
                    title = getString("title"),
                    description = getString("description"),
                    startTimestamp = getLong("startTimestamp"),
                    endTimestamp = getLong("endTimestamp"),
                    hijriDate = getString("hijriDate"),
                    type = OccasionType.valueOf(getString("type")),
                    address = getString("address"),
                    createdAt = getLong("createdAt"),
                    updatedAt = getLong("updatedAt"),
                ),
                mediaList = getJSONArray("media").asObjectList().map(::jsonToMedia),
            )
        }
    }

    private fun jsonToMedia(jsonObject: JSONObject): Media {
        return with(jsonObject) {
            Media(
                id = getLong("id"),
                title = getString("title"),
                thumbnail = getString("thumbnail"),
                src = getString("src"),
                type = MediaType.valueOf(getString("type")),
                referenceId = getString("referenceId"),
            )
        }
    }
}
