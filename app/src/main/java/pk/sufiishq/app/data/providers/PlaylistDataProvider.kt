package pk.sufiishq.app.data.providers

import androidx.lifecycle.LiveData
import pk.sufiishq.app.models.Playlist
import pk.sufiishq.aurora.models.DataMenuItem

interface PlaylistDataProvider {
    fun getPopupMenuItems(): List<DataMenuItem>
    fun getShowPlaylistAddUpdateDialog(): LiveData<Playlist?>
    fun getShowConfirmPlaylistDeleteDialog(): LiveData<Playlist?>
    fun getAll(): LiveData<List<Playlist>>
    fun get(id: Int): LiveData<Playlist>
}
