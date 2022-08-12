package pk.sufiishq.app.helpers

import android.view.View
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import pk.sufiishq.app.R
import javax.inject.Inject

class InAppUpdateManager @Inject constructor() {

    private lateinit var listener: Listener

    fun checkInAppUpdate(activity: ComponentActivity) {

        val appUpdateManager = AppUpdateManagerFactory.create(activity)

        listener = Listener(activity)
        appUpdateManager.registerListener(listener)

        // Returns an intent object that you use to check for an update.
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        appUpdateInfoTask
            .addOnSuccessListener { appUpdateInfo ->

                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        AppUpdateType.FLEXIBLE,
                        activity,
                        99
                    )
                } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                    popupSnackbarForCompleteUpdate(activity)
                }
            }
    }

    fun unregisterListener(activity: ComponentActivity) {
        AppUpdateManagerFactory.create(activity).unregisterListener(listener)
    }

    private fun popupSnackbarForCompleteUpdate(activity: ComponentActivity) {
        Snackbar.make(
            activity.findViewById<View>(android.R.id.content).rootView,
            "An update has just been downloaded.",
            Snackbar.LENGTH_INDEFINITE
        ).apply {
            setAction("RESTART") {
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