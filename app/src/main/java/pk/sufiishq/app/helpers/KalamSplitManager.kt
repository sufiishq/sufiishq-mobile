package pk.sufiishq.app.helpers

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.qualifiers.ApplicationContext
import pk.sufiishq.app.core.event.dispatcher.EventDispatcher
import pk.sufiishq.app.core.event.events.Event
import pk.sufiishq.app.core.event.events.KalamSplitManagerEvents
import pk.sufiishq.app.core.event.exception.UnhandledEventException
import pk.sufiishq.app.core.event.handler.EventHandler
import pk.sufiishq.app.core.player.AudioPlayer
import pk.sufiishq.app.di.qualifier.AndroidMediaPlayer
import pk.sufiishq.app.models.Kalam
import pk.sufiishq.app.utils.*
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KalamSplitManager @Inject constructor(
    @ApplicationContext val appContext: Context,
    private val previewAudioPlayer: PreviewAudioPlayer,
    @AndroidMediaPlayer private val player: AudioPlayer
) : EventHandler {

    private val splitStatus = MutableLiveData<SplitStatus>(SplitCompleted())

    private val splitStart = MutableLiveData(0)
    private val splitEnd = MutableLiveData(0)
    private val kalamLength = MutableLiveData(0)
    private val kalamPreviewLength = MutableLiveData(0)
    private val previewPlayStart = MutableLiveData(false)
    private val previewKalamProgress = MutableLiveData(0)
    private var kalam: Kalam? = null

    init {

        EventDispatcher.getInstance().registerEventHandler(this)

        previewAudioPlayer.setOnCompletionListener {
            previewAudioPlayer.releaseProgressListener()
            previewKalamProgress.value = 0
            previewPlayStart.value = false
        }

        previewAudioPlayer.setOnProgressListener {
            previewKalamProgress.value = it
        }
    }

    /*=======================================*/
    // PRIVATE METHODS
    /*=======================================*/

    private fun setKalam(kalam: Kalam) {
        reset()

        this.kalam = kalam
        val duration =
            previewAudioPlayer.getDuration(appContext.filesDir.absolutePath + "/" + kalam.offlineSource)
        setSplitEnd(duration)
        kalamLength.value = duration

    }

    private fun startPreview() {
        previewKalamProgress.value = 0
        setSplitStatus(SplitInProgress)
        kalam?.let {
            val sourceFile = File(appContext.filesDir.absolutePath + "/" + it.offlineSource)
            val outFile = File(appContext.cacheDir, CACHE_SPLIT_FILENAME)
            sourceFile.split(
                outFile,
                splitStart.optValue(0).formatTime,
                (splitEnd.optValue(0) - splitStart.optValue(0)).formatTime
            ) { returnCode ->

                if (returnCode == SPLIT_IN_PROGRESS) return@split

                if (returnCode == SPLIT_SUCCESS) {
                    val duration = previewAudioPlayer.getDuration(outFile.absolutePath)
                    kalamPreviewLength.postValue(duration)
                }

                setSplitStatus(SplitCompleted(returnCode))
            }
        }
    }

    private fun playPreview() {

        if (player.isPlaying()) player.doPlayOrPause()

        if (previewAudioPlayer.isPlaying()) {
            previewAudioPlayer.pause()
            previewPlayStart.value = false
        } else {
            previewAudioPlayer.start()
            previewPlayStart.value = true
        }
    }

    private fun setSplitStart(start: Int) {
        splitStart.value = start
    }

    private fun setSplitEnd(end: Int) {
        splitEnd.value = end
    }

    private fun setSplitStatus(status: SplitStatus) {
        if (previewAudioPlayer.isPlaying()) {
            previewAudioPlayer.pause()
            previewPlayStart.value = false
        }
        splitStatus.postValue(status)
    }

    private fun updateSeekbarValue(value: Float) {
        previewKalamProgress.value = value.toInt()
        previewAudioPlayer.seekTo(value.toInt())
    }

    private fun reset() {
        splitStatus.value = SplitCompleted()
        splitStart.value = 0
        splitEnd.value = 0

        kalamLength.value = 0
        kalamPreviewLength.value = 0
        previewPlayStart.value = false
        previewKalamProgress.value = 0
        kalam = null
    }

    /*=======================================*/
    // PUBLIC METHODS
    /*=======================================*/

    fun getKalam() = kalam!!
    fun getSplitFile() = File(appContext.cacheDir, CACHE_SPLIT_FILENAME)
    fun getSplitStatus(): LiveData<SplitStatus> = splitStatus
    fun getSplitStart(): LiveData<Int> = splitStart
    fun getSplitEnd(): LiveData<Int> = splitEnd
    fun getKalamLength(): LiveData<Int> = kalamLength
    fun getKalamPreviewLength(): LiveData<Int> = kalamPreviewLength
    fun getPreviewPlayStart(): LiveData<Boolean> = previewPlayStart
    fun getPreviewKalamProgress(): LiveData<Int> = previewKalamProgress

    /*=======================================*/
    // HANDLE KALAM SPLIT MANAGER EVENTS
    /*=======================================*/

    override fun onEvent(event: Event) {

        when (event) {
            is KalamSplitManagerEvents.SetKalam -> setKalam(event.kalam)
            is KalamSplitManagerEvents.StartPreview -> startPreview()
            is KalamSplitManagerEvents.PlayPreview -> playPreview()
            is KalamSplitManagerEvents.SetSplitStart -> setSplitStart(event.position)
            is KalamSplitManagerEvents.SetSplitEnd -> setSplitEnd(event.position)
            is KalamSplitManagerEvents.SetSplitStatus -> setSplitStatus(event.status)
            is KalamSplitManagerEvents.UpdateSeekbar -> updateSeekbarValue(event.value)
            else -> throw UnhandledEventException(event, this)
        }
    }
}