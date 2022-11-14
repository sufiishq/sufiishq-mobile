package pk.sufiishq.app.core.player.service

import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import dagger.hilt.android.AndroidEntryPoint
import pk.sufiishq.app.core.player.AudioPlayer
import pk.sufiishq.app.core.player.listener.PlayerStateListener
import pk.sufiishq.app.core.player.state.MediaState
import pk.sufiishq.app.core.player.util.PlayerNotification
import pk.sufiishq.app.data.repository.KalamRepository
import pk.sufiishq.app.di.qualifier.AndroidMediaPlayer
import pk.sufiishq.app.models.Kalam
import pk.sufiishq.app.utils.app
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class AudioPlayerService : LifecycleService(), PlayerStateListener {

    @Inject
    @AndroidMediaPlayer
    lateinit var player: AudioPlayer

    @Inject
    lateinit var kalamRepository: KalamRepository

    private val notification: PlayerNotification by lazy { PlayerNotification(this) }

    override fun onCreate() {
        super.onCreate()
        player.registerListener(this)
    }

    override fun onStateChange(mediaState: MediaState) {
        when (mediaState) {

            // when received state is prepared or resume
            is MediaState.Prepared, is MediaState.Resume -> buildNotification(mediaState.kalam)

            // when received state is pause
            is MediaState.Pause -> removeNotification()

            // when received state is complete
            is MediaState.Complete -> complete(mediaState.kalam)

            // received different states
            else -> Timber.d("got another state $mediaState")
        }
    }

    private fun buildNotification(kalam: Kalam) {
        notification.buildNotification(kalam, this)
    }

    private fun removeNotification() {
        stopForeground(true)
    }

    private fun complete(kalam: Kalam) {
        kalamRepository.getNextKalam(
            kalam.id,
            player.getTrackListType(),
            app().appConfig.isShuffle()
        )
            .observe(this) { nextKalam ->
                nextKalam?.let {
                    player.setSource(nextKalam, player.getTrackListType())
                    player.doPlayOrPause()
                }
            }
    }

    companion object {
        const val NOTIFY_ID = 1
        const val CHANNEL_ID = "SufiIshq"
    }
}