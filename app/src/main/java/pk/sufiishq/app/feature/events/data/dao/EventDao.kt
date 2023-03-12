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

package pk.sufiishq.app.feature.events.data.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import pk.sufiishq.app.feature.events.model.Event

@Dao
interface EventDao {

    @Query("SELECT * FROM `events` ORDER BY date ASC")
    fun getAllWithPaging(): PagingSource<Int, Event>

    @Query("SELECT * FROM `events` ORDER BY date ASC")
    suspend fun getAll(): List<Event>

    @Query("SELECT * FROM `events` WHERE remainingDays >= 0 AND remainingDays <= 2")
    suspend fun getUpcomingEvents(): List<Event>

    @Query("SELECT * FROM `events` WHERE id = :id")
    suspend fun getEvent(id: Long): Event

    @Query("SELECT COUNT(*) FROM events")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAll(events: List<Event>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(event: Event)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateAll(event: List<Event>)
}
