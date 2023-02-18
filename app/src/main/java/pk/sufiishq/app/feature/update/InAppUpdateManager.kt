/*
 * Copyright 2022-2023 SufiIshq
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pk.sufiishq.app.feature.update

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
import pk.sufiishq.app.R
import pk.sufiishq.app.feature.app.controller.MainController
import pk.sufiishq.app.utils.getString
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InAppUpdateManager @Inject constructor() {

    private lateinit var listener: Listener
    private lateinit var activity: ComponentActivity
    private lateinit var appUpdateInfo: AppUpdateInfo

    fun checkInAppUpdate(activity: ComponentActivity, mainController: MainController) {
        val appUpdateManager = AppUpdateManagerFactory.create(activity)

        this.activity = activity
        listener = Listener(activity)
        appUpdateManager.registerListener(listener)

        // Returns an intent object that you use to check for an update.
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            this.appUpdateInfo = appUpdateInfo

            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                mainController.showUpdateButton(true)
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
                99,
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
            Snackbar.LENGTH_INDEFINITE,
        )
            .apply {
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
