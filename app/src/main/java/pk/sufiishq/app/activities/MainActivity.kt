package pk.sufiishq.app.activities

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import dagger.hilt.android.AndroidEntryPoint
import pk.sufiishq.app.SufiIshqApp
import pk.sufiishq.app.data.repository.KalamRepository
import pk.sufiishq.app.helpers.Screen
import pk.sufiishq.app.services.AudioPlayerService
import pk.sufiishq.app.ui.screen.MainView
import pk.sufiishq.app.ui.theme.SufiIshqTheme
import pk.sufiishq.app.utils.DARK_THEME
import pk.sufiishq.app.utils.getFromStorage
import pk.sufiishq.app.utils.isDeviceSupportDarkMode
import pk.sufiishq.app.utils.observeOnce
import pk.sufiishq.app.viewmodels.AssetKalamLoaderViewModel
import pk.sufiishq.app.viewmodels.KalamViewModel
import pk.sufiishq.app.viewmodels.PlayerViewModel
import pk.sufiishq.app.viewmodels.PlaylistViewModel
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity(), ServiceConnection {

    @Inject
    lateinit var kalamRepository: KalamRepository

    private val playerViewModel: PlayerViewModel by viewModels()
    private val kalamViewModel: KalamViewModel by viewModels()
    private val playlistDataProvider: PlaylistViewModel by viewModels()
    private val assetKalamLoaderViewModel: AssetKalamLoaderViewModel by viewModels()
    private val playerIntent by lazy { Intent(this, AudioPlayerService::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme()

        setContent {
            SufiIshqTheme {
                MainView(playerViewModel, kalamViewModel, playlistDataProvider)
            }
        }

        with(assetKalamLoaderViewModel) {
            this.countAll().observe(this@MainActivity) { count ->
                this.loadAllKalam(count) {
                    bindService(playerIntent, this@MainActivity, Context.BIND_AUTO_CREATE)
                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        playerViewModel.disposeDownload()
        assetKalamLoaderViewModel.release()
        unbindService(this)
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val binder = service as AudioPlayerService.AudioPlayerBinder
        val playerController = binder.getService()
        SufiIshqApp.getInstance().setPlayerController(playerController)

        if (playerController.getActiveTrack() == null) {
            kalamRepository.getDefaultKalam().observeOnce(this@MainActivity) { kalam ->
                playerController.setActiveTrack(kalam, Screen.Tracks.ALL, 0)
                playerViewModel.setPlayerService(playerController)
                playerController.setPlayerListener(playerViewModel)
            }
        } else {
            playerViewModel.setPlayerService(playerController)
            playerController.setPlayerListener(playerViewModel)
        }

        handleDeeplink(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleDeeplink(intent)
    }

    private fun handleDeeplink(intent: Intent?) {
        FirebaseDynamicLinks.getInstance()
            .getDynamicLink(intent)
            .addOnSuccessListener { pendingDynamicLinkData ->

                var deepLink: Uri? = null

                if (pendingDynamicLinkData != null) {
                    deepLink = pendingDynamicLinkData.link
                }

                deepLink?.let { uri ->

                    uri.pathSegments?.let { pathSegments ->
                        if (pathSegments.size == 2) {
                            val kalamId = pathSegments[pathSegments.size.minus(1)]
                            kalamViewModel.getKalam(kalamId.toInt())
                                .observe(this@MainActivity) { kalam ->
                                    kalam?.let {
                                        playerViewModel.changeTrack(
                                            kalam,
                                            kalamViewModel.getActiveTrackType(),
                                            kalamViewModel.getActivePlaylistId()
                                        )
                                    }
                                }
                        }
                    }
                }
            }
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        playerViewModel.setPlayerService(null)
        SufiIshqApp.getInstance().setPlayerController(null)
    }

    private fun setTheme() {
        if (!isDeviceSupportDarkMode()) {
            IS_DARK_THEME = DARK_THEME.getFromStorage(false)
        }
    }

    companion object {
        var IS_DARK_THEME = false
    }
}