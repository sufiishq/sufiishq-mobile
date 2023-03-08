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

package pk.sufiishq.app.feature.app

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.android.material.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import pk.sufiishq.app.utils.TextRes
import pk.sufiishq.app.utils.getString
import timber.log.Timber
import javax.inject.Inject

class PermissionManager @Inject constructor() {

    private var resultLauncher: ActivityResultLauncher<String>? = null

    fun validateNotificationPermission(activity: ComponentActivity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.POST_NOTIFICATIONS,
                ) == PackageManager.PERMISSION_GRANTED -> {
                    Timber.d("WoW! we have notification permissions")
                }
                activity.shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
                    showNotificationPermissionRationale(activity)
                }
                else -> {
                    resultLauncher =
                        activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) { }
                    resultLauncher?.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
    }

    private fun showNotificationPermissionRationale(activity: ComponentActivity) {
        MaterialAlertDialogBuilder(activity, R.style.MaterialAlertDialog_Material3)
            .setTitle(getString(TextRes.label_alert))
            .setMessage(getString(TextRes.msg_require_notif_permissions))
            .setPositiveButton(getString(TextRes.label_ok)) { _, _ ->
                val intent = Intent(ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:${activity.packageName}")
                activity.startActivity(intent)
            }
            .setCancelable(false)
            .show()
    }
}
