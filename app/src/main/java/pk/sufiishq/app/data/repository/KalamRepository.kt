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

package pk.sufiishq.app.data.repository

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import pk.sufiishq.app.data.dao.KalamDao
import pk.sufiishq.app.data.repository.KalamRepository.KalamTableInfo.CODE
import pk.sufiishq.app.data.repository.KalamRepository.KalamTableInfo.FAVORITE
import pk.sufiishq.app.data.repository.KalamRepository.KalamTableInfo.LOCATION
import pk.sufiishq.app.data.repository.KalamRepository.KalamTableInfo.OFFLINE_SRC
import pk.sufiishq.app.data.repository.KalamRepository.KalamTableInfo.ONLINE_SRC
import pk.sufiishq.app.data.repository.KalamRepository.KalamTableInfo.PLAYLIST_ID
import pk.sufiishq.app.data.repository.KalamRepository.KalamTableInfo.RECORDED_DATE
import pk.sufiishq.app.data.repository.KalamRepository.KalamTableInfo.TITLE
import pk.sufiishq.app.di.qualifier.IoDispatcher
import pk.sufiishq.app.helpers.TrackListType
import pk.sufiishq.app.models.Kalam
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class KalamRepository
@Inject
constructor(
    private val kalamDao: KalamDao,
    @IoDispatcher private val dispatcher: CoroutineContext,
) {

    private var trackListType: TrackListType = TrackListType.All()
    private var searchKeyword = ""

    suspend fun insert(kalam: Kalam) = kalamDao.insert(kalam)

    suspend fun insertAll(allKalams: List<Kalam>) = kalamDao.insertAll(allKalams)

    fun getKalam(id: Int): LiveData<Kalam?> {
        return kalamDao.getKalam(id)
    }

    fun setTrackListType(trackListType: TrackListType) {
        this.trackListType = trackListType
    }

    fun getTrackListType(): TrackListType {
        return trackListType
    }

    fun setSearchKeyword(searchKeyword: String) {
        this.searchKeyword = searchKeyword
    }

    fun getSearchKeyword() = searchKeyword

    fun load(): PagingSource<Int, Kalam> {
        return when (val type = trackListType) {
            is TrackListType.Downloads -> loadDownloadsKalam(searchKeyword)
            is TrackListType.Favorites -> loadFavoritesKalam(searchKeyword)
            is TrackListType.Playlist -> loadPlaylistKalam(type.playlistId, searchKeyword)
            else -> loadAllKalam(searchKeyword)
        }
    }

    private fun loadAllKalam(searchKeyword: String): PagingSource<Int, Kalam> {
        return kalamDao.getAllKalam("%$searchKeyword%")
    }

    private fun loadDownloadsKalam(searchKeyword: String): PagingSource<Int, Kalam> {
        return kalamDao.getDownloadsKalam("%$searchKeyword%")
    }

    private fun loadFavoritesKalam(searchKeyword: String): PagingSource<Int, Kalam> {
        return kalamDao.getFavoritesKalam("%$searchKeyword%")
    }

    private fun loadPlaylistKalam(
        playlistId: Int,
        searchKeyword: String,
    ): PagingSource<Int, Kalam> {
        return kalamDao.getPlaylistKalam(playlistId, "%$searchKeyword%")
    }

    fun loadAllPlaylistKalam(playlistId: Int): LiveData<List<Kalam>> {
        return kalamDao.getAllPlaylistKalam(playlistId)
    }

    fun getDefaultKalam(): LiveData<Kalam> {
        return kalamDao.getFirstKalam()
    }

    fun countAll(): LiveData<Int> {
        return kalamDao.countAll()
    }

    fun countDownloads(): LiveData<Int> {
        return kalamDao.countDownloads()
    }

    fun countFavorites(): LiveData<Int> {
        return kalamDao.countFavorites()
    }

    suspend fun update(kalam: Kalam) {
        kalamDao.update(kalam)
    }

    suspend fun delete(kalam: Kalam) {
        kalamDao.delete(kalam)
    }

    suspend fun loadAllFromAssets(context: Context): List<Kalam> {
        return withContext(dispatcher) {
            val list = mutableListOf<Kalam>()
            val fileContent =
                context.assets.open("kalam.json").bufferedReader().use { it.readText() }
            val jsonArray = JSONArray(fileContent)
            (0 until jsonArray.length()).forEach {
                val jsonObject = jsonArray.getJSONObject(it)
                list.add(parseKalam(jsonObject))
            }
            list
        }
    }

    @SuppressLint("Range")
    private fun parseKalam(jsonObject: JSONObject): Kalam {
        return Kalam(
            id = 0,
            title = jsonObject.getString(TITLE),
            code = jsonObject.getInt(CODE),
            recordeDate = jsonObject.getString(RECORDED_DATE),
            location = jsonObject.getString(LOCATION),
            onlineSource = jsonObject.getString(ONLINE_SRC),
            offlineSource = jsonObject.getString(OFFLINE_SRC),
            isFavorite = jsonObject.getInt(FAVORITE),
            playlistId = jsonObject.getInt(PLAYLIST_ID),
        )
    }

    object KalamTableInfo {
        const val ID = "id"
        const val TITLE = "title"
        const val CODE = "code"
        const val RECORDED_DATE = "recorded_date"
        const val LOCATION = "location"
        const val ONLINE_SRC = "online_src"
        const val OFFLINE_SRC = "offline_src"
        const val FAVORITE = "favorite"
        const val PLAYLIST_ID = "playlist_id"
    }
}
