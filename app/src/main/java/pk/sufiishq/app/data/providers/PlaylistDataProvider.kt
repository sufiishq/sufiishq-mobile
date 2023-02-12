package pk.sufiishq.app.data.providers

import androidx.lifecycle.LiveData
import pk.sufiishq.app.models.Playlist
import pk.sufiishq.aurora.models.DataMenuItem

interface PlaylistDataProvider {
    fun getPopupMenuItems(): List<DataMenuItem>
    fun showAddUpdatePlaylistDialog(): LiveData<Playlist?>
    fun showConfirmDeletePlaylistDialog(): LiveData<Playlist?>
    fun getAll(): LiveData<List<Playlist>>
    fun get(id: Int): LiveData<Playlist>
    fun add(playlist: Playlist)
    fun showAddUpdatePlaylistDialog(playlist: Playlist?)
    fun showConfirmDeletePlaylistDialog(playlist: Playlist?)
    fun update(playlist: Playlist)
    fun delete(playlist: Playlist)
}
