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

package pk.sufiishq.app.core.playlist.controller

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import pk.sufiishq.app.core.kalam.data.repository.KalamRepository
import pk.sufiishq.app.core.playlist.data.repository.PlaylistRepository
import pk.sufiishq.app.di.qualifier.PlaylistItemPopupMenuItems
import pk.sufiishq.app.helpers.popupmenu.PopupMenu
import pk.sufiishq.app.core.playlist.model.Playlist
import pk.sufiishq.aurora.models.DataMenuItem
import javax.inject.Inject

@HiltViewModel
class PlaylistViewModel
@Inject
constructor(
    private val playlistRepository: PlaylistRepository,
    private val kalamRepository: KalamRepository,
    @PlaylistItemPopupMenuItems private val popupMenu: PopupMenu,
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
        viewModelScope.launch { playlistRepository.add(playlist) }
    }

    override fun showAddUpdatePlaylistDialog(playlist: Playlist?) {
        showAddUpdatePlaylistDialog.postValue(playlist)
    }

    override fun showConfirmDeletePlaylistDialog(playlist: Playlist?) {
        showConfirmPlaylistDeleteDialog.postValue(playlist)
    }

    override fun update(playlist: Playlist) {
        viewModelScope.launch { playlistRepository.update(playlist) }
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
