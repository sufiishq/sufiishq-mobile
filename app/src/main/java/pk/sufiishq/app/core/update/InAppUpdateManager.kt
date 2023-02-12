package pk.sufiishq.app.core.update

import android.view.View
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import javax.inject.Inject
import javax.inject.Singleton
import pk.sufiishq.app.R
import pk.sufiishq.app.data.providers.MainDataProvider
import pk.sufiishq.app.utils.getString

@Singleton
class InAppUpdateManager @Inject constructor() {

    private lateinit var listener: Listener
    private lateinit var activity: ComponentActivity
    private lateinit var appUpdateInfo: AppUpdateInfo

    fun checkInAppUpdate(activity: ComponentActivity, mainDataProvider: MainDataProvider) {

        val appUpdateManager = AppUpdateManagerFactory.create(activity)

        this.activity = activity
        listener = Listener(activity)
        appUpdateManager.registerListener(listener)

        // Returns an intent object that you use to check for an update.
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        appUpdateInfoTask
            .addOnSuccessListener { appUpdateInfo ->
                this.appUpdateInfo = appUpdateInfo

                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                    mainDataProvider.showUpdateButton(true)
                } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                    popupSnackbarForCompleteUpdate(activity)
                }
            }
    }

    fun startUpdateFlow() {
        val appUpdateManager = AppUpdateManagerFactory.create(activity)

        if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
            appUpdateManager.startUpdateFlowForResult(
                appUpdateInfo,
                AppUpdateType.FLEXIBLE,
                activity,
                99
            )
        }
    }

    fun unregisterListener(activity: ComponentActivity) {
        AppUpdateManagerFactory.create(activity).unregisterListener(listener)
    }

    private fun popupSnackbarForCompleteUpdate(activity: ComponentActivity) {
        Snackbar.make(
            activity.findViewById<View>(android.R.id.content).rootView,
            getString(R.string.label_update_downloaded),
            Snackbar.LENGTH_INDEFINITE
        ).apply {
            setAction(getString(R.string.label_restart_all_caps)) {
                AppUpdateManagerFactory.create(activity).completeUpdate()
            }
            setActionTextColor(ContextCompat.getColor(activity, R.color.dark_secondary))
            show()
        }
    }

    private inner class Listener(val activity: ComponentActivity) : InstallStateUpdatedListener {
        override fun onStateUpdate(installState: InstallState) {
            if (installState.installStatus() == InstallStatus.DOWNLOADED) {
                popupSnackbarForCompleteUpdate(activity)
            }
        }
    }
}