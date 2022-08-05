@file:Suppress("unused")

package pk.sufiishq.app.services

import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.lifecycle.LifecycleService
import dagger.hilt.android.AndroidEntryPoint
import pk.sufiishq.app.data.repository.KalamRepository
import pk.sufiishq.app.di.qualifier.SecureSharedPreferences
import pk.sufiishq.app.helpers.PlayerNotification
import pk.sufiishq.app.helpers.Screen
import pk.sufiishq.app.helpers.SufiishqMediaPlayer
import pk.sufiishq.app.models.Kalam
import pk.sufiishq.app.storage.KeyValueStorage
import pk.sufiishq.app.utils.IS_SHUFFLE_ON
import pk.sufiishq.app.utils.canPlay
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class AudioPlayerService : LifecycleService(), MediaPlayer.OnPreparedListener,
    MediaPlayer.OnErrorListener,
    MediaPlayer.OnCompletionListener,
    PlayerController {

    private val player by lazy { SufiishqMediaPlayer() }
    private val binder by lazy { AudioPlayerBinder() }
    private val handler by lazy { Handler(Looper.getMainLooper()) }
    private val playerNotification by lazy { PlayerNotification(this) }
    private var activeKalam: Kalam? = null
    private var listener: Listener? = null
    private var currentTrackPosition = 0
    private var trackType = Screen.Tracks.ALL
    private var playlistId = 0

    @Inject
    lateinit var kalamRepository: KalamRepository

    @Inject
    @SecureSharedPreferences
    lateinit var keyValueStorage: KeyValueStorage

    override fun onCreate() {
        super.onCreate()
        initMusicPlayer()
    }

    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)
        return binder
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable)
        player.stop()
        player.release()
    }

    override fun onPrepared(p0: MediaPlayer?) {

        listener?.onTrackLoaded(player.duration)
        log("Track Loaded: id = ${activeKalam?.id}, title = ${activeKalam?.title}")

        player.seekTo(currentTrackPosition)
        player.start()

        showNotification()
        handler.post(runnable)

        listener?.onPlayStart()
        log("Track Play Start")
    }

    override fun onError(mediaPlayer: MediaPlayer?, what: Int, extra: Int): Boolean {
        handler.removeCallbacks(runnable)
        currentTrackPosition = 0
        listener?.onError(RuntimeException("Something went wrong"))
        log("Track Error: id = ${activeKalam?.id}, title = ${activeKalam?.title}, src = ${activeKalam?.onlineSource} | what $what - extra $extra")
        return true
    }

    override fun onCompletion(p0: MediaPlayer?) {
        kalamRepository.getNextKalam(getActiveTrack()!!.id, trackType, playlistId, false)
            .observe(this) { nextKalam ->
                if (nextKalam == null) {
                    allTrackCompleted()
                    return@observe
                }

                if (nextKalam.canPlay(this)) {
                    setActiveTrack(nextKalam, trackType, playlistId)
                    doPlay()
                }
            }
    }

    // ============================================
    // Controller Listener
    // ============================================

    override fun setPlayerListener(listener: Listener?) {
        this.listener = listener
        listener?.initService(activeKalam!!)
        log("Listener Initialize")
    }

    override fun getActiveTrack(): Kalam? {
        return activeKalam
    }

    override fun setActiveTrack(kalam: Kalam, trackType: String, playlistId: Int) {
        this.trackType = trackType
        this.playlistId = playlistId

        handler.removeCallbacks(runnable)
        this.activeKalam = kalam
        currentTrackPosition = 0
        listener?.onTrackUpdated(kalam)
        log("Track Updated: id = ${kalam.id}, title = ${kalam.title}")
    }

    override fun isPlaying(): Boolean {
        return player.isPlaying
    }

    override fun doPlay() {

        if (!getActiveTrack().canPlay(this)) return

        activeKalam?.let {

            // resume current track
            if (currentTrackPosition > 0) {

                player.seekTo(currentTrackPosition)
                player.start()

                showNotification()
                handler.post(runnable)
                listener?.onResume()
                log("Resume Track: id = ${it.id}, title = ${it.title}")
            }

            // start new track
            else {
                newPlay(it)
            }
        }
    }

    override fun doPause() {
        if (isPlaying()) {
            player.pause()
            currentTrackPosition = player.currentPosition
            stopForeground(true)
            handler.removeCallbacks(runnable)
            listener?.onPause()
            log("Track Pause: id = ${activeKalam?.id}, title = ${activeKalam?.title}")
        }
    }

    override fun seekTo(value: Float) {
        activeKalam?.let {
            currentTrackPosition = normalizePercentToLength(value)
            log("seekTo: value = $value, currentLength = $currentTrackPosition")
            handler.removeCallbacks(runnable)
            newPlay(it)
        }
    }

    override fun getCurrentProgress(): Float {
        return if (currentTrackPosition > 0) {
            normalizeLengthToPercent()
        } else {
            0f
        }
    }

    override fun playNext() {
        getActiveTrack()?.let { kalam ->
            kalamRepository.getNextKalam(kalam.id, trackType, playlistId, keyValueStorage.get(IS_SHUFFLE_ON, false))
                .observe(this) { nextKalam ->
                    playNextOrPrevious(nextKalam)
                }
        }
    }

    override fun playPrevious() {
        getActiveTrack()?.let { kalam ->
            kalamRepository.getPreviousKalam(kalam.id, trackType, playlistId, keyValueStorage.get(IS_SHUFFLE_ON, false))
                .observe(this) { previousKalam ->
                    playNextOrPrevious(previousKalam)
                }
        }
    }

    // ============================================
    // PRIVATE METHODS
    // ============================================

    private fun initMusicPlayer() {
        player.setOnPreparedListener(this)
        player.setOnErrorListener(this)
        player.setOnCompletionListener(this)
    }

    private fun newPlay(kalam: Kalam) {
        handler.removeCallbacks(runnable)
        player.stop()
        player.reset()
        player.setDataSource(this, kalam)
        player.prepareAsync()

        listener?.onTrackLoading()
        log("Track Loading: id = ${kalam.id}, title = ${kalam.title}")
    }

    private fun showNotification() {
        playerNotification.buildNotification(activeKalam, this)
    }

    private fun normalizeLengthToPercent(): Float {
        return if (player.duration > 0) {
            player.currentPosition.toFloat() / player.duration.toFloat() * 100f
        } else {
            0f
        }
    }

    private fun normalizePercentToLength(value: Float): Int {
        return (value / 100f * player.duration.toFloat()).toInt()
    }

    private fun playNextOrPrevious(kalam: Kalam?) {
        kalam?.let {
            if (kalam.canPlay(this)) {
                setActiveTrack(kalam, trackType, playlistId)
                if (isPlaying()) {
                    doPlay()
                }
            }
        }
    }

    private fun allTrackCompleted() {
        handler.removeCallbacks(runnable)
        player.stop()
        currentTrackPosition = 0
        listener?.onCompleted(activeKalam!!)
        log("Track Completed: id = ${activeKalam?.id}, title = ${activeKalam?.title}")
    }

    private fun log(message: String) {
        Timber.v(message)
    }

    private val runnable = object : Runnable {
        override fun run() {
            listener?.onProgressChanged(player.currentPosition, normalizeLengthToPercent())
            log("On Progress Changed: ${normalizeLengthToPercent()}")
            handler.postDelayed(this, 1000)
        }
    }

    companion object {
        const val NOTIFY_ID = 1
        const val CHANNEL_ID = "SufiIshq"
    }

    inner class AudioPlayerBinder : Binder() {
        fun getService(): PlayerController {
            return this@AudioPlayerService
        }
    }

    interface Listener {
        fun initService(kalam: Kalam) { /* optional */
        }

        fun onTrackUpdated(kalam: Kalam) { /* optional */
        }

        fun onTrackLoading() { /* optional */
        }

        fun onTrackLoaded(totalDuration: Int) { /* optional */
        }

        fun onPlayStart() { /* optional */
        }

        fun onPause() { /* optional */
        }

        fun onResume() { /* optional */
        }

        fun onProgressChanged(currentPosition: Int, progress: Float) { /* optional */
        }

        fun onCompleted(kalam: Kalam) { /* optional */
        }

        fun onStopped() { /* optional */
        }

        fun onError(ex: Exception) { /* optional */
        }
    }
}

interface PlayerController {
    fun setPlayerListener(listener: AudioPlayerService.Listener?)
    fun getActiveTrack(): Kalam?
    fun setActiveTrack(kalam: Kalam, trackType: String, playlistId: Int)
    fun isPlaying(): Boolean
    fun doPlay()
    fun doPause()
    fun seekTo(value: Float)
    fun getCurrentProgress(): Float
    fun playNext()
    fun playPrevious()
}