package pk.sufiishq.app.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import dagger.hilt.android.AndroidEntryPoint
import pk.sufiishq.app.configs.AppConfig
import pk.sufiishq.app.core.event.dispatcher.EventDispatcher
import pk.sufiishq.app.core.event.events.PlayerEvents
import pk.sufiishq.app.core.player.AudioPlayer
import pk.sufiishq.app.core.player.service.AudioPlayerService
import pk.sufiishq.app.data.repository.KalamRepository
import pk.sufiishq.app.di.qualifier.AndroidMediaPlayer
import pk.sufiishq.app.helpers.GlobalEventHandler
import pk.sufiishq.app.helpers.InAppUpdateManager
import pk.sufiishq.app.helpers.ObserveOnlyOnce
import pk.sufiishq.app.helpers.TrackListType
import pk.sufiishq.app.models.Kalam
import pk.sufiishq.app.utils.DARK_THEME
import pk.sufiishq.app.utils.getFromStorage
import pk.sufiishq.app.utils.isDeviceSupportDarkMode
import pk.sufiishq.app.viewmodels.AssetKalamLoaderViewModel
import pk.sufiishq.app.viewmodels.HomeViewModel
import javax.inject.Inject

@AndroidEntryPoint
open class BaseActivity : ComponentActivity() {

    @Inject
    lateinit var appConfig: AppConfig

    @Inject
    lateinit var kalamRepository: KalamRepository

    @Inject
    @AndroidMediaPlayer
    lateinit var player: AudioPlayer

    @Inject
    lateinit var inAppUpdateManager: InAppUpdateManager

    @Inject
    lateinit var observeOnlyOnce: ObserveOnlyOnce<Kalam>

    @Inject
    lateinit var eventDispatcher: EventDispatcher

    @Inject
    lateinit var globalEventHandler: GlobalEventHandler

    private val assetKalamLoaderViewModel: AssetKalamLoaderViewModel by viewModels()
    protected val homeViewModel: HomeViewModel by viewModels()

    private val playerIntent by lazy { Intent(this, AudioPlayerService::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme()

        with(assetKalamLoaderViewModel) {
            this.countAll().observe(this@BaseActivity) { count ->
                this.loadAllKalam(count) {
                    if (!player.isPlaying()) {
                        startService(playerIntent)
                        observeOnlyOnce.take(this@BaseActivity, kalamRepository.getDefaultKalam()) {
                            player.setSource(it, TrackListType.All())
                        }
                    }
                }
            }
        }

        handleDeeplink(intent)
        inAppUpdateManager.checkInAppUpdate(this)
    }

    override fun onDestroy() {
        super.onDestroy()

        runCatching {
            eventDispatcher.dispatch(PlayerEvents.DisposeDownload)
        }

        runCatching {
            inAppUpdateManager.unregisterListener(this)
        }

        assetKalamLoaderViewModel.release()
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
                            homeViewModel.getKalam(kalamId.toInt())
                                .observe(this@BaseActivity) { kalam ->
                                    kalam?.let {
                                        eventDispatcher.dispatch(
                                            PlayerEvents.ChangeTrack(
                                                kalam,
                                                TrackListType.All()
                                            )
                                        )
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

    companion object {
        var IS_DARK_THEME = false
    }
}