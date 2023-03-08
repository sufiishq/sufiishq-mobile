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

package pk.sufiishq.app.feature.media.controller

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import pk.sufiishq.app.feature.player.controller.AudioPlayer
import pk.sufiishq.app.feature.player.di.qualifier.AndroidMediaPlayer
import pk.sufiishq.app.feature.player.listener.PlayerStateListener
import pk.sufiishq.app.feature.player.state.MediaState
import javax.inject.Inject

@HiltViewModel
class VideoPlayViewModel @Inject constructor(
    @AndroidMediaPlayer private val audioPlayer: AudioPlayer,
) : ViewModel(), VideoPlayController, PlayerStateListener {

    private val pauseVideo = MutableLiveData(false)

    init {
        audioPlayer.registerListener(this)
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        if (isPlaying && audioPlayer.isPlaying()) {
            pauseVideo.postValue(false)
            audioPlayer.doPlayOrPause()
        }
    }

    override fun pauseVideo(): LiveData<Boolean> {
        return pauseVideo
    }

    override fun onStateChange(mediaState: MediaState) {
        if (mediaState is MediaState.Playing) {
            pauseVideo.postValue(true)
        }
    }
}
