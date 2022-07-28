@file:Suppress("unused")

package pk.sufiishq.app.services

import android.app.*
import android.content.Intent
import android.media.MediaPlayer
import android.os.*
import pk.sufiishq.app.R
import pk.sufiishq.app.activities.MainActivity
import pk.sufiishq.app.helpers.SufiishqMediaPlayer
import pk.sufiishq.app.models.Kalam
import pk.sufiishq.app.utils.canPlay
import timber.log.Timber

class AudioPlayerService : Service(), MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
    MediaPlayer.OnCompletionListener,
    PlayerController {

    private val player by lazy { SufiishqMediaPlayer() }
    private val binder by lazy { AudioPlayerBinder() }
    private val handler by lazy { Handler(Looper.getMainLooper()) }
    private var activeKalam: Kalam? = null
    private var listener: Listener? = null
    private var currentTrackLength = 0

    override fun onCreate() {
        super.onCreate()
        initMusicPlayer()
    }

    override fun onBind(p0: Intent?): IBinder {
        return binder
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable)
        player.stop()
        player.release()
    }

    override fun onPrepared(p0: MediaPlayer?) {

        listener?.onTrackLoaded()
        log("Track Loaded: id = ${activeKalam?.id}, title = ${activeKalam?.title}")

        player.seekTo(currentTrackLength)
        player.start()

        showNotification()
        handler.post(runnable)

        listener?.onPlayStart()
        log("Track Play Start")
    }

    override fun onError(mediaPlayer: MediaPlayer?, what: Int, extra: Int): Boolean {
        handler.removeCallbacks(runnable)
        currentTrackLength = 0
        listener?.onError(RuntimeException("Something went wrong"))
        log("Track Error: id = ${activeKalam?.id}, title = ${activeKalam?.title}, src = ${activeKalam?.onlineSource} | what $what - extra $extra")
        return true
    }

    override fun onCompletion(p0: MediaPlayer?) {
        handler.removeCallbacks(runnable)
        player.stop()
        currentTrackLength = 0
        listener?.onCompleted(activeKalam!!)
        log("Track Completed: id = ${activeKalam?.id}, title = ${activeKalam?.title}")
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

    override fun setActiveTrack(kalam: Kalam) {

        handler.removeCallbacks(runnable)
        this.activeKalam = kalam
        currentTrackLength = 0
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
            if (currentTrackLength > 0) {

                player.seekTo(currentTrackLength)
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
            currentTrackLength = player.currentPosition
            stopForeground(true)
            handler.removeCallbacks(runnable)
            listener?.onPause()
            log("Track Pause: id = ${activeKalam?.id}, title = ${activeKalam?.title}")
        }
    }

    override fun seekTo(value: Float) {
        activeKalam?.let {
            currentTrackLength = normalizePercentToLength(value)
            log("seekTo: value = $value, currentLength = $currentTrackLength")
            handler.removeCallbacks(runnable)
            newPlay(it)
        }
    }

    override fun getCurrentProgress(): Float {
        return if (currentTrackLength > 0) {
            normalizeLengthToPercent()
        } else {
            0f
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
        val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val notificationChannel =
                NotificationChannel(CHANNEL_ID, "Sufi Ishq", NotificationManager.IMPORTANCE_MIN)
            notificationChannel.enableLights(false)
            notificationChannel.setShowBadge(false)
            notificationChannel.lockscreenVisibility = Notification.VISIBILITY_SECRET
            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(notificationChannel)

            Notification.Builder(this, CHANNEL_ID)
        } else {
            Notification.Builder(this)
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        builder.setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_start_logo)
            .setTicker(activeKalam?.title)
            .setOngoing(true)
            .setContentTitle(activeKalam?.title)
            .setContentText("${activeKalam?.location} ${activeKalam?.year}")

        startForeground(NOTIFY_ID, builder.build())
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

    private fun log(message: String) {
        Timber.v(message)
    }

    private val runnable = object : Runnable {
        override fun run() {
            listener?.onProgressChanged(normalizeLengthToPercent())
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

        fun onTrackLoaded() { /* optional */
        }

        fun onPlayStart() { /* optional */
        }

        fun onPause() { /* optional */
        }

        fun onResume() { /* optional */
        }

        fun onProgressChanged(progress: Float) { /* optional */
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
    fun setActiveTrack(kalam: Kalam)
    fun isPlaying(): Boolean
    fun doPlay()
    fun doPause()
    fun seekTo(value: Float)
    fun getCurrentProgress(): Float
}