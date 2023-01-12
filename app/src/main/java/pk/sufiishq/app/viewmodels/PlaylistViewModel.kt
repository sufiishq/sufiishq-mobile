package pk.sufiishq.app.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import pk.sufiishq.app.core.event.dispatcher.EventDispatcher
import pk.sufiishq.app.core.event.events.Event
import pk.sufiishq.app.core.event.events.PlaylistEvents
import pk.sufiishq.app.core.event.exception.UnhandledEventException
import pk.sufiishq.app.data.providers.PlaylistDataProvider
import pk.sufiishq.app.data.repository.KalamRepository
import pk.sufiishq.app.data.repository.PlaylistRepository
import pk.sufiishq.app.models.Kalam
import pk.sufiishq.app.models.Playlist
import pk.sufiishq.app.utils.toast

@HiltViewModel
class PlaylistViewModel @Inject constructor(
    private val app: Application,
    private val playlistRepository: PlaylistRepository,
    private val kalamRepository: KalamRepository
) : BaseViewModel(app), PlaylistDataProvider {

    init {
        EventDispatcher.getInstance().registerEventHandler(this)
    }

    private val showPlaylistDialog = MutableLiveData<Kalam?>(null)
    private val showAddUpdatePlaylistDialog = MutableLiveData<Playlist?>(null)
    private val showConfirmPlaylistDeleteDialog = MutableLiveData<Playlist?>(null)

    override fun getShowPlaylistDialog(): LiveData<Kalam?> {
        return showPlaylistDialog
    }

    override fun getShowPlaylistAddUpdateDialog(): LiveData<Playlist?> {
        return showAddUpdatePlaylistDialog
    }

    override fun getShowConfirmPlaylistDeleteDialog(): LiveData<Playlist?> {
        return showConfirmPlaylistDeleteDialog
    }

    override fun getAll(): LiveData<List<Playlist>> = playlistRepository.loadAll()

    override fun get(id: Int): LiveData<Playlist> = playlistRepository.load(id)

    private fun add(playlist: Playlist) {
        viewModelScope.launch {
            playlistRepository.add(playlist)
        }
    }

    private fun setShowPlaylistDialog(kalam: Kalam?) {
        showPlaylistDialog.postValue(kalam)
    }

    private fun setShowPlaylistAddUpdateDialog(playlist: Playlist?) {
        showAddUpdatePlaylistDialog.postValue(playlist)
    }

    private fun setShowPlaylistConfirmDeleteDialog(playlist: Playlist?) {
        showConfirmPlaylistDeleteDialog.postValue(playlist)
    }

    private fun update(playlist: Playlist) {
        viewModelScope.launch {
            playlistRepository.update(playlist)
        }
    }

    private fun delete(playlist: Playlist) {

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

    private fun addToPlaylist(kalam: Kalam, playlist: Playlist) {
        viewModelScope.launch {
            kalam.playlistId = playlist.id
            kalamRepository.update(kalam)
            app.toast("${kalam.title} added in ${playlist.title} Playlist")
        }
    }

    /*=======================================*/
    // HANDLE PLAYLIST EVENTS
    /*=======================================*/

    override fun onEvent(event: Event) {
        when (event) {
            is PlaylistEvents.ShowPlaylistDialog -> setShowPlaylistDialog(event.kalam)
            is PlaylistEvents.AddToPlaylist -> addToPlaylist(event.kalam, event.playlist)
            is PlaylistEvents.ShowAddUpdatePlaylistDialog -> setShowPlaylistAddUpdateDialog(event.playlist)
            is PlaylistEvents.ShowConfirmDeletePlaylistDialog -> setShowPlaylistConfirmDeleteDialog(
                event.playlist
            )
            is PlaylistEvents.Add -> add(event.playlist)
            is PlaylistEvents.Update -> update(event.playlist)
            is PlaylistEvents.Delete -> delete(event.playlist)
            else -> throw UnhandledEventException(event, this)
        }
    }
}