package pk.sufiishq.app.di.modules

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.mockk.spyk
import javax.inject.Singleton
import pk.sufiishq.app.db.SufiIshqDatabase
import pk.sufiishq.app.feature.kalam.data.dao.KalamDao
import pk.sufiishq.app.feature.playlist.data.dao.PlaylistDao

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DatabaseModule::class]
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