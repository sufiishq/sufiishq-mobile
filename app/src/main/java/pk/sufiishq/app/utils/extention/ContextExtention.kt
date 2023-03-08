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

package pk.sufiishq.app.utils.extention

import android.app.Activity
import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.TRANSPORT_CELLULAR
import android.net.NetworkCapabilities.TRANSPORT_ETHERNET
import android.net.NetworkCapabilities.TRANSPORT_WIFI
import android.net.Uri
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.Locale

fun Context.isNetworkAvailable(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
    return capabilities?.hasAnyOneTransport(TRANSPORT_CELLULAR, TRANSPORT_WIFI, TRANSPORT_ETHERNET)
        ?: false
}

fun NetworkCapabilities.hasAnyOneTransport(vararg transport: Int): Boolean {
    var value = false
    run lit@{
        transport.forEach {
            if (hasTransport(it)) {
                value = true
                return@lit
            }
        }
    }
    return value
}

fun Context.dpToPx(dp: Float) = dp * resources.displayMetrics.density

fun Context.toastShort(text: String) = this.toast(text, Toast.LENGTH_SHORT)

fun Context.toast(text: String, toastLength: Int = Toast.LENGTH_LONG) =
    CoroutineScope(Dispatchers.Main).launch { Toast.makeText(this@toast, text, toastLength).show() }

fun Context.assetsToBitmap(fileName: String): Bitmap? {
    return try {
        with(assets.open(fileName)) { BitmapFactory.decodeStream(this) }
    } catch (e: IOException) {
        null
    }
}

fun Context.launchGoogleMap(latLng: Pair<Double, Double>) {
    val uri: String =
        String.format(
            Locale.ENGLISH,
            "geo:%f,%f?z=17",
            latLng.first,
            latLng.second,
        )
    Intent(Intent.ACTION_VIEW, Uri.parse(uri)).apply { startActivity(this) }
}

fun Context.launchCallIntent(number: String) {
    Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel:$number")).apply(this::startActivity)
}

fun Context.hasBiometricCapability(): Boolean {
    return BiometricManager.from(this)
        .canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK) ==
        BiometricManager.BIOMETRIC_SUCCESS
}

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

fun Context.setScreenOrientation(orientation: Int) {
    val activity = this.findActivity() ?: return
    activity.requestedOrientation = orientation
    if (orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
        hideSystemUi()
    } else {
        showSystemUi()
    }
}

fun Context.hideSystemUi() {
    val activity = this.findActivity() ?: return
    val window = activity.window ?: return
    WindowCompat.setDecorFitsSystemWindows(window, false)
    WindowInsetsControllerCompat(window, window.decorView).let { controller ->
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }
}

fun Context.showSystemUi() {
    val activity = this.findActivity() ?: return
    val window = activity.window ?: return
    WindowCompat.setDecorFitsSystemWindows(window, true)
    WindowInsetsControllerCompat(
        window,
        window.decorView,
    ).show(WindowInsetsCompat.Type.systemBars())
}

fun Context.toPortrait() {
    setScreenOrientation(orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
}

fun Context.toLandscape() {
    setScreenOrientation(orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
}

fun Context.getNotificationManager(): NotificationManager {
    return getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
}

fun Context.notify(id: Int, notification: Notification) {
    getNotificationManager().notify(id, notification)
}
