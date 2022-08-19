package pk.sufiishq.app.core.player

import android.content.Context
import android.media.*
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import pk.sufiishq.app.core.player.helper.AppMediaPlayer
import pk.sufiishq.app.core.player.listener.PlayerStateListener
import pk.sufiishq.app.core.player.state.MediaState
import pk.sufiishq.app.di.qualifier.AndroidMediaPlayer
import pk.sufiishq.app.helpers.TrackListType
import pk.sufiishq.app.models.Kalam
import pk.sufiishq.app.utils.toast
import timber.log.Timber
import javax.inject.Inject


@AndroidMediaPlayer
class SufiishqMediaPlayer @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val mediaPlayer: AppMediaPlayer
) : AudioPlayer, AppMediaPlayer.OnProgressChangeListener, MediaPlayer.OnCompletionListener,
    MediaPlayer.OnPreparedListener,
    MediaPlayer.OnErrorListener,
    AudioManager.OnAudioFocusChangeListener {

    private var activeKalam: Kalam? = null
    private var listeners = mutableSetOf<PlayerStateListener>()
    private var activeState: MediaState? = null
    private var trackListType: TrackListType = TrackListType.All()

    private val audioManager: AudioManager by lazy { appContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager }
    private val audioAttributes: AudioAttributes by lazy {
        AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .build()
    }
    private val audioFocusRequest: AudioFocusRequest? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                .setAudioAttributes(audioAttributes)
                .setWillPauseWhenDucked(true)
                .setOnAudioFocusChangeListener(this).build()
        } else {
            null
        }
    }

    init {
        mediaPlayer.stop()
        mediaPlayer.reset()
        mediaPlayer.setOnProgressChangeListener(this)
        mediaPlayer.setOnCompletionListener(this)
        mediaPlayer.setOnPreparedListener(this)
        mediaPlayer.setOnErrorListener(this)

        when {
            (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) -> audioManager.requestAudioFocus(
                audioFocusRequest!!
            )
            (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) -> {
                audioManager.requestAudioFocus(
                    this,
                    AudioManager.STREAM_MUSIC,
                    AudioManager.AUDIOFOCUS_GAIN
                )
            }
        }
    }

    private fun changeState(state: MediaState) {
        activeState = state
        listeners.forEach { listener ->
            listener.onStateChange(activeState!!)
        }
    }

    private fun play() {
        mediaPlayer.prepareAsync()
        changeState(MediaState.Loading(activeKalam!!))
    }

    private fun pause() {
        mediaPlayer.pause()
        changeState(
            MediaState.Pause(
                activeKalam!!,
                mediaPlayer.currentPosition,
                mediaPlayer.duration
            )
        )
    }

    private fun resume() {
        mediaPlayer.start()
        changeState(
            MediaState.Resume(
                activeKalam!!,
                mediaPlayer.currentPosition,
                mediaPlayer.duration
            )
        )
    }

    private fun rePlay() {
        setSource(activeKalam!!, getTrackListType())
        doPlayOrPause()
    }

    override fun getActiveTrack(): Kalam {
        return activeKalam!!
    }

    override fun setSource(source: Kalam, trackListType: TrackListType) {
        this.trackListType = trackListType
        mediaPlayer.stop()
        mediaPlayer.reset()
        activeKalam = source
        changeState(MediaState.Idle(activeKalam!!))
        mediaPlayer.setDataSource(appContext, activeKalam!!)
    }

    override fun doPlayOrPause() {
        if (activeKalam == null) {
            appContext.toast("source is not set, please set kalam using setSource(source: Kalam) method")
            return
        }

        when (activeState) {
            is MediaState.Idle -> play()
            is MediaState.Playing -> pause()
            is MediaState.Pause -> resume()
            is MediaState.Complete, is MediaState.Error -> rePlay()
            else -> Timber.w("why we get this state $activeState, please check something wrong happening")
        }
    }

    override fun isPlaying(): Boolean {
        return mediaPlayer.isPlaying
    }

    override fun seekTo(msec: Int) {
        mediaPlayer.seekTo(msec)
        changeState(MediaState.Loading(activeKalam!!))
    }

    override fun release() {
        mediaPlayer.stop()
        mediaPlayer.reset()
        changeState(MediaState.Stop(activeKalam!!))
        when {
            (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) -> audioManager.abandonAudioFocusRequest(
                audioFocusRequest!!
            )
            (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) -> audioManager.abandonAudioFocus(this)
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
        changeState(MediaState.Playing(activeKalam!!, progress, mediaPlayer.duration))
    }

    override fun onCompletion(mp: MediaPlayer?) {
        changeState(MediaState.Complete(activeKalam!!))
    }

    override fun onPrepared(mp: MediaPlayer?) {
        changeState(MediaState.Prepared(activeKalam!!))
        mediaPlayer.playbackParams = PlaybackParams().setSpeed(1f)
        mediaPlayer.start()
    }

    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        changeState(MediaState.Error(activeKalam!!, what, extra, "something went wrong"))
        return true
    }

    override fun onAudioFocusChange(focusChange: Int) {
        when (focusChange) {
            AudioManager.AUDIOFOCUS_LOSS -> {
                setSource(activeKalam!!, getTrackListType())
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