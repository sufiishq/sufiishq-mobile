package pk.sufiishq.app.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.*
import android.os.Build
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

fun Context.isNetworkAvailable(): Boolean {

    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        capabilities?.hasAnyOneTransport(TRANSPORT_CELLULAR, TRANSPORT_WIFI, TRANSPORT_ETHERNET)
            ?: false

    } else {
        try {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            activeNetworkInfo != null && activeNetworkInfo.isConnected
        } catch (e: Exception) {
            false
        }
    }
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

fun Context.getColorCompat(@ColorRes colorResId: Int): Int =
    ContextCompat.getColor(this, colorResId)

fun Context.dpToPx(dp: Float) = dp * resources.displayMetrics.density

fun Context.toast(text: String) = Toast.makeText(this, text, Toast.LENGTH_LONG).show()
