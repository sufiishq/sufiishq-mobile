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

package pk.sufiishq.app.di.modules

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.mockk.spyk
import pk.sufiishq.app.db.SufiIshqDatabase
import pk.sufiishq.app.feature.kalam.data.dao.KalamDao
import pk.sufiishq.app.feature.playlist.data.dao.PlaylistDao
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DatabaseModule::class],
)
class TestDatabaseModule {

    @Provides
    fun provideAppDatabase(@ApplicationContext appContext: Context): SufiIshqDatabase {
        return Room.inMemoryDatabaseBuilder(appContext, SufiIshqDatabase::class.java)
            .allowMainThreadQueries().build()
    }

    @Provides
    @Singleton
    fun provideKalamDao(db: SufiIshqDatabase): KalamDao {
        return spyk(db.kalamDao())
    }

    @Provides
    @Singleton
    fun providePlaylistDao(db: SufiIshqDatabase): PlaylistDao {
        return spyk(db.playlistDao())
    }
}
