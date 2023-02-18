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

package pk.sufiishq.app.feature.playlist.data.repository

import androidx.lifecycle.LiveData
import pk.sufiishq.app.feature.playlist.data.dao.PlaylistDao
import pk.sufiishq.app.feature.playlist.model.Playlist
import javax.inject.Inject

class PlaylistRepository
@Inject
constructor(
    private val playlistDao: PlaylistDao,
) {

    fun loadAll(): LiveData<List<Playlist>> = playlistDao.getAll()

    fun load(id: Int): LiveData<Playlist> = playlistDao.get(id)

    suspend fun add(playlist: Playlist) = playlistDao.add(playlist)

    suspend fun update(playlist: Playlist) = playlistDao.update(playlist)

    suspend fun delete(playlist: Playlist) = playlistDao.delete(playlist)

    fun countAll(): LiveData<Int> = playlistDao.countAll()
}
