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
import pk.sufiishq.app.core.playlist.model.Playlist
import pk.sufiishq.aurora.models.DataMenuItem

interface PlaylistController {
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
