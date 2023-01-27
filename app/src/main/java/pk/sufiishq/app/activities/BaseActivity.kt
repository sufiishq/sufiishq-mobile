package pk.sufiishq.app.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import pk.sufiishq.app.core.event.events.PlayerEvents
import pk.sufiishq.app.core.player.AudioPlayer
import pk.sufiishq.app.core.player.service.AudioPlayerService
import pk.sufiishq.app.data.repository.KalamRepository
import pk.sufiishq.app.di.qualifier.AndroidMediaPlayer
import pk.sufiishq.app.helpers.InAppUpdateManager
import pk.sufiishq.app.helpers.ObserveOnlyOnce
import pk.sufiishq.app.helpers.TrackListType
import pk.sufiishq.app.models.Kalam
import pk.sufiishq.app.utils.DARK_THEME
import pk.sufiishq.app.utils.dispatch
import pk.sufiishq.app.utils.getFromStorage
import pk.sufiishq.app.utils.isDeviceSupportDarkMode
import pk.sufiishq.app.viewmodels.AssetKalamLoaderViewModel
import pk.sufiishq.app.viewmodels.HomeViewModel


@AndroidEntryPoint
open class BaseActivity : ComponentActivity() {

    @Inject
    lateinit var kalamRepository: KalamRepository

    @Inject
    @AndroidMediaPlayer
    lateinit var player: AudioPlayer

    @Inject
    lateinit var inAppUpdateManager: InAppUpdateManager

    private val assetKalamLoaderViewModel: AssetKalamLoaderViewModel by viewModels()
    private val homeViewModel: HomeViewModel by viewModels()

    private val playerIntent by lazy { Intent(this, AudioPlayerService::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme()

        val observeOnlyOnce = ObserveOnlyOnce<Kalam>()
        with(assetKalamLoaderViewModel) {

            // count number of kalam
            this.countAll().observe(this@BaseActivity) { count ->

                // load kalam from assets to db if number of count is 0. note: loading done only once
                this.loadAllKalam(count) {

                    // setup player ui if player is pause/stop
                    if (!player.isPlaying()) {

                        startService(playerIntent)
                        observeOnlyOnce.take(this@BaseActivity, kalamRepository.getDefaultKalam()) {
                            player.setSource(it, TrackListType.All())
                        }
                    }

                    // handle all incoming deep links and extract the kalam-id and play
                    handleDeeplink(intent)

                    // check any incoming update from play-store
                    inAppUpdateManager.checkInAppUpdate(this@BaseActivity)
                }
            }
        }


    }

    override fun onDestroy() {
        super.onDestroy()

        runCatching {
            PlayerEvents.DisposeDownload.dispatch()
        }

        runCatching {
            inAppUpdateManager.unregisterListener(this)
        }

        runCatching {
            assetKalamLoaderViewModel.release()
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

    private fun handleDeeplink(intent: Intent?) {
        if (isActivityLaunchedFromHistory()) return
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
                            val observeOnlyOnce = ObserveOnlyOnce<Kalam?>()
                            observeOnlyOnce.take(
                                this@BaseActivity, homeViewModel.getKalam(kalamId.toInt())
                            ) { kalam ->
                                kalam?.let {
                                    PlayerEvents.ChangeTrack(
                                        kalam, TrackListType.All()
                                    ).dispatch()
                                }
                            }
                        }
                    }
                }
            }
    }

    private fun setTheme() {
        if (!isDeviceSupportDarkMode()) {
            IS_DARK_THEME = DARK_THEME.getFromStorage(false)
        }
    }

    private fun isActivityLaunchedFromHistory(): Boolean {
        return intent.flags == (Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY)
    }

    companion object {
        var IS_DARK_THEME = false
    }
}