package pk.sufiishq.app.core.player.service

import androidx.lifecycle.LifecycleService
import androidx.lifecycle.asFlow
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import pk.sufiishq.app.core.player.AudioPlayer
import pk.sufiishq.app.core.player.listener.PlayerStateListener
import pk.sufiishq.app.core.player.state.MediaState
import pk.sufiishq.app.core.player.util.PlayerNotification
import pk.sufiishq.app.core.storage.LastKalamPlayLiveData
import pk.sufiishq.app.data.repository.KalamRepository
import pk.sufiishq.app.di.qualifier.AndroidMediaPlayer
import pk.sufiishq.app.di.qualifier.IoDispatcher
import pk.sufiishq.app.models.Kalam
import pk.sufiishq.app.models.KalamInfo
import kotlin.coroutines.CoroutineContext


@AndroidEntryPoint
class AudioPlayerService : LifecycleService(), PlayerStateListener {

    @Inject
    @AndroidMediaPlayer
    lateinit var player: AudioPlayer

    @Inject
    lateinit var kalamRepository: KalamRepository

    @Inject
    lateinit var activePlay: LastKalamPlayLiveData

    @IoDispatcher
    @Inject
    lateinit var dispatcher: CoroutineContext

    private val notification: PlayerNotification by lazy { PlayerNotification(this) }
    private var autoPlay = false
    private var job: Job? = null

    override fun onCreate() {
        super.onCreate()

        autoPlay = false
        job = null
        player.registerListener(this)

        lifecycleScope.launch {
            activePlay.asFlow().collectLatest(::kalamInfoCollected)
        }
    }

    override fun onStateChange(mediaState: MediaState) {
        when (mediaState) {

            // start foreground notification when received state is prepared or resume
            is MediaState.Resume -> buildNotification(mediaState.kalam)

            // remove foreground notification when received state is pause or idle
            is MediaState.Idle, is MediaState.Pause -> removeNotification()

            // received different states
            else -> { /* do nothing */
            }
        }
    }

    private fun buildNotification(kalam: Kalam) {
        notification.buildNotification(kalam, this)
    }

    private fun removeNotification() {
        stopForeground(STOP_FOREGROUND_REMOVE)
    }

    private fun kalamInfoCollected(kalamInfo: KalamInfo?) {
        kalamInfo?.let {
            job = CoroutineScope(dispatcher).launch {
                kalamRepository
                    .getKalam(kalamInfo.kalam.id)
                    .asFlow()
                    .cancellable()
                    .collectLatest {
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