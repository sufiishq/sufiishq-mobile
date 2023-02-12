package pk.sufiishq.app.di.modules

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Handler
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import pk.sufiishq.app.core.kalam.splitter.PreviewAudioPlayer
import pk.sufiishq.app.core.player.AudioPlayer
import pk.sufiishq.app.core.player.SufiishqMediaPlayer
import pk.sufiishq.app.di.qualifier.AndroidMediaPlayer

@Module
@InstallIn(SingletonComponent::class)
interface AndroidPlayerModule {

    @AndroidMediaPlayer
    @Singleton
    @Binds
    fun bindsAndroidMediaPlayer(sufiishqMediaPlayer: SufiishqMediaPlayer): AudioPlayer
}

@Module
@InstallIn(SingletonComponent::class)
class PlayerModule {

    @Provides
    fun providesPreviewAudioPlayer(handler: Handler): PreviewAudioPlayer {
        return PreviewAudioPlayer(handler, MediaPlayer())
    }

    @Provides
    fun provideAudioAttributes(): AudioAttributes {
        return AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .build()
    }

    @Provides
    fun provideAudioFocusRequest(
        audioAttributes: AudioAttributes
    ): AudioFocusRequest.Builder? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                .setAudioAttributes(audioAttributes)
                .setWillPauseWhenDucked(true)
        } else {
            null
        }
    }

    @Provides
    fun provideAudioManager(@ApplicationContext context: Context): AudioManager {
        return context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }
}