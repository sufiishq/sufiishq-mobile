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

package pk.sufiishq.app.feature.personalize.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pk.sufiishq.app.feature.personalize.model.Personalize

@Dao
interface PersonalizeDao {

    @Query("SELECT * FROM personalize LIMIT 1")
    fun get(): LiveData<Personalize?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(personalize: Personalize)

    @Query("DELETE FROM personalize")
    suspend fun clear()
}
