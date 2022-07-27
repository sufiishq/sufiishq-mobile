package pk.sufiishq.app.data.repository

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import pk.sufiishq.app.data.dao.KalamDao
import pk.sufiishq.app.helpers.Screen
import pk.sufiishq.app.models.Kalam
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KalamRepository @Inject constructor(private val kalamDao: KalamDao) {

    private var trackType = Screen.Tracks.ALL
    private var playlistId = 0
    private var searchKeyword = ""

    suspend fun insert(kalam: Kalam) = kalamDao.insert(kalam)

    suspend fun insertAll(allKalams: List<Kalam>) = kalamDao.insertAll(allKalams)

    fun setTrackType(trackType: String) {
        this.trackType = trackType
    }

    fun setPlaylistId(playlistId: Int) {
        this.playlistId = playlistId
    }

    fun setSearchKeyword(searchKeyword: String) {
        this.searchKeyword = searchKeyword
    }

    fun load(): PagingSource<Int, Kalam> {
        return when (trackType) {
            Screen.Tracks.DOWNLOADS -> loadDownloadsKalam(searchKeyword)
            Screen.Tracks.FAVORITES -> loadFavoritesKalam(searchKeyword)
            Screen.Tracks.PLAYLIST -> loadPlaylistKalam(playlistId, searchKeyword)
            else -> loadAllKalam(searchKeyword)
        }
    }

    private fun loadAllKalam(searchKeyword: String): PagingSource<Int, Kalam> {
        return kalamDao.getAllKalam("%${searchKeyword}%")
    }

    private fun loadDownloadsKalam(searchKeyword: String): PagingSource<Int, Kalam> {
        return kalamDao.getDownloadsKalam("%${searchKeyword}%")
    }

    private fun loadFavoritesKalam(searchKeyword: String): PagingSource<Int, Kalam> {
        return kalamDao.getFavoritesKalam("%${searchKeyword}%")
    }

    private fun loadPlaylistKalam(
        playlistId: Int,
        searchKeyword: String
    ): PagingSource<Int, Kalam> {
        return kalamDao.getPlaylistKalam(playlistId, "%${searchKeyword}%")
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

    fun loadAllFromAssets(context: Context): LiveData<List<Kalam>> {

        val allKalam = MutableLiveData<List<Kalam>>()

        CoroutineScope(Dispatchers.IO).launch {
            val list = mutableListOf<Kalam>()

            val fileContent =
                context.assets.open("kalam.json").bufferedReader().use { it.readText() }
            val jsonArray = JSONArray(fileContent)
            (0 until jsonArray.length()).forEach {
                val jsonObject = jsonArray.getJSONObject(it)
                list.add(parseKalam(jsonObject))
            }

            allKalam.postValue(list)
        }

        return allKalam
    }

    @SuppressLint("Range")
    private fun parseKalam(jsonObject: JSONObject): Kalam {
        return Kalam(
            id = 0,
            title = jsonObject.getString(KalamTableInfo.COLUMN_TITLE),
            code = jsonObject.getInt(KalamTableInfo.COLUMN_CODE),
            year = jsonObject.getString(KalamTableInfo.COLUMN_YEAR),
            location = jsonObject.getString(KalamTableInfo.COLUMN_LOCATION),
            onlineSource = jsonObject.getString(KalamTableInfo.ONLINE_SRC),
            offlineSource = jsonObject.getString(KalamTableInfo.OFFLINE_SRC),
            isFavorite = jsonObject.getInt(KalamTableInfo.FAVORITE),
            playlistId = jsonObject.getInt(KalamTableInfo.PLAYLIST_ID)
        )
    }

    private class KalamTableInfo {
        companion object {
            const val COLUMN_ID = "id"
            const val COLUMN_TITLE = "title"
            const val COLUMN_CODE = "code"
            const val COLUMN_YEAR = "year"
            const val COLUMN_LOCATION = "location"
            const val ONLINE_SRC = "online_src"
            const val OFFLINE_SRC = "offline_src"
            const val FAVORITE = "favorite"
            const val PLAYLIST_ID = "playlist_id"
        }
    }
}