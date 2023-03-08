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

package pk.sufiishq.app.feature.occasions.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import pk.sufiishq.app.feature.occasions.OccasionType
import pk.sufiishq.app.feature.occasions.data.repository.OccasionPagingSource
import pk.sufiishq.app.feature.occasions.model.Occasion

@Dao
interface OccasionDao {

    @Transaction
    @Query(
        "SELECT * FROM occasion " +
            "WHERE (title LIKE :searchKeyword OR startTimestamp LIKE :searchKeyword OR endTimestamp LIKE :searchKeyword OR hijriDate LIKE :searchKeyword) AND type=:type " +
            "ORDER BY updatedAt DESC",
    )
    fun getAll(
        type: OccasionType,
        searchKeyword: String,
    ): OccasionPagingSource

    @Query("SELECT COUNT(*) AS totalRecords FROM occasion")
    suspend fun getCount(): Int

    @Query("SELECT MAX(updatedAt) AS highestTimestamp FROM occasion")
    suspend fun getHighestTimestamp(): Long?

    @Query("SELECT MIN(updatedAt) AS lowestTimestamp FROM occasion")
    suspend fun getLowestTimestamp(): Long?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(occasion: Occasion)
}
