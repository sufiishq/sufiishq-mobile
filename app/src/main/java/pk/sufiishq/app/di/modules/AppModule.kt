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
import pk.sufiishq.app.helpers.KalamSplitManager
import pk.sufiishq.app.helpers.PreviewAudioPlayer

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
        previewAudioPlayer: PreviewAudioPlayer
    ): KalamSplitManager {
        return KalamSplitManager(appContext, previewAudioPlayer)
    }
}