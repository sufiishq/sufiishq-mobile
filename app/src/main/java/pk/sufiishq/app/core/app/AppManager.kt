package pk.sufiishq.app.core.app

import android.app.Activity
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import com.google.android.gms.tasks.Task
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.dynamiclinks.PendingDynamicLinkData
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import pk.sufiishq.app.R
import pk.sufiishq.app.core.player.PlayerManager
import pk.sufiishq.app.data.repository.KalamRepository
import pk.sufiishq.app.di.qualifier.IoDispatcher
import pk.sufiishq.app.helpers.TrackListType
import pk.sufiishq.app.models.Kalam
import pk.sufiishq.app.utils.DEEPLINK_HOST
import pk.sufiishq.app.utils.getString
import pk.sufiishq.app.utils.isNetworkAvailable
import pk.sufiishq.app.utils.quickToast
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

class AppManager @Inject constructor(
    @IoDispatcher private val dispatcher: CoroutineContext,
    private val kalamRepository: KalamRepository,
    private val playerManager: PlayerManager
) {

    private val shareKalamIndicatorDialog = MutableLiveData(false)

    fun shareKalam(kalam: Kalam, componentActivity: ComponentActivity) {

        setShareKalamIndicatorDialog(true)

        FirebaseDynamicLinks.getInstance().createDynamicLink()
            .setLink("$DEEPLINK_HOST/kalam/${kalam.id}".toUri())
            .setDomainUriPrefix(DEEPLINK_HOST)
            .setAndroidParameters(DynamicLink.AndroidParameters.Builder().build())
            .buildShortDynamicLink().addOnSuccessListener { task ->
                setShareKalamIndicatorDialog(false)
                val appName = componentActivity.getString(R.string.app_name)
                Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_SUBJECT, kalam.title)
                    putExtra(Intent.EXTRA_TEXT, task.shortLink.toString())
                }.also {
                    componentActivity.startActivity(
                        Intent.createChooser(
                            it,
                            getString(R.string.dynamic_share_kalam, appName)
                        )
                    )
                }
            }.addOnFailureListener {
                setShareKalamIndicatorDialog(false)
                if (!componentActivity.isNetworkAvailable()) {
                    quickToast(R.string.msg_no_network_connection)
                } else {
                    quickToast(
                        it.message ?: componentActivity.getString(R.string.label_unknown_error)
                    )
                }
                Timber.e(it)
            }
    }

    fun showKalamShareIndicatorDialog(): LiveData<Boolean> {
        return shareKalamIndicatorDialog
    }

    private fun setShareKalamIndicatorDialog(isShow: Boolean) {
        shareKalamIndicatorDialog.postValue(isShow)
    }

    fun shareApp(activity: ComponentActivity) {
        val appName = activity.getString(R.string.app_name)
        Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, appName)
            putExtra(
                Intent.EXTRA_TEXT,
                "https://play.google.com/store/apps/details?id=pk.sufiishq.app"
            )
        }.also {
            activity.startActivity(
                Intent.createChooser(
                    it,
                    getString(R.string.dynamic_share_kalam, appName)
                )
            )
        }
    }

    fun handleShareKalamDeepLink(intent: Intent?, activity: Activity) {
        FirebaseDynamicLinks.getInstance()
            .getDynamicLink(intent)
            .addOnSuccessListener(activity, ::resolveKalamDeepLink)
            .addOnCompleteListener {
                clearDeeplink(activity, it)
            }
    }

    private fun resolveKalamDeepLink(pendingDynamicLinkData: PendingDynamicLinkData?) {

        pendingDynamicLinkData
            ?.link
            ?.pathSegments
            ?.takeIf { it.size == 2 }
            ?.apply {
                playReceivedKalam(this[this.size.minus(1)].toInt())
            }

    }

    private fun clearDeeplink(activity: Activity, task: Task<PendingDynamicLinkData?>?) {
        if (task?.result != null) {
            activity.intent = Intent()
        }
    }

    private fun playReceivedKalam(kalamId: Int) {
        var job: Job? = null
        job = CoroutineScope(dispatcher).launch {
            delay(500)
            kalamRepository.getKalam(kalamId).asFlow().cancellable().collectLatest {
                it?.let {
                    playerManager.changeTrack(it, TrackListType.All())
                }
                job?.cancel()
            }
        }
    }
}