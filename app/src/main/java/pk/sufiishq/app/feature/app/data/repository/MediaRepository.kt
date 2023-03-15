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

package pk.sufiishq.app.feature.app.data.repository

import androidx.paging.PagingSource
import org.json.JSONObject
import pk.sufiishq.app.feature.app.MediaType
import pk.sufiishq.app.feature.app.api.MediaService
import pk.sufiishq.app.feature.app.data.dao.MediaDao
import pk.sufiishq.app.feature.app.model.Media
import pk.sufiishq.app.utils.asObjectList
import timber.log.Timber
import javax.inject.Inject

class MediaRepository @Inject constructor(
    private val mediaService: MediaService,
    private val mediaDao: MediaDao,
) {

    suspend fun loadAllMediaWithSuspend(referenceId: String, mediaType: MediaType): List<Media> {
        return mediaDao.getAllWithSuspend(referenceId, mediaType)
    }

    fun loadAllMedia(referenceId: String, mediaType: MediaType): PagingSource<Int, Media> {
        return mediaDao.getAll(referenceId, mediaType)
    }

    suspend fun getCount(referenceId: String, mediaType: MediaType): Int {
        return mediaDao.getCount(referenceId, mediaType)
    }

    suspend fun fetchMedia(referenceId: String) {
        try {
            mediaService
                .fetch(referenceId)
                .execute()
                .takeIf { it.isSuccessful && it.body() != null }
                ?.let { it.body()!!.string() }
                ?.let(::transform)
                ?.let { mediaDao.addAll(it) }
        } catch (ex: Exception) {
            Timber.e(ex)
        }
    }

    private fun transform(response: String): List<Media> {
        return JSONObject(response)
            .getJSONArray("data")
            .asObjectList()
            .map(::jsonToMedia)
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
