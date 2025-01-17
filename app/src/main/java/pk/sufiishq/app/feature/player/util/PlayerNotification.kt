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

package pk.sufiishq.app.feature.player.util

import android.app.Service
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
import android.os.Build
import pk.sufiishq.app.feature.app.AppNotificationManager
import pk.sufiishq.app.feature.kalam.model.Kalam
import pk.sufiishq.app.feature.player.controller.AudioPlayer
import pk.sufiishq.app.feature.player.di.qualifier.AndroidMediaPlayer
import pk.sufiishq.app.feature.player.service.AudioPlayerService
import pk.sufiishq.app.utils.Constants
import pk.sufiishq.app.utils.formatDateAs
import pk.sufiishq.app.utils.safeTryServiceCall
import javax.inject.Inject

class PlayerNotification @Inject constructor(
    private val appNotificationManager: AppNotificationManager,
) {

    @AndroidMediaPlayer
    @Inject
    lateinit var audioPlayer: AudioPlayer

    fun buildNotification(activeKalam: Kalam?, service: Service) {
        val builder = appNotificationManager.make(
            title = activeKalam!!.title,
            content = "${activeKalam.location} ${
                activeKalam.recordeDate.formatDateAs(
                    prefix = "- ",
                )
            }",
            onGoing = true,
            autoCancel = false,
        ).setSilent(true)

        safeTryServiceCall(audioPlayer) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                service.startForeground(AudioPlayerService.NOTIFY_ID, builder.build())
            } else {
                service.startForeground(AudioPlayerService.NOTIFY_ID, builder.build(), FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK)
            }
        }
    }
}
