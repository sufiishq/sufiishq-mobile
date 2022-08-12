package pk.sufiishq.app.di.modules

import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import pk.sufiishq.app.configs.AppConfig
import pk.sufiishq.app.core.player.AudioPlayer
import pk.sufiishq.app.core.player.SufiishqMediaPlayer
import pk.sufiishq.app.core.player.helpers.AppMediaPlayer
import pk.sufiishq.app.core.storage.KeyValueStorage
import pk.sufiishq.app.core.storage.SecureSharedPreferencesStorage
import pk.sufiishq.app.di.qualifier.AndroidMediaPlayer
import pk.sufiishq.app.di.qualifier.SecureSharedPreferences
import pk.sufiishq.app.helpers.KalamSplitManager
import pk.sufiishq.app.helpers.PreviewAudioPlayer
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun providesHandler(): Handler {
        return Handler(Looper.getMainLooper())
    }

    @Provides
    fun providesPreviewAudioPlayer(handler: Handler): PreviewAudioPlayer {
        return PreviewAudioPlayer(handler, MediaPlayer())
    }

    @Provides
    fun providesKalamSplitManager(
        @ApplicationContext appContext: Context,
        previewAudioPlayer: PreviewAudioPlayer,
        @AndroidMediaPlayer player: AudioPlayer
    ): KalamSplitManager {
        return KalamSplitManager(appContext, previewAudioPlayer, player)
    }

    @Provides
    @Singleton
    @SecureSharedPreferences
    fun providesKeyValueStorage(@ApplicationContext appContext: Context): KeyValueStorage {
        return SecureSharedPreferencesStorage(appContext)
    }

    @Provides
    @Singleton
    @AndroidMediaPlayer
    fun providesAndroidMediaPlayer(
        @ApplicationContext appContext: Context,
        appPlayer: AppMediaPlayer
    ): AudioPlayer {
        return SufiishqMediaPlayer(appContext, appPlayer)
    }

    @Provides
    @Singleton
    fun providesAppConfig(): AppConfig {
        return AppConfig()
    }
}