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