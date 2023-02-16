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
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import pk.sufiishq.app.data.dao.KalamDao
import pk.sufiishq.app.data.dao.PlaylistDao
import pk.sufiishq.app.db.SufiIshqDatabase

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    fun provideAppDatabase(@ApplicationContext appContext: Context): SufiIshqDatabase {
        return SufiIshqDatabase.getInstance(appContext)
    }

    @Provides
    fun provideKalamDao(db: SufiIshqDatabase): KalamDao {
        return db.kalamDao()
    }

    @Provides
    fun providePlaylistDao(db: SufiIshqDatabase): PlaylistDao {
        return db.playlistDao()
    }
}
