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

package pk.sufiishq.app.core.player.util

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import pk.sufiishq.app.R
import pk.sufiishq.app.activities.MainActivity
import pk.sufiishq.app.core.kalam.model.Kalam
import pk.sufiishq.app.core.player.service.AudioPlayerService
import pk.sufiishq.app.utils.formatDateAs
import pk.sufiishq.app.utils.getString

@SuppressLint("UnspecifiedImmutableFlag")
class PlayerNotification(private val context: Context) {

    private val pendingIntent: PendingIntent by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(
                context,
                0,
                Intent(context, MainActivity::class.java),
                PendingIntent.FLAG_IMMUTABLE,
            )
        } else {
            PendingIntent.getActivity(
                context,
                0,
                Intent(context, MainActivity::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT,
            )
        }
    }

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val notificationChannel =
                NotificationChannel(
                    AudioPlayerService.CHANNEL_ID,
                    getString(R.string.app_name),
                    NotificationManager.IMPORTANCE_MIN,
                )
            notificationChannel.enableLights(false)
            notificationChannel.setShowBadge(false)
            notificationChannel.lockscreenVisibility = Notification.VISIBILITY_SECRET
            val manager = context.getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(notificationChannel)
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    fun buildNotification(activeKalam: Kalam?, service: Service) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val builder = Notification.Builder(context, AudioPlayerService.CHANNEL_ID)

            builder
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_start_logo)
                .setTicker(activeKalam?.title)
                .setOngoing(true)
                .setContentTitle(activeKalam?.title)
                .setContentText(
                    "${activeKalam?.location} ${
                        activeKalam?.recordeDate?.formatDateAs(
                            prefix = "- ",
                        )
                    }",
                )

            service.startForeground(AudioPlayerService.NOTIFY_ID, builder.build())
        } else {
            val builder = NotificationCompat.Builder(context, AudioPlayerService.CHANNEL_ID)

            builder
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_start_logo)
                .setTicker(activeKalam?.title)
                .setOngoing(true)
                .setContentTitle(activeKalam?.title)
                .setContentText(
                    "${activeKalam?.location} ${
                        activeKalam?.recordeDate?.formatDateAs(
                            prefix = "- ",
                        )
                    }",
                )

            service.startForeground(AudioPlayerService.NOTIFY_ID, builder.build())
        }
    }
}
