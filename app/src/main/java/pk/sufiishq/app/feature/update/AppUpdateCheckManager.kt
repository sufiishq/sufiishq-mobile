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

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.ComponentActivity
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.UpdateAvailability
import pk.sufiishq.app.feature.app.controller.MainController
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppUpdateCheckManager @Inject constructor() {

    fun checkInAppUpdate(activity: ComponentActivity, mainController: MainController) {
        val appUpdateManager = AppUpdateManagerFactory.create(activity)

        appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
            mainController.showUpdateDialog(
                appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE,
            )
        }
    }

    fun routeToPlayStore(context: Context) {
        try {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=${context.packageName}"),
                ),
            )
        } catch (e: ActivityNotFoundException) {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=${context.packageName}"),
                ),
            )
        }
    }
}
