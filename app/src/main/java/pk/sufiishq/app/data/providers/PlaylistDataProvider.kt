package pk.sufiishq.app.data.providers

import androidx.lifecycle.LiveData
import pk.sufiishq.app.models.Kalam
import pk.sufiishq.app.models.Playlist

interface PlaylistDataProvider {

    fun getShowPlaylistDialog(): LiveData<Kalam?>
    fun getAll(): LiveData<List<Playlist>>
    fun get(id: Int): LiveData<Playlist>
    fun add(playlist: Playlist)
    fun update(playlist: Playlist)
    fun delete(playlist: Playlist)
}