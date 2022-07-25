package pk.sufiishq.app.helpers

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper

class PreviewAudioPlayer : MediaPlayer() {

    private var onProgressChange: (progress: Int) -> Unit = {}
    private val handler = Handler(Looper.getMainLooper())
    private val runnable = object : Runnable {
        override fun run() {
            onProgressChange(currentPosition)
            handler.postDelayed(this, 1000)
        }
    }

    override fun start() {
        super.start()
        handler.removeCallbacks(runnable)
        handler.postDelayed(runnable, 1000)
    }

    override fun pause() {
        super.pause()
        handler.removeCallbacks(runnable)
    }

    fun getDuration(path: String): Int {
        reset()
        setDataSource(path)
        prepare()
        return duration
    }

    override fun stop() {
        super.stop()
        handler.removeCallbacks(runnable)
    }

    fun setOnProgressListener(onProgressChange: (progress: Int) -> Unit) {
        this.onProgressChange = onProgressChange
    }

    fun releaseProgressListener() {
        handler.removeCallbacks(runnable)
    }
}