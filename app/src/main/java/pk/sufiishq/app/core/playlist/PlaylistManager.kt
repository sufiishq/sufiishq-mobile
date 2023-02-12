package pk.sufiishq.app.core.playlist

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import pk.sufiishq.app.R
import pk.sufiishq.app.data.repository.KalamRepository
import pk.sufiishq.app.data.repository.PlaylistRepository
import pk.sufiishq.app.di.qualifier.IoDispatcher
import pk.sufiishq.app.models.Kalam
import pk.sufiishq.app.models.Playlist
import pk.sufiishq.app.utils.quickToast
import kotlin.coroutines.CoroutineContext

class PlaylistManager @Inject constructor(
    private val playlistRepository: PlaylistRepository,
    private val kalamRepository: KalamRepository,
    @ApplicationContext private val appContext: Context,
    @IoDispatcher private val dispatcher: CoroutineContext
) {

    private val showPlaylistDialog = MutableLiveData<Pair<Kalam, List<Playlist>>?>(null)
    private var job: Job? = null

    fun getAllPlaylist(): LiveData<List<Playlist>> {
        return playlistRepository.loadAll()
    }

    fun addToPlaylist(kalam: Kalam, playlist: Playlist) {
        dismissPlaylistDialog()

        CoroutineScope(dispatcher).launch {
            kalam.playlistId = playlist.id
            kalamRepository.update(kalam)
            quickToast(R.string.dynamic_kalam_added_in_playlist, kalam.title, playlist.title)
        }
    }

    fun showPlaylistDialog(): LiveData<Pair<Kalam, List<Playlist>>?> {
        return showPlaylistDialog
    }

    fun showPlaylistDialog(kalam: Kalam) {
        job = CoroutineScope(dispatcher).launch {
            getAllPlaylist().asFlow().cancellable().collectLatest {
                if (it.isNotEmpty()) {
                    showPlaylistDialog.postValue(Pair(kalam, it))
                } else {
                    quickToast(R.string.label_no_playlist_found)
                }
            }
        }
    }

    fun dismissPlaylistDialog() {
        job?.cancel()
        showPlaylistDialog.postValue(null)
    }
}