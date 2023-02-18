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

package pk.sufiishq.app.core.playlist.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import pk.sufiishq.app.core.playlist.model.Playlist

@Dao
interface PlaylistDao {

    @Insert suspend fun add(playlist: Playlist)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(playlist: Playlist)

    @Delete suspend fun delete(playlist: Playlist)

    @Query("SELECT * FROM playlist WHERE id = :id")
    fun get(id: Int): LiveData<Playlist>

    @Query(
        "SELECT playlist.*, COUNT(kalam.playlist_id) AS totalKalam FROM playlist " +
            "LEFT JOIN kalam ON kalam.playlist_id = playlist.id " +
            "GROUP by playlist.id " +
            "ORDER BY playlist.id DESC",
    )
    fun getAll(): LiveData<List<Playlist>>

    @Query("SELECT COUNT(*) FROM playlist")
    fun countAll(): LiveData<Int>
}
