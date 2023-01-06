package pk.sufiishq.app.data.repository

import androidx.lifecycle.LiveData
import javax.inject.Inject
import pk.sufiishq.app.data.dao.PlaylistDao
import pk.sufiishq.app.models.Playlist

class PlaylistRepository @Inject constructor(private val playlistDao: PlaylistDao) {

    fun loadAll(): LiveData<List<Playlist>> = playlistDao.getAll()

    fun load(id: Int): LiveData<Playlist> = playlistDao.get(id)

    suspend fun add(playlist: Playlist) = playlistDao.add(playlist)

    suspend fun update(playlist: Playlist) = playlistDao.update(playlist)

    suspend fun delete(playlist: Playlist) = playlistDao.delete(playlist)

    fun countAll(): LiveData<Int> = playlistDao.countAll()
}