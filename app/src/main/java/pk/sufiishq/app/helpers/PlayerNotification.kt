package pk.sufiishq.app.helpers

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import pk.sufiishq.app.R
import pk.sufiishq.app.activities.MainActivity
import pk.sufiishq.app.models.Kalam
import pk.sufiishq.app.services.AudioPlayerService

@SuppressLint("UnspecifiedImmutableFlag")
class PlayerNotification(private val context: Context) {

    private val pendingIntent: PendingIntent by lazy {
        PendingIntent.getActivity(
            context,
            0,
            Intent(context, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val notificationChannel =
                NotificationChannel(
                    AudioPlayerService.CHANNEL_ID,
                    "Sufi Ishq",
                    NotificationManager.IMPORTANCE_MIN
                )
            notificationChannel.enableLights(false)
            notificationChannel.setShowBadge(false)
            notificationChannel.lockscreenVisibility = Notification.VISIBILITY_SECRET
            val manager =
                context.getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(notificationChannel)
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    fun buildNotification(activeKalam: Kalam?, service: AudioPlayerService) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val builder = Notification.Builder(context, AudioPlayerService.CHANNEL_ID)

            builder.setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_start_logo)
                .setTicker(activeKalam?.title)
                .setOngoing(true)
                .setContentTitle(activeKalam?.title)
                .setContentText("${activeKalam?.location} ${activeKalam?.year}")

            service.startForeground(AudioPlayerService.NOTIFY_ID, builder.build())
        } else {
            val builder = NotificationCompat.Builder(context, AudioPlayerService.CHANNEL_ID)

            builder.setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_start_logo)
                .setTicker(activeKalam?.title)
                .setOngoing(true)
                .setContentTitle(activeKalam?.title)
                .setContentText("${activeKalam?.location} ${activeKalam?.year}")

            service.startForeground(AudioPlayerService.NOTIFY_ID, builder.build())
        }
    }
}