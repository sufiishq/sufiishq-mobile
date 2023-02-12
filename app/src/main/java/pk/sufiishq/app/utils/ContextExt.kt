package pk.sufiishq.app.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.TRANSPORT_CELLULAR
import android.net.NetworkCapabilities.TRANSPORT_ETHERNET
import android.net.NetworkCapabilities.TRANSPORT_WIFI
import android.net.Uri
import android.widget.Toast
import java.io.IOException
import java.util.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
    CoroutineScope(Dispatchers.Main).launch {
        Toast.makeText(this@toast, text, toastLength).show()
    }

fun Context.assetsToBitmap(fileName: String): Bitmap? {
    return try {
        with(assets.open(fileName)) {
            BitmapFactory.decodeStream(this)
        }
    } catch (e: IOException) {
        null
    }
}

fun Context.launchGoogleMap(latLng: Pair<Double, Double>) {
    val uri: String = String.format(
        Locale.ENGLISH,
        "geo:%f,%f?z=17",
        latLng.first,
        latLng.second
    )
    Intent(Intent.ACTION_VIEW, Uri.parse(uri)).apply {
        startActivity(this)
    }
}