package pk.sufiishq.app.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import pk.sufiishq.app.core.app.AppManager
import pk.sufiishq.app.core.firebase.AuthManager
import pk.sufiishq.app.core.player.AudioPlayer
import pk.sufiishq.app.core.player.service.AudioPlayerService
import pk.sufiishq.app.di.qualifier.AndroidMediaPlayer
import pk.sufiishq.app.viewmodels.AssetKalamLoaderViewModel
import pk.sufiishq.app.viewmodels.MainViewModel
import timber.log.Timber


@AndroidEntryPoint
open class BaseActivity : FragmentActivity() {

    @Inject
    @AndroidMediaPlayer
    lateinit var player: AudioPlayer

    @Inject
    lateinit var appManager: AppManager

    @Inject
    lateinit var authManager: AuthManager

    private val mainDataProvider: MainViewModel by viewModels()
    private val assetKalamLoaderViewModel: AssetKalamLoaderViewModel by viewModels()
    private val playerIntent by lazy { Intent(this, AudioPlayerService::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        assetKalamLoaderViewModel.loadAllKalam()

        startService(playerIntent)

        // check any incoming update from play-store
        mainDataProvider.checkUpdate(this@BaseActivity)

        // handle all incoming deep links and extract the kalam-id and play
        handleDeeplink(intent)

        authManager.registerActivityResultListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()

        runCatching {
            mainDataProvider.unregisterListener(this)
        }

        if (!player.isPlaying()) {
            player.release()
            stopService(playerIntent)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleDeeplink(intent)
    }

    override fun onPause() {
        super.onPause()
        Timber.d("mint-> onPause called")
    }

    override fun onStop() {
        super.onStop()
        Timber.d("mint-> onStop called")
    }

    private fun handleDeeplink(intent: Intent?) {
        appManager.handleShareKalamDeepLink(intent, this)
    }
}