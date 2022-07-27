package pk.sufiishq.app.activities

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import pk.sufiishq.app.SufiIshqApp
import pk.sufiishq.app.data.repository.KalamRepository
import pk.sufiishq.app.services.AudioPlayerService
import pk.sufiishq.app.ui.screen.MainView
import pk.sufiishq.app.ui.theme.SufiIshqTheme
import pk.sufiishq.app.utils.observeOnce
import pk.sufiishq.app.viewmodels.PlayerViewModel
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity(), ServiceConnection {

    @Inject
    lateinit var kalamRepository: KalamRepository

    private val playerViewModel: PlayerViewModel by viewModels()
    private val playerIntent by lazy { Intent(this, AudioPlayerService::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SufiIshqTheme {
                MainView(playerViewModel)
            }
        }

        init {
            bindService(playerIntent, this, Context.BIND_AUTO_CREATE)
        }
    }

    private fun init(initiated: () -> Unit) {
        kalamRepository.countAll().observe(this) { count ->
            if (count <= 0) {
                kalamRepository.loadAllFromAssets(this).observe(this) { allKalams ->
                    lifecycleScope.launch {
                        kalamRepository.insertAll(allKalams)
                        initiated()
                    }
                }
            } else {
                initiated()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        playerViewModel.disposeDownload()
        unbindService(this)
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val binder = service as AudioPlayerService.AudioPlayerBinder
        val playerController = binder.getService()
        SufiIshqApp.getInstance().setPlayerController(playerController)

        if (playerController.getActiveTrack() == null) {
            kalamRepository.getDefaultKalam().observeOnce(this@MainActivity) { kalam ->
                playerController.setActiveTrack(kalam)
                playerViewModel.setPlayerService(playerController)
                playerController.setPlayerListener(playerViewModel)
            }
        } else {
            playerViewModel.setPlayerService(playerController)
            playerController.setPlayerListener(playerViewModel)
        }
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        playerViewModel.setPlayerService(null)
        SufiIshqApp.getInstance().setPlayerController(null)
    }
}