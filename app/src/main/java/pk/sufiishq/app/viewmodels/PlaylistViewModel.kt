package pk.sufiishq.app.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.schedulers.Schedulers
import pk.sufiishq.app.data.providers.PlaylistDataProvider
import pk.sufiishq.app.data.repository.KalamRepository
import pk.sufiishq.app.data.repository.PlaylistRepository
import pk.sufiishq.app.models.Playlist
import javax.inject.Inject

@HiltViewModel
class PlaylistViewModel @Inject constructor(
    val playlistRepository: PlaylistRepository,
    val kalamRepository: KalamRepository
) : ViewModel(), PlaylistDataProvider {

    override fun getAll() = playlistRepository.loadAll()

    override fun get(id: Int) = playlistRepository.load(id)

    override fun add(playlist: Playlist) = playlistRepository.add(playlist)

    override fun update(playlist: Playlist) = playlistRepository.update(playlist)

    override fun delete(playlist: Playlist) {
        Completable.fromAction {

            // get all kalam where playlist matched with current playlist
            val kalams = kalamRepository.loadAllPlaylistKalam(playlist.id)

            // loop through each kalam and reset the playlist id to 0 and update
            kalams.forEach { kalam ->
                kalam.playlistId = 0
                kalamRepository.update(kalam)
            }

            // delete playlist from playlist table
            playlistRepository.delete(playlist)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(mainThread())
            .subscribe()
    }
}