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

package pk.sufiishq.app.core.player.controller

import android.content.Context
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.PlaybackParams
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import pk.sufiishq.app.R
import pk.sufiishq.app.core.kalam.helper.TrackListType
import pk.sufiishq.app.core.kalam.model.Kalam
import pk.sufiishq.app.core.player.helper.AppMediaPlayer
import pk.sufiishq.app.core.player.listener.PlayerStateListener
import pk.sufiishq.app.core.player.state.MediaState
import pk.sufiishq.app.utils.getString
import timber.log.Timber
import javax.inject.Inject

class SufiishqMediaPlayer
@Inject
constructor(
    @ApplicationContext private val appContext: Context,
    private val mediaPlayer: AppMediaPlayer,
    private val audioManager: AudioManager,
    private val audioFocusRequestBuilder: AudioFocusRequest.Builder?,
) :
    AudioPlayer,
    AppMediaPlayer.OnProgressChangeListener,
    MediaPlayer.OnCompletionListener,
    MediaPlayer.OnPreparedListener,
    MediaPlayer.OnErrorListener,
    AudioManager.OnAudioFocusChangeListener {

    private lateinit var activeKalam: Kalam
    private var listeners = mutableSetOf<PlayerStateListener>()
    private var activeState: MediaState? = null
    private var trackListType: TrackListType = TrackListType.All()
    private var audioFocusRequest: AudioFocusRequest? = null

    init {
        initPlayer()
    }

    private fun initPlayer() {
        mediaPlayer.stop()
        mediaPlayer.reset()
        mediaPlayer.setOnProgressChangeListener(this)
        mediaPlayer.setOnCompletionListener(this)
        mediaPlayer.setOnPreparedListener(this)
        mediaPlayer.setOnErrorListener(this)

        when {
            (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) ->
                audioManager.requestAudioFocus(
                    audioFocusRequestBuilder!!.setOnAudioFocusChangeListener(this).build().also {
                        audioFocusRequest = it
                    },
                )
            (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) -> {
                audioManager.requestAudioFocus(
                    this,
                    AudioManager.STREAM_MUSIC,
                    AudioManager.AUDIOFOCUS_GAIN,
                )
            }
        }
    }

    private fun changeState(state: MediaState) {
        activeState = state
        listeners.forEach { listener -> listener.onStateChange(activeState!!) }
    }

    private fun play() {
        mediaPlayer.prepareAsync()
        changeState(MediaState.Loading(activeKalam, trackListType))
    }

    private fun pause() {
        mediaPlayer.pause()
        changeState(
            MediaState.Pause(
                activeKalam,
                mediaPlayer.currentPosition,
                mediaPlayer.duration,
                trackListType,
            ),
        )
    }

    private fun resume() {
        mediaPlayer.start()
        changeState(
            MediaState.Resume(
                activeKalam,
                mediaPlayer.currentPosition,
                mediaPlayer.duration,
                trackListType,
            ),
        )
    }

    private fun rePlay() {
        setSource(activeKalam, getTrackListType())
        doPlayOrPause()
    }

    override fun getActiveTrack(): Kalam {
        return activeKalam
    }

    override fun setSource(source: Kalam, trackListType: TrackListType) {
        this.trackListType = trackListType
        mediaPlayer.stop()
        mediaPlayer.reset()
        activeKalam = source
        mediaPlayer.setDataSource(appContext, activeKalam)
        changeState(MediaState.Idle(activeKalam, trackListType))
    }

    override fun doPlayOrPause() {
        when (activeState) {
            is MediaState.Idle -> play()
            is MediaState.Playing -> pause()
            is MediaState.Pause -> resume()
            is MediaState.Complete,
            is MediaState.Error,
            -> rePlay()
            else -> Timber.w("why we get this state $activeState, please check something wrong happening")
        }
    }

    override fun isPlaying(): Boolean {
        return mediaPlayer.isPlaying
    }

    override fun seekTo(msec: Int) {
        mediaPlayer.seekTo(msec)
        changeState(MediaState.Loading(activeKalam, trackListType))
    }

    override fun release() {
        try {
            mediaPlayer.stop()
            mediaPlayer.reset()
            changeState(MediaState.Stop(activeKalam, trackListType))
            when {
                (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) ->
                    audioManager.abandonAudioFocusRequest(
                        audioFocusRequest!!,
                    )
                (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) -> audioManager.abandonAudioFocus(
                    this,
                )
            }
        } catch (ex: Exception) {
            Timber.e(ex)
        }
    }

    override fun getTrackListType(): TrackListType {
        return trackListType
    }

    override fun registerListener(listener: PlayerStateListener): Boolean {
        activeState?.let { listener.onStateChange(it) }
        return listeners.add(listener)
    }

    override fun onProgressChanged(progress: Int) {
        changeState(MediaState.Playing(activeKalam, progress, mediaPlayer.duration, trackListType))
    }

    override fun onCompletion(mp: MediaPlayer?) {
        changeState(MediaState.Complete(activeKalam, trackListType))
    }

    override fun onPrepared(mp: MediaPlayer?) {
        mediaPlayer.playbackParams = PlaybackParams().setSpeed(1f)
        resume()
    }

    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        Timber.e("get error from media player: what: $what, extra $extra")
        if (what != -38 && extra != 0) {
            changeState(
                MediaState.Error(
                    activeKalam,
                    what,
                    extra,
                    getString(R.string.label_something_went_wrong),
                    trackListType,
                ),
            )
        }
        return true
    }

    override fun onAudioFocusChange(focusChange: Int) {
        when (focusChange) {
            AudioManager.AUDIOFOCUS_LOSS -> {
                if (isPlaying()) pause()
            }
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                // Pause playback
                if (isPlaying()) {
                    pause()
                }
            }
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> {
                // Lower the volume, keep playing
                mediaPlayer.setVolume(0.2f, 0.2f)
            }
            AudioManager.AUDIOFOCUS_GAIN -> {
                // Your app has been granted audio focus again
                // Raise volume to normal, restart playback if necessary
                mediaPlayer.setVolume(1f, 1f)
            }
        }
    }
}
