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

package pk.sufiishq.app.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import pk.sufiishq.app.feature.kalam.data.dao.KalamDao
import pk.sufiishq.app.feature.kalam.model.Kalam
import pk.sufiishq.app.feature.playlist.data.dao.PlaylistDao
import pk.sufiishq.app.feature.playlist.model.Playlist

@Database(entities = [Kalam::class, Playlist::class], version = 1, exportSchema = false)
abstract class SufiIshqDatabase : RoomDatabase() {

    abstract fun kalamDao(): KalamDao
    abstract fun playlistDao(): PlaylistDao

    companion object {

        private var INSTANCE: SufiIshqDatabase? = null

        fun getInstance(context: Context): SufiIshqDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(context, SufiIshqDatabase::class.java, "sufiishq").build()
                }
            }

            return INSTANCE!!
        }
    }
}
