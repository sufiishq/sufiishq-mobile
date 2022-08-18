package pk.sufiishq.app.worker

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import pk.sufiishq.app.R
import pk.sufiishq.app.core.player.service.AudioPlayerService
import pk.sufiishq.app.utils.deleteContent

class CacheRemoveWorker(context: Context, workerParam: WorkerParameters) :
    Worker(context, workerParam) {

    companion object {
        val TAG = CacheRemoveWorker::class.simpleName!!
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
    fun buildNotification(info: String, context: Context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val builder = Notification.Builder(context, AudioPlayerService.CHANNEL_ID)

            builder.setSmallIcon(R.drawable.ic_start_logo)
                .setTicker("SufiIshq")
                .setOngoing(false)
                .setContentTitle("Sufi Ishq")
                .setContentText(info)

            with(NotificationManagerCompat.from(context)) {
                notify(0, builder.build())
            }
        } else {
            val builder = NotificationCompat.Builder(context, AudioPlayerService.CHANNEL_ID)

            builder.setSmallIcon(R.drawable.ic_start_logo)
                .setTicker("SufiIshq")
                .setOngoing(false)
                .setContentTitle("Sufi Ishq")
                .setContentText(info)

            with(NotificationManagerCompat.from(context)) {
                notify(0, builder.build())
            }
        }
    }

    override fun doWork(): Result {

        val totalCacheSize = getCacheSize()
        applicationContext.cacheDir.deleteContent()
        val cacheSizeAfterClear = getCacheSize()

        buildNotification(
            "Total cache size: $totalCacheSize MB, Cache Size after clear: $cacheSizeAfterClear MB",
            applicationContext
        )

        return Result.success()
    }

    private fun getCacheSize(): String {
        return applicationContext.cacheDir.listFiles().let {
            var totalSize = 0.toLong()

            it?.forEach { file ->
                totalSize = totalSize.plus(file.length())
            }

            "%.2f".format(totalSize.toDouble() / 1024 / 1024)
        }
    }
}