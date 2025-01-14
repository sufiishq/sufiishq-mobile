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
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec
import androidx.sqlite.db.SupportSQLiteDatabase
import pk.sufiishq.app.feature.app.data.dao.MediaDao
import pk.sufiishq.app.feature.app.model.Media
import pk.sufiishq.app.feature.events.data.dao.EventDao
import pk.sufiishq.app.feature.events.model.Event
import pk.sufiishq.app.feature.kalam.data.dao.KalamDao
import pk.sufiishq.app.feature.kalam.model.Kalam
import pk.sufiishq.app.feature.occasions.data.dao.OccasionDao
import pk.sufiishq.app.feature.occasions.model.Occasion
import pk.sufiishq.app.feature.personalize.data.dao.PersonalizeDao
import pk.sufiishq.app.feature.personalize.model.Personalize
import pk.sufiishq.app.feature.playlist.data.dao.PlaylistDao
import pk.sufiishq.app.feature.playlist.model.Playlist

@Database(
    entities = [Kalam::class, Playlist::class, Occasion::class, Media::class, Event::class, Personalize::class],
    version = 4,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(
            from = 3,
            to = 4,
            spec = SufiIshqDatabase.AutoMigration::class
        ),
    ],
)
abstract class SufiIshqDatabase : RoomDatabase() {

    abstract fun kalamDao(): KalamDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun occasionDao(): OccasionDao
    abstract fun mediaDao(): MediaDao
    abstract fun eventDao(): EventDao
    abstract fun personalizeDao(): PersonalizeDao

    companion object {

        private var INSTANCE: SufiIshqDatabase? = null

        fun getInstance(context: Context): SufiIshqDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE =
                        Room.databaseBuilder(context, SufiIshqDatabase::class.java, "sufiishq")
                            .build()
                }
            }

            return INSTANCE!!
        }
    }

    class AutoMigration : AutoMigrationSpec {
        override fun onPostMigrate(db: SupportSQLiteDatabase) {
            super.onPostMigrate(db)
            db.execSQL("UPDATE kalam SET online_src = REPLACE(online_src, 'https://sufiishq.pk/', 'https://filedn.com/lNVSC44OvOKYw0PH6GJfvPQ/')")
        }
    }
}
