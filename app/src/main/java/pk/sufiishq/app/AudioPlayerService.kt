package pk.sufiishq.app

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import java.lang.Exception
import android.app.NotificationManager

import android.app.NotificationChannel
import android.os.*
import java.lang.RuntimeException


class AudioPlayerService: Service(), MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener, PlayerController {

    private val player by lazy { MediaPlayer() }
    private val binder by lazy { AudioPlayerBinder() }
    private val handler by lazy { Handler(Looper.getMainLooper()) }
    private var activeTrack: Track? = null
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
        log("Track Loaded: id = ${activeTrack?.id}, title = ${activeTrack?.title}")

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
        log("Track Error: id = ${activeTrack?.id}, title = ${activeTrack?.title}, src = ${activeTrack?.src} | what $what - extra $extra")
        return true
    }

    override fun onCompletion(p0: MediaPlayer?) {
        handler.removeCallbacks(runnable)
        player.stop()
        currentTrackLength = 0
        listener?.onCompleted(activeTrack!!)
        log("Track Completed: id = ${activeTrack?.id}, title = ${activeTrack?.title}")
    }

    // ============================================
    // Controller Listener
    // ============================================

    override fun setPlayerListener(listener: Listener?) {
        this.listener = listener
        listener?.initService(activeTrack!!)
        log("Listener Initialize")
    }

    override fun getActiveTrack() : Track? {
        return activeTrack
    }

    override fun setActiveTrack(track: Track) {
        handler.removeCallbacks(runnable)
        this.activeTrack = track
        currentTrackLength = 0
        listener?.onTrackUpdated(track)
        log("Track Updated: id = ${track.id}, title = ${track.title}")
    }

    override fun isPlaying(): Boolean {
        return player.isPlaying
    }

    override fun doPlay() {

        if (!isNetworkAvailable()) {
            listener?.onError(RuntimeException("Network not available"))
            log("Track Error: Network not available")
            return
        }

        activeTrack?.let {

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
                currentTrackLength = it.startFrom * 1000
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
            log("Track Pause: id = ${activeTrack?.id}, title = ${activeTrack?.title}")
        }
    }

    override fun seekTo(value: Float) {
        activeTrack?.let {
            currentTrackLength = normalizePercentToLength(value)
            log("seekTo: value = $value, currentLength = $currentTrackLength")
            handler.removeCallbacks(runnable)
            newPlay(it)
        }
    }

    override fun getCurrentProgress(): Float {
        return if(currentTrackLength > 0) {
            normalizeLengthToPercent()
        }
        else {
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

    private fun newPlay(track: Track) {
        handler.removeCallbacks(runnable)
        player.stop()
        player.reset()
        player.setDataSource(this, Uri.parse(track.src))
        player.prepareAsync()

        listener?.onTrackLoading()
        log("Track Loading: id = ${track.id}, title = ${track.title}")
    }

    private fun showNotification() {
        val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val notificationChannel = NotificationChannel(CHANNEL_ID, "Sufi Ishq", NotificationManager.IMPORTANCE_MIN)
            notificationChannel.enableLights(false)
            notificationChannel.setShowBadge(false)
            notificationChannel.lockscreenVisibility = Notification.VISIBILITY_SECRET
            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(notificationChannel)

            Notification.Builder(this, CHANNEL_ID)
        } else {
            Notification.Builder(this)
        }

        val pendingIntent = PendingIntent.getActivity(this, 0, Intent(this, MainActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT)
        builder.setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_stat_logo)
            .setTicker(activeTrack?.title)
            .setOngoing(true)
            .setContentTitle(activeTrack?.title)
            .setContentText("${activeTrack?.location} ${activeTrack?.year}")

        startForeground(NOTIFY_ID, builder.build())
    }

    private fun normalizeLengthToPercent(): Float {
        return if (player.duration > 0) {
            player.currentPosition.toFloat() / player.duration.toFloat() * 100f
        }
        else {
            0f
        }
    }

    private fun normalizePercentToLength(value: Float): Int {
        return (value / 100f * player.duration.toFloat()).toInt()
    }

    private fun log(message: String) {
        Log.d("si->", message)
    }

    private val runnable = object: Runnable {
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

    inner class AudioPlayerBinder: Binder() {
        fun getService(): PlayerController {
            return this@AudioPlayerService
        }
    }

    interface Listener {
        fun initService(track: Track) {}
        fun onTrackUpdated(track: Track) {}
        fun onTrackLoading() {}
        fun onTrackLoaded() {}
        fun onPlayStart() {}
        fun onPause() {}
        fun onResume() {}
        fun onProgressChanged(progress: Float) {}
        fun onCompleted(track: Track) {}
        fun onStopped() {}
        fun onError(ex: Exception) {}
    }
}

interface PlayerController {
    fun setPlayerListener(listener: AudioPlayerService.Listener?)
    fun getActiveTrack(): Track?
    fun setActiveTrack(track: Track)
    fun isPlaying(): Boolean
    fun doPlay()
    fun doPause()
    fun seekTo(value: Float)
    fun getCurrentProgress(): Float
}