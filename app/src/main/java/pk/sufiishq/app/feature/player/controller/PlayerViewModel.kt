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

package pk.sufiishq.app.feature.player.controller

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import pk.sufiishq.app.feature.kalam.model.KalamInfo
import pk.sufiishq.app.feature.player.di.qualifier.AndroidMediaPlayer
import pk.sufiishq.app.feature.player.listener.PlayerStateListener
import pk.sufiishq.app.feature.player.state.MediaState
import pk.sufiishq.app.feature.player.state.mapToIdleState
import pk.sufiishq.app.feature.player.state.mapToLoadingState
import pk.sufiishq.app.feature.player.state.mapToPauseState
import pk.sufiishq.app.feature.player.state.mapToPlayingState
import pk.sufiishq.app.feature.player.state.mapToResumeState
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class PlayerViewModel
@Inject
constructor(
    @AndroidMediaPlayer private val player: AudioPlayer,
) : ViewModel(), PlayerController, PlayerStateListener {

    private val kalamInfo = MutableLiveData<KalamInfo?>(null)
    private var seekbarEnableOnPlaying = true

    init {
        player.registerListener(this)
    }

    override fun getKalamInfo(): LiveData<KalamInfo?> {
        return kalamInfo
    }

    override fun updateSeekbarValue(value: Float) {
        seekbarEnableOnPlaying = false

        kalamInfo.value =
            kalamInfo.value?.copy(
                currentProgress = value.toInt(),
            )
    }

    override fun onSeekbarChanged(value: Int) {
        seekbarEnableOnPlaying = true
        player.seekTo(value)
    }

    override fun doPlayOrPause() {
        player.doPlayOrPause()
    }

    private fun updateKalamInfo(updatedKalamInfo: KalamInfo) {
        kalamInfo.postValue(updatedKalamInfo)
    }

    override fun onStateChange(mediaState: MediaState) {
        val newKalamInfo =
            when (mediaState) {
                is MediaState.Loading -> mediaState.mapToLoadingState(kalamInfo.value!!)
                is MediaState.Playing ->
                    mediaState.mapToPlayingState(
                        kalamInfo.value,
                        seekbarEnableOnPlaying,
                    )
                is MediaState.Pause -> mediaState.mapToPauseState()
                is MediaState.Resume -> mediaState.mapToResumeState()
                is MediaState.Idle,
                is MediaState.Stop,
                is MediaState.Complete,
                is MediaState.Error,
                -> mediaState.mapToIdleState()
            }

        updateKalamInfo(newKalamInfo)
    }
}
