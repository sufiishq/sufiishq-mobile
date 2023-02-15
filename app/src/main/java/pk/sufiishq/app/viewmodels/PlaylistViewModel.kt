package pk.sufiishq.app.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import pk.sufiishq.app.data.controller.PlaylistController
import pk.sufiishq.app.data.repository.KalamRepository
import pk.sufiishq.app.data.repository.PlaylistRepository
import pk.sufiishq.app.di.qualifier.PlaylistItemPopupMenuItems
import pk.sufiishq.app.helpers.popupmenu.PopupMenu
import pk.sufiishq.app.models.Playlist
import pk.sufiishq.aurora.models.DataMenuItem

@HiltViewModel
class PlaylistViewModel @Inject constructor(
    private val playlistRepository: PlaylistRepository,
    private val kalamRepository: KalamRepository,
    @PlaylistItemPopupMenuItems private val popupMenu: PopupMenu
) : ViewModel(), PlaylistController {

    private val showAddUpdatePlaylistDialog = MutableLiveData<Playlist?>(null)
    private val showConfirmPlaylistDeleteDialog = MutableLiveData<Playlist?>(null)

    override fun getPopupMenuItems(): List<DataMenuItem> {
        return popupMenu.getPopupMenuItems()
    }

    override fun showAddUpdatePlaylistDialog(): LiveData<Playlist?> {
        return showAddUpdatePlaylistDialog
    }

    override fun showConfirmDeletePlaylistDialog(): LiveData<Playlist?> {
        return showConfirmPlaylistDeleteDialog
    }

    override fun getAll(): LiveData<List<Playlist>> = playlistRepository.loadAll()

    override fun get(id: Int): LiveData<Playlist> = playlistRepository.load(id)

    override fun add(playlist: Playlist) {
        viewModelScope.launch {
            playlistRepository.add(playlist)
        }
    }

    override fun showAddUpdatePlaylistDialog(playlist: Playlist?) {
        showAddUpdatePlaylistDialog.postValue(playlist)
    }

    override fun showConfirmDeletePlaylistDialog(playlist: Playlist?) {
        showConfirmPlaylistDeleteDialog.postValue(playlist)
    }

    override fun update(playlist: Playlist) {
        viewModelScope.launch {
            playlistRepository.update(playlist)
        }
    }

    override fun delete(playlist: Playlist) {

        // get all kalam where playlist matched with current playlist
        kalamRepository.loadAllPlaylistKalam(playlist.id).observeForever { kalams ->

            // loop through each kalam and reset the playlist id to 0 and update
            kalams.forEach { kalam ->
                kalam.playlistId = 0
                viewModelScope.launch { kalamRepository.update(kalam) }
            }

            // delete playlist from playlist table
            viewModelScope.launch { playlistRepository.delete(playlist) }
        }
    }

}