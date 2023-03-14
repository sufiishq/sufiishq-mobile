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

package pk.sufiishq.app.feature.player.di.module

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
import pk.sufiishq.app.BuildConfig
import pk.sufiishq.app.feature.kalam.splitter.PreviewAudioPlayer
import pk.sufiishq.app.feature.player.controller.AudioPlayer
import pk.sufiishq.app.feature.player.controller.SufiishqMediaPlayer
import pk.sufiishq.app.feature.player.di.qualifier.AndroidMediaPlayer
import timber.log.Timber
import javax.inject.Singleton

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
        return AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA).build()
    }

    @Provides
    fun provideAudioFocusRequest(
        audioAttributes: AudioAttributes,
    ): AudioFocusRequest.Builder? {
        BuildConfig.VERSION_CODE
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                    .setAudioAttributes(audioAttributes)
                    .setWillPauseWhenDucked(true)
            } catch (t: Throwable) {
                Timber.e(t)
                null
            }
        } else {
            null
        }
    }

    @Provides
    fun provideAudioManager(@ApplicationContext context: Context): AudioManager {
        return context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }
}
