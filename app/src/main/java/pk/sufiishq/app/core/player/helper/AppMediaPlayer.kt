package pk.sufiishq.app.core.player.helper

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.Handler
import pk.sufiishq.app.models.Kalam
import pk.sufiishq.app.utils.hasOfflineSource
import java.io.File
import javax.inject.Inject

class AppMediaPlayer @Inject constructor(
    private val handler: Handler
) : MediaPlayer() {

    private var onProgressChangeListener: OnProgressChangeListener? = null

    private val runnable = object : Runnable {
        override fun run() {
            getOnProgressChangeListener()?.onProgressChanged(currentPosition)
            handler.postDelayed(this, UPDATE_DELAY)
        }
    }

    fun setDataSource(context: Context, kalam: Kalam) {
        if (kalam.hasOfflineSource()) {
            setDataSource(context.filesDir.absolutePath + File.separator + kalam.offlineSource)
        } else {
            setDataSource(context, Uri.parse(kalam.onlineSource))
        }
    }

    override fun start() {
        super.start()
        handler.removeCallbacks(runnable)
        handler.postDelayed(runnable, UPDATE_DELAY)
    }

    override fun pause() {
        super.pause()
        handler.removeCallbacks(runnable)
    }

    override fun stop() {
        super.stop()
        handler.removeCallbacks(runnable)
    }

    fun setOnProgressChangeListener(onProgressChangeListener: OnProgressChangeListener) {
        this.onProgressChangeListener = onProgressChangeListener
    }

    fun getOnProgressChangeListener(): OnProgressChangeListener? {
        return onProgressChangeListener
    }

    override fun setOnCompletionListener(listener: OnCompletionListener?) {
        super.setOnCompletionListener {
            handler.removeCallbacks(runnable)
            listener?.onCompletion(it)
        }
    }

    interface OnProgressChangeListener {
        fun onProgressChanged(progress: Int)
    }

    companion object {
        const val UPDATE_DELAY = 1000L
    }
}