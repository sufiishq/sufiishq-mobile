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

package pk.sufiishq.app.core.kalam.data.dao

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SimpleSQLiteQuery
import pk.sufiishq.app.core.kalam.model.Kalam

@Dao
interface KalamDao {

    @Query("SELECT * FROM kalam ORDER BY id DESC LIMIT 1")
    fun getFirstKalam(): LiveData<Kalam>

    @Query("SELECT * FROM kalam WHERE id = :id")
    fun getKalam(id: Int): LiveData<Kalam?>

    @Query(
        "SELECT * FROM kalam " +
            "WHERE (LOWER(title) LIKE :searchKeyword OR LOWER(location) LIKE :searchKeyword OR recorded_date LIKE :searchKeyword)" +
            "ORDER BY id DESC",
    )
    fun getAllKalam(searchKeyword: String): PagingSource<Int, Kalam>

    @Query(
        "SELECT * FROM kalam " +
            "WHERE (LOWER(title) LIKE :searchKeyword OR LOWER(location) LIKE :searchKeyword OR recorded_date LIKE :searchKeyword) AND offline_src != ''" +
            "ORDER BY id DESC",
    )
    fun getDownloadsKalam(searchKeyword: String): PagingSource<Int, Kalam>

    @Query(
        "SELECT * FROM kalam " +
            "WHERE (LOWER(title) LIKE :searchKeyword OR LOWER(location) LIKE :searchKeyword OR recorded_date LIKE :searchKeyword) AND favorite = 1 " +
            "ORDER BY id DESC",
    )
    fun getFavoritesKalam(searchKeyword: String): PagingSource<Int, Kalam>

    @Query(
        "SELECT * FROM kalam " +
            "WHERE (LOWER(title) LIKE :searchKeyword OR LOWER(location) LIKE :searchKeyword OR recorded_date LIKE :searchKeyword) AND playlist_id = :playlistId " +
            "ORDER BY id DESC",
    )
    fun getPlaylistKalam(playlistId: Int, searchKeyword: String): PagingSource<Int, Kalam>

    @Query("SELECT * FROM kalam WHERE playlist_id = :playlistId")
    fun getAllPlaylistKalam(playlistId: Int): LiveData<List<Kalam>>

    @RawQuery(observedEntities = [Kalam::class])
    fun getSingleKalam(query: SimpleSQLiteQuery): LiveData<Kalam?>

    @Insert suspend fun insert(kalam: Kalam)

    @Insert suspend fun insertAll(allKalams: List<Kalam>)

    @Query("SELECT COUNT(*) FROM kalam")
    fun countAll(): LiveData<Int>

    @Query("SELECT COUNT(*) FROM kalam WHERE offline_src != ''")
    fun countDownloads(): LiveData<Int>

    @Query("SELECT COUNT(*) FROM kalam WHERE favorite = 1")
    fun countFavorites(): LiveData<Int>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(kalam: Kalam)

    @Delete suspend fun delete(kalam: Kalam)
}
