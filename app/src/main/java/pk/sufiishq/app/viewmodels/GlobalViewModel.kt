package pk.sufiishq.app.viewmodels

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import pk.sufiishq.app.R
import pk.sufiishq.app.core.event.events.Event
import pk.sufiishq.app.core.event.events.GlobalEvents
import pk.sufiishq.app.core.event.exception.UnhandledEventException
import pk.sufiishq.app.data.providers.GlobalDataProvider
import pk.sufiishq.app.helpers.InAppUpdateManager

@HiltViewModel
class GlobalViewModel @Inject constructor(
    app: Application,
    private val inAppUpdateManager: InAppUpdateManager
) : BaseViewModel(app), GlobalDataProvider {

    private val showUpdateDialog = MutableLiveData(false)

    override fun getShowUpdateButton(): LiveData<Boolean> {
        return showUpdateDialog
    }

    private fun setShowUpdateButton(value: Boolean) {
        showUpdateDialog.postValue(value)
    }

    private fun handleUpdate() {
        inAppUpdateManager.startUpdateFlow()
    }

    private fun openFacebookGroup(context: Context, groupUrl: String) {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(groupUrl)))
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

    override fun onEvent(event: Event) {
        when (event) {
            is GlobalEvents.ShareApp -> shareApp(event.context)
            is GlobalEvents.ShowUpdateButton -> setShowUpdateButton(event.isShow)
            is GlobalEvents.StartUpdateFlow -> handleUpdate()
            is GlobalEvents.OpenFacebookGroup -> openFacebookGroup(event.context, event.groupUrl)
            else -> throw UnhandledEventException(event, this)
        }
    }
}