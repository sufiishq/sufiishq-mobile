package pk.sufiishq.app.helpers

import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import pk.sufiishq.app.R
import pk.sufiishq.app.core.event.dispatcher.EventDispatcher
import pk.sufiishq.app.core.event.events.Event
import pk.sufiishq.app.core.event.events.GlobalEvents
import pk.sufiishq.app.core.event.exception.UnhandledEventException
import pk.sufiishq.app.core.event.handler.EventHandler
import pk.sufiishq.app.models.Kalam
import pk.sufiishq.app.utils.DEEPLINK_HOST
import pk.sufiishq.app.utils.isNetworkAvailable
import pk.sufiishq.app.utils.toast
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GlobalEventHandler @Inject constructor(
    private val inAppUpdateManager: InAppUpdateManager,
    eventDispatcher: EventDispatcher
) : EventHandler {

    private val showCircularProgressDialog = MutableLiveData(false)
    private val showUpdateDialog = MutableLiveData(false)

    init {
        eventDispatcher.registerEventHandler(this)
    }

    fun getShowUpdateButton(): LiveData<Boolean> {
        return showUpdateDialog
    }

    fun getShowCircularProgressDialog(): LiveData<Boolean> {
        return showCircularProgressDialog
    }

    private fun setShowUpdateButton(value: Boolean) {
        showUpdateDialog.postValue(value)
    }

    private fun handleUpdate() {
        inAppUpdateManager.startUpdateFlow()
    }

    private fun setCircularProgressDialog(isShow: Boolean) {
        showCircularProgressDialog.postValue(isShow)
    }

    private fun shareApp(context: Context) {
        if (ComponentActivity::class.java.isAssignableFrom(context.javaClass)) {
            val appName = context.getString(R.string.app_name)
            Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, appName)
                putExtra(
                    Intent.EXTRA_TEXT,
                    "https://play.google.com/store/apps/details?id=pk.sufiishq.app"
                )
            }.also {
                context.startActivity(Intent.createChooser(it, "Share $appName"))
            }
        } else {
            throw IllegalArgumentException("context: $context is not an activity")
        }
    }

    private fun shareKalam(kalam: Kalam, context: Context) {

        if (ComponentActivity::class.java.isAssignableFrom(context.javaClass)) {
            setCircularProgressDialog(true)

            FirebaseDynamicLinks.getInstance()
                .createDynamicLink()
                .setLink("$DEEPLINK_HOST/kalam/${kalam.id}".toUri())
                .setDomainUriPrefix(DEEPLINK_HOST)
                .setAndroidParameters(DynamicLink.AndroidParameters.Builder().build())
                .buildShortDynamicLink()
                .addOnSuccessListener { task ->
                    setCircularProgressDialog(false)
                    val appName = context.getString(R.string.app_name)
                    Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_SUBJECT, kalam.title)
                        putExtra(Intent.EXTRA_TEXT, task.shortLink.toString())
                    }.also {
                        context.startActivity(Intent.createChooser(it, "Share $appName"))
                    }
                }
                .addOnFailureListener {
                    setCircularProgressDialog(false)
                    if (!context.isNetworkAvailable()) {
                        context.toast(context.getString(R.string.no_network_connection))
                    } else {
                        context.toast(it.message ?: context.getString(R.string.unknown_error))
                    }
                    Timber.e(it)
                }
        } else {
            throw IllegalArgumentException("context: $context is not an activity")
        }
    }

    override fun onEvent(event: Event) {
        when (event) {
            is GlobalEvents.ShareApp -> shareApp(event.context)
            is GlobalEvents.ShareKalam -> shareKalam(event.kalam, event.context)
            is GlobalEvents.ShowUpdateButton -> setShowUpdateButton(event.isShow)
            else -> throw UnhandledEventException(event, this)
        }
    }
}