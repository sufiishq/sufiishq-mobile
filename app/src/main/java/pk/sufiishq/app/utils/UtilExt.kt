package pk.sufiishq.app.utils

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.*
import androidx.core.net.toUri
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import pk.sufiishq.app.R
import pk.sufiishq.app.SufiIshqApp
import pk.sufiishq.app.activities.MainActivity
import pk.sufiishq.app.models.Kalam
import timber.log.Timber
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

val app: SufiIshqApp = SufiIshqApp.getInstance()

fun <T> State<T?>.optValue(default: T) = value ?: default

fun Kalam.copyWithDefaults(
    id: Int = this.id,
    title: String = this.title,
    code: Int = this.code,
    recordedDate: String = this.recordeDate,
    location: String = this.location,
    onlineSource: String = this.onlineSource,
    offlineSource: String = this.offlineSource,
    isFavorite: Int = this.isFavorite,
    playlistId: Int = this.playlistId
) = Kalam(
    id,
    title,
    code,
    recordedDate,
    location,
    onlineSource,
    offlineSource,
    isFavorite,
    playlistId
)

fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
    observe(lifecycleOwner, object : Observer<T> {
        override fun onChanged(t: T?) {
            observer.onChanged(t)
            removeObserver(this)
        }
    })
}

fun String.checkValue(ifNotEmpty: String, ifEmpty: String) = if (isEmpty()) ifEmpty else ifNotEmpty

fun String?.ifNotEmpty(run: (text: String) -> Unit) {
    this?.let {
        if (it.trim().isNotEmpty()) run(it.trim())
    }
}

fun Kalam?.hasOfflineSource() = this?.offlineSource?.isNotEmpty() ?: false

fun Kalam?.canPlay(context: Context): Boolean {
    return if (!this.hasOfflineSource() && !context.isNetworkAvailable()) {
        context.toast("Network not available")
        Timber.e("Track Error: Network not available")
        false
    } else true
}

fun <T> LiveData<T>.optValue(default: T): T {
    return value ?: default
}

fun Kalam.isOfflineFileExists(): Boolean {
    return File(app.filesDir.absolutePath + File.separator + offlineSource).exists()
}

fun isDeviceSupportDarkMode(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
}

fun String.formatDateAs(format: String = "d MMM, yyyy", prefix: String = ""): String {
    return when {
        this.isEmpty() -> ""
        this.length == 4 -> prefix + this
        else -> {
            val date = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse(this)
            prefix + SimpleDateFormat(format, Locale.getDefault()).format(date!!)
        }
    }
}

fun Number.runWithDelay(block: () -> Unit) {
    Handler(Looper.myLooper()!!).postDelayed(block, this.toLong())
}

fun Kalam.share(context: Context, linkCreated: (status: Boolean) -> Unit) {
    if (context is MainActivity) {

        FirebaseDynamicLinks.getInstance()
            .createDynamicLink()
            .setLink("$DEEPLINK_HOST/kalam/$id".toUri())
            .setDomainUriPrefix(DEEPLINK_HOST)
            .setAndroidParameters(DynamicLink.AndroidParameters.Builder().build())
            .buildShortDynamicLink()
            .addOnSuccessListener { task ->
                linkCreated(true)
                val appName = context.getString(R.string.app_name)
                Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_SUBJECT, title)
                    putExtra(Intent.EXTRA_TEXT, task.shortLink.toString())
                }.also {
                    context.startActivity(Intent.createChooser(it, "Share $appName"))
                }
            }
            .addOnFailureListener {
                linkCreated(false)
                if (!context.isNetworkAvailable()) {
                    context.toast(context.getString(R.string.no_network_connection))
                } else {
                    context.toast(it.message ?: context.getString(R.string.unknown_error))
                }
                Timber.e(it)
            }
    }
}

@Composable
fun <T> rem(value: T): MutableState<T> {
    return remember { mutableStateOf(value) }
}

@Composable
fun isDarkThem(): Boolean {
    return if (isDeviceSupportDarkMode()) {
        isSystemInDarkTheme()
    } else {
        MainActivity.IS_DARK_THEME
    }
}

