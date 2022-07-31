package pk.sufiishq.app.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import pk.sufiishq.app.data.providers.PlaylistDataProvider
import pk.sufiishq.app.data.repository.KalamRepository
import pk.sufiishq.app.data.repository.PlaylistRepository
import pk.sufiishq.app.di.qualifier.IoDispatcher
import pk.sufiishq.app.models.Playlist
import javax.inject.Inject

@HiltViewModel
class PlaylistViewModel @Inject constructor(
    private val playlistRepository: PlaylistRepository,
    private val kalamRepository: KalamRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel(), PlaylistDataProvider {

    override fun getAll(): LiveData<List<Playlist>> = playlistRepository.loadAll()

    override fun get(id: Int): LiveData<Playlist> = playlistRepository.load(id)

    override fun add(playlist: Playlist) {
        viewModelScope.launch(dispatcher) {
            playlistRepository.add(playlist)
        }
    }

    override fun update(playlist: Playlist) {
        viewModelScope.launch(dispatcher) {
            playlistRepository.update(playlist)
        }
    }

    override fun delete(playlist: Playlist) {

        // get all kalam where playlist matched with current playlist
        kalamRepository.loadAllPlaylistKalam(playlist.id).observeForever { kalams ->

            // loop through each kalam and reset the playlist id to 0 and update
            kalams.forEach { kalam ->
                kalam.playlistId = 0
                viewModelScope.launch(dispatcher) { kalamRepository.update(kalam) }
            }

            // delete playlist from playlist table
            viewModelScope.launch(dispatcher) { playlistRepository.delete(playlist) }
        }
    }

    override fun countAll(): LiveData<Int> {
        return playlistRepository.countAll()
    }
}