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

package pk.sufiishq.app.feature.player.service

import androidx.lifecycle.LifecycleService
import androidx.lifecycle.asFlow
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import pk.sufiishq.app.di.qualifier.IoDispatcher
import pk.sufiishq.app.feature.kalam.data.repository.KalamRepository
import pk.sufiishq.app.feature.kalam.model.Kalam
import pk.sufiishq.app.feature.kalam.model.KalamInfo
import pk.sufiishq.app.feature.player.controller.AudioPlayer
import pk.sufiishq.app.feature.player.di.qualifier.AndroidMediaPlayer
import pk.sufiishq.app.feature.player.listener.PlayerStateListener
import pk.sufiishq.app.feature.player.state.MediaState
import pk.sufiishq.app.feature.player.util.PlayerNotification
import pk.sufiishq.app.feature.storage.LastKalamPlayLiveData
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class AudioPlayerService : LifecycleService(), PlayerStateListener {

    @Inject @AndroidMediaPlayer
    lateinit var player: AudioPlayer

    @Inject lateinit var kalamRepository: KalamRepository

    @Inject lateinit var activePlay: LastKalamPlayLiveData

    @IoDispatcher @Inject
    lateinit var dispatcher: CoroutineContext

    @Inject lateinit var playerNotification: PlayerNotification

    private var autoPlay = false
    private var job: Job? = null

    override fun onCreate() {
        super.onCreate()

        autoPlay = false
        job = null
        player.registerListener(this)

        lifecycleScope.launch { activePlay.asFlow().collectLatest(::kalamInfoCollected) }
    }

    override fun onStateChange(mediaState: MediaState) {
        when (mediaState) {
            // start foreground notification when received state is prepared or resume
            is MediaState.Resume -> buildNotification(mediaState.kalam)

            // remove foreground notification when received state is pause or idle
            is MediaState.Idle,
            is MediaState.Pause,
            -> removeNotification()

            // received different states
            else -> {
                /* do nothing */
            }
        }
    }

    private fun buildNotification(kalam: Kalam) {
        playerNotification.buildNotification(kalam, this)
    }

    private fun removeNotification() {
        stopForeground(STOP_FOREGROUND_REMOVE)
    }

    private fun kalamInfoCollected(kalamInfo: KalamInfo?) {
        kalamInfo?.let {
            job =
                CoroutineScope(dispatcher).launch {
                    kalamRepository.getKalam(kalamInfo.kalam.id).asFlow().cancellable().collectLatest {
                        kalamCollected(it, kalamInfo)
                    }
                }
        }
    }

    private fun kalamCollected(kalam: Kalam?, kalamInfo: KalamInfo) {
        kalam?.let {
            player.setSource(kalam, kalamInfo.trackListType)
            if (autoPlay) player.doPlayOrPause()
            autoPlay = true
            job?.cancel()
        }
    }

    companion object {
        const val NOTIFY_ID = 1
        const val CHANNEL_ID = "SufiIshq"
    }
}
