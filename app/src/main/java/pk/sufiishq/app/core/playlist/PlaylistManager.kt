/*
 * Copyright 2022-2023 SufiIshq
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pk.sufiishq.app.core.playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
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
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class PlaylistManager
@Inject
constructor(
    private val playlistRepository: PlaylistRepository,
    private val kalamRepository: KalamRepository,
    @IoDispatcher private val dispatcher: CoroutineContext,
) {

    private val showPlaylistDialog = MutableLiveData<Pair<Kalam, List<Playlist>>?>(null)
    private var job: Job? = null

    private fun getAllPlaylist(): LiveData<List<Playlist>> {
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
        job =
            CoroutineScope(dispatcher).launch {
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
