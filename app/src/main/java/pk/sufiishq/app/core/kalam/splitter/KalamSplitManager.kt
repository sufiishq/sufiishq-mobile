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

package pk.sufiishq.app.core.kalam.splitter

import android.content.Context
import android.media.MediaPlayer
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import pk.sufiishq.app.R
import pk.sufiishq.app.core.kalam.data.repository.KalamRepository
import pk.sufiishq.app.core.kalam.model.Kalam
import pk.sufiishq.app.core.player.controller.AudioPlayer
import pk.sufiishq.app.core.player.di.qualifier.AndroidMediaPlayer
import pk.sufiishq.app.di.qualifier.IoDispatcher
import pk.sufiishq.app.utils.CACHE_SPLIT_FILENAME
import pk.sufiishq.app.utils.KALAM_DIR
import pk.sufiishq.app.utils.appendPath
import pk.sufiishq.app.utils.formatTime
import pk.sufiishq.app.utils.moveTo
import pk.sufiishq.app.utils.quickToast
import pk.sufiishq.app.utils.split
import java.io.File
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@Suppress("UNUSED_PARAMETER")
class KalamSplitManager
@Inject
constructor(
    @ApplicationContext val appContext: Context,
    private val previewAudioPlayer: PreviewAudioPlayer,
    private val kalamRepository: KalamRepository,
    @AndroidMediaPlayer private val player: AudioPlayer,
    @IoDispatcher private val dispatcher: CoroutineContext,
) {

    private val splitKalamInfo = MutableLiveData<SplitKalamInfo?>(null)

    init {
        previewAudioPlayer.setOnCompletionListener(::onPlayerComplete)
        previewAudioPlayer.setOnProgressListener(::onProgressChange)
    }

    fun dismissKalamSplitDialog() {
        splitKalamInfo.value = null
    }

    fun showKalamSplitDialog(kalam: Kalam) {
        previewAudioPlayer
            .getDuration(appContext.filesDir.absolutePath + "/" + kalam.offlineSource)
            .let { duration ->
                SplitKalamInfo(
                    kalam = kalam,
                    splitEnd = duration,
                    kalamLength = duration,
                )
            }
            .apply { splitKalamInfo.postValue(this) }
    }

    fun startSplitting() {
        splitKalamInfo.value
            ?.copy(
                previewKalamLength = 0,
                splitStatus = SplitStatus.InProgress,
            )
            ?.also { splitKalamInfo.postValue(it) }
            ?.let {
                val destFile = getDestFile()
                val sourceFile = appContext.filesDir.appendPath(it.kalam.offlineSource)
                Pair(Pair(sourceFile, destFile), it)
            }
            ?.apply {
                val sourceFile = first.first
                val destFile = first.second

                val kalamInfo = second
                val splitStart = kalamInfo.splitStart
                val splitEnd = kalamInfo.splitEnd

                sourceFile.split(
                    destFile,
                    splitStart.formatTime,
                    (splitEnd - splitStart).formatTime,
                    ::splitCompleted,
                )
            }
    }

    fun playSplitKalamPreview() {
        pauseMainPlayer()

        splitKalamInfo.value
            ?.copy(
                previewPlayStart = !previewAudioPlayer.isPlaying(),
            )
            ?.apply {
                togglePlay()
                splitKalamInfo.postValue(this)
            }
    }

    fun setSplitStart(start: Int) {
        splitKalamInfo.value
            ?.copy(
                splitStart = start,
            )
            ?.apply { splitKalamInfo.value = this }
    }

    fun setSplitEnd(end: Int) {
        splitKalamInfo.value
            ?.copy(
                splitEnd = end,
            )
            ?.apply { splitKalamInfo.value = this }
    }

    fun setSplitStatus(status: SplitStatus) {
        pausePreviewPlayer()

        splitKalamInfo.value
            ?.copy(
                previewKalamProgress = 0,
                previewPlayStart = false,
                splitStatus = status,
            )
            ?.apply { splitKalamInfo.postValue(this) }
    }

    fun updateSplitSeekbarValue(value: Float) {
        splitKalamInfo.value
            ?.copy(
                previewKalamProgress = value.toInt(),
            )
            ?.apply {
                splitKalamInfo.postValue(this)
                previewAudioPlayer.seekTo(value.toInt())
            }
    }

    fun saveSplitKalam(sourceKalam: Kalam, kalamTitle: String) {
        CoroutineScope(dispatcher).launch {
            val fileName = buildString {
                append(KALAM_DIR)
                append(File.separator)
                append(kalamTitle.lowercase().replace(" ", "_"))
                append("_${Calendar.getInstance().timeInMillis}.mp3")
            }

            val kalam =
                sourceKalam.copy(
                    id = 0,
                    title = kalamTitle,
                    onlineSource = "",
                    offlineSource = fileName,
                    isFavorite = 0,
                    playlistId = 0,
                )

            kalamRepository.insert(kalam)

            getDestFile().moveTo(appContext.filesDir.appendPath(fileName)).also {
                quickToast(R.string.dynamic_kalam_saved, kalamTitle)
            }
        }
    }

    fun showKalamSplitDialog() = splitKalamInfo

    private fun splitCompleted(splitStatus: SplitStatus) {
        splitKalamInfo.value
            ?.let {
                val duration =
                    if (splitStatus is SplitStatus.Done) {
                        previewAudioPlayer.getDuration(getDestFile().absolutePath)
                    } else {
                        it.previewKalamLength
                    }

                it.copy(
                    previewKalamLength = duration,
                    splitStatus = splitStatus,
                )
            }
            ?.apply { splitKalamInfo.postValue(this) }
    }

    private fun onProgressChange(progress: Int) {
        splitKalamInfo.value
            ?.copy(
                previewKalamProgress = progress,
            )
            ?.apply { splitKalamInfo.postValue(this) }
    }

    private fun onPlayerComplete(mediaPlayer: MediaPlayer) {
        previewAudioPlayer.releaseProgressListener()
        splitKalamInfo.value
            ?.copy(
                previewKalamProgress = 0,
                previewPlayStart = false,
            )
            ?.apply { splitKalamInfo.postValue(this) }
    }

    private fun togglePlay() {
        if (previewAudioPlayer.isPlaying()) {
            previewAudioPlayer.pause()
        } else {
            previewAudioPlayer.start()
        }
    }

    private fun pausePreviewPlayer() {
        if (previewAudioPlayer.isPlaying()) previewAudioPlayer.pause()
    }

    private fun pauseMainPlayer() {
        if (player.isPlaying()) player.doPlayOrPause()
    }

    private fun getDestFile() = appContext.cacheDir.appendPath(CACHE_SPLIT_FILENAME)
}
