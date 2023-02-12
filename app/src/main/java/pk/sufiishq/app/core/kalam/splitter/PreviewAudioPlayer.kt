package pk.sufiishq.app.core.kalam.splitter

import android.media.MediaPlayer
import android.os.Handler
import javax.inject.Inject

class PreviewAudioPlayer @Inject constructor(
    private val handler: Handler,
    private val sourcePlayer: MediaPlayer
) {

    private var onProgressChange: (progress: Int) -> Unit = {}

    private val runnable = object : Runnable {
        override fun run() {
            getProgressListener().invoke(sourcePlayer.currentPosition)
            handler.postDelayed(this, UPDATE_DELAY)
        }
    }

    fun start() {
        sourcePlayer.start()
        handler.removeCallbacks(runnable)
        handler.postDelayed(runnable, UPDATE_DELAY)
    }

    fun pause() {
        sourcePlayer.pause()
        handler.removeCallbacks(runnable)
    }

    fun getDuration(path: String): Int {
        sourcePlayer.reset()
        sourcePlayer.setDataSource(path)
        sourcePlayer.prepare()
        return sourcePlayer.duration
    }

    fun stop() {
        sourcePlayer.stop()
        handler.removeCallbacks(runnable)
    }

    fun setOnProgressListener(onProgressChange: (progress: Int) -> Unit) {
        this.onProgressChange = onProgressChange
    }

    fun getProgressListener(): (progress: Int) -> Unit {
        return onProgressChange
    }

    fun releaseProgressListener() {
        handler.removeCallbacks(runnable)
    }

    fun isPlaying(): Boolean {
        return sourcePlayer.isPlaying
    }

    fun seekTo(msec: Int) {
        sourcePlayer.seekTo(msec)
    }

    fun setOnCompletionListener(listener: MediaPlayer.OnCompletionListener) {
        sourcePlayer.setOnCompletionListener(listener)
    }

    companion object {
        private const val UPDATE_DELAY = 1000L
    }
}