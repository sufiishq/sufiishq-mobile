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

package pk.sufiishq.app.feature.kalam.splitter

import android.media.MediaPlayer
import android.os.Handler
import javax.inject.Inject

class PreviewAudioPlayer
@Inject
constructor(
    private val handler: Handler,
    private val sourcePlayer: MediaPlayer,
) {

    private var onProgressChange: (progress: Int) -> Unit = {}

    private val runnable =
        object : Runnable {
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
