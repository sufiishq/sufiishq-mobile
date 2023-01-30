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
import javax.inject.Singleton
import org.json.JSONObject
import pk.sufiishq.app.R
import pk.sufiishq.app.core.player.AudioPlayer
import pk.sufiishq.app.core.player.SufiishqMediaPlayer
import pk.sufiishq.app.core.player.helper.AppMediaPlayer
import pk.sufiishq.app.core.splitter.PreviewAudioPlayer
import pk.sufiishq.app.core.storage.KeyValueStorage
import pk.sufiishq.app.core.storage.SecureSharedPreferencesStorage
import pk.sufiishq.app.di.qualifier.AndroidMediaPlayer
import pk.sufiishq.app.di.qualifier.HelpJson
import pk.sufiishq.app.di.qualifier.SecureSharedPreferences
import pk.sufiishq.app.models.NavigationItem

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
    @HelpJson
    fun helpContentJson(@ApplicationContext context: Context): JSONObject {
        return JSONObject(
            context
                .assets
                .open("help/help.json")
                .bufferedReader()
                .use { it.readText() }
        )
    }

    @Singleton
    @Provides
    fun provideMainNavigation(): List<NavigationItem> {
        return listOf(
            NavigationItem("Qibla Direction", R.drawable.compass, ""),
            NavigationItem("Tasbee-Sarkar", R.drawable.tasbih, ""),
            NavigationItem("Events", R.drawable.events, ""),
            NavigationItem("Gallery", R.drawable.gallery, ""),
            NavigationItem("Niaz", R.drawable.niaz, ""),
            NavigationItem("Remedy", R.drawable.remedy, ""),
            NavigationItem("Rubai", R.drawable.rubai, ""),
            NavigationItem("Location", R.drawable.location, ""),
            NavigationItem("Shijra", R.drawable.shijra, ""),
        )
    }
}