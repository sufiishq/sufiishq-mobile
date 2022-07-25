package pk.sufiishq.app.data.repository

import pk.sufiishq.app.data.dao.PlaylistDao
import pk.sufiishq.app.models.Playlist
import javax.inject.Inject

class PlaylistRepository @Inject constructor(private val playlistDao: PlaylistDao) {

    fun loadAll() = playlistDao.getAll()

    fun load(id: Int) = playlistDao.get(id)

    fun add(playlist: Playlist) = playlistDao.add(playlist)

    fun update(playlist: Playlist) = playlistDao.update(playlist)

    fun delete(playlist: Playlist) = playlistDao.delete(playlist)

    fun countAll() = playlistDao.countAll()
}