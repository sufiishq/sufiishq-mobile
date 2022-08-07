package pk.sufiishq.app.data.repository

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.sqlite.db.SimpleSQLiteQuery
import io.reactivex.Observable
import org.json.JSONArray
import org.json.JSONObject
import pk.sufiishq.app.data.dao.KalamDao
import pk.sufiishq.app.data.repository.KalamRepository.KalamTableInfo.Companion.CODE
import pk.sufiishq.app.data.repository.KalamRepository.KalamTableInfo.Companion.FAVORITE
import pk.sufiishq.app.data.repository.KalamRepository.KalamTableInfo.Companion.ID
import pk.sufiishq.app.data.repository.KalamRepository.KalamTableInfo.Companion.LOCATION
import pk.sufiishq.app.data.repository.KalamRepository.KalamTableInfo.Companion.OFFLINE_SRC
import pk.sufiishq.app.data.repository.KalamRepository.KalamTableInfo.Companion.ONLINE_SRC
import pk.sufiishq.app.data.repository.KalamRepository.KalamTableInfo.Companion.PLAYLIST_ID
import pk.sufiishq.app.data.repository.KalamRepository.KalamTableInfo.Companion.RECORDED_DATE
import pk.sufiishq.app.data.repository.KalamRepository.KalamTableInfo.Companion.TITLE
import pk.sufiishq.app.helpers.Screen
import pk.sufiishq.app.helpers.Screen.Tracks.DOWNLOADS
import pk.sufiishq.app.helpers.Screen.Tracks.FAVORITES
import pk.sufiishq.app.helpers.Screen.Tracks.PLAYLIST
import pk.sufiishq.app.models.Kalam
import pk.sufiishq.app.utils.KALAM_TABLE_NAME
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KalamRepository @Inject constructor(private val kalamDao: KalamDao) {

    private var trackType = Screen.Tracks.ALL
    private var playlistId = 0
    private var searchKeyword = ""

    suspend fun insert(kalam: Kalam) = kalamDao.insert(kalam)

    suspend fun insertAll(allKalams: List<Kalam>) = kalamDao.insertAll(allKalams)

    fun getKalam(id: Int): LiveData<Kalam?> {
        return kalamDao.getKalam(id)
    }

    fun setTrackType(trackType: String) {
        this.trackType = trackType
    }

    fun setPlaylistId(playlistId: Int) {
        this.playlistId = playlistId
    }

    fun setSearchKeyword(searchKeyword: String) {
        this.searchKeyword = searchKeyword
    }

    fun getTrackType() = trackType

    fun getPlaylistId() = playlistId

    fun getSearchKeyword() = searchKeyword

    fun load(): PagingSource<Int, Kalam> {
        return when (trackType) {
            DOWNLOADS -> loadDownloadsKalam(searchKeyword)
            FAVORITES -> loadFavoritesKalam(searchKeyword)
            PLAYLIST -> loadPlaylistKalam(playlistId, searchKeyword)
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

    fun getNextKalam(
        id: Int,
        trackType: String,
        playlistId: Int,
        shuffle: Boolean
    ): LiveData<Kalam?> {

        if (shuffle) return getRandomKalam(trackType, playlistId)

        val query = buildString {
            append("SELECT * FROM kalam ")
            append("WHERE $ID < $id ")

            when (trackType) {
                DOWNLOADS -> append("AND $OFFLINE_SRC != '' ")
                FAVORITES -> append("AND $FAVORITE = 1 ")
                PLAYLIST -> append("AND $PLAYLIST_ID = $playlistId ")
            }

            append("ORDER BY $ID DESC ")
            append("LIMIT 1")
        }

        return kalamDao.getSingleKalam(SimpleSQLiteQuery(query))
    }

    fun getPreviousKalam(
        id: Int,
        trackType: String,
        playlistId: Int,
        shuffle: Boolean
    ): LiveData<Kalam?> {

        if (shuffle) return getRandomKalam(trackType, playlistId)

        val query = buildString {
            append("SELECT * FROM kalam ")
            append("WHERE $ID > $id ")

            when (trackType) {
                DOWNLOADS -> append("AND $OFFLINE_SRC != '' ")
                FAVORITES -> append("AND $FAVORITE = 1 ")
                PLAYLIST -> append("AND $PLAYLIST_ID = $playlistId ")
            }

            append("ORDER BY $ID ASC ")
            append("LIMIT 1")
        }

        return kalamDao.getSingleKalam(SimpleSQLiteQuery(query))
    }

    fun getRandomKalam(trackType: String, playlistId: Int): LiveData<Kalam?> {
        val query = buildString {
            append("SELECT * FROM $KALAM_TABLE_NAME ")

            when (trackType) {
                DOWNLOADS -> append("WHERE $OFFLINE_SRC != '' ")
                FAVORITES -> append("WHERE $FAVORITE = 1 ")
                PLAYLIST -> append("WHERE $PLAYLIST_ID = $playlistId ")
            }

            append("ORDER BY random() ")
            append("LIMIT 1")
        }

        return kalamDao.getSingleKalam(SimpleSQLiteQuery(query))
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

    fun loadAllFromAssets(context: Context): Observable<List<Kalam>> {

        return Observable.create { emitter ->
            val list = mutableListOf<Kalam>()
            val fileContent =
                context.assets.open("kalam.json").bufferedReader().use { it.readText() }
            val jsonArray = JSONArray(fileContent)
            (0 until jsonArray.length()).forEach {
                val jsonObject = jsonArray.getJSONObject(it)
                list.add(parseKalam(jsonObject))
            }
            emitter.onNext(list)
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
            playlistId = jsonObject.getInt(PLAYLIST_ID)
        )
    }

    private class KalamTableInfo {
        companion object {
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
}