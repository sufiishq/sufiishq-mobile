package pk.sufiishq.app.utils

import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import pk.sufiishq.app.SufiIshqApp
import pk.sufiishq.app.activities.BaseActivity
import pk.sufiishq.app.core.event.dispatcher.EventDispatcher
import pk.sufiishq.app.core.event.events.Event
import pk.sufiishq.app.core.event.handler.EventHandler
import pk.sufiishq.app.models.Kalam
import timber.log.Timber

fun app(): SufiIshqApp = SufiIshqApp.getInstance()

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
    id, title, code, recordedDate, location, onlineSource, offlineSource, isFavorite, playlistId
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
    return File(app().filesDir.absolutePath + File.separator + offlineSource).exists()
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

fun <T> LiveData<T>.asFlow(): Flow<T> = callbackFlow {
    val observer = Observer<T> { value -> this.trySend(value).isSuccess }
    observeForever(observer)
    awaitClose {
        removeObserver(observer)
    }
}.flowOn(Dispatchers.Main.immediate)

fun Kalam.offlineFile(): File? {
    return takeIf { offlineSource.isNotEmpty() }?.let {
        File(buildString {
            append(app().filesDir)
            append(File.separator)
            append(it.offlineSource)
        })
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
        BaseActivity.IS_DARK_THEME
    }
}

fun <T : Event> T.dispatch(vararg with: T) {

    // event dispatcher will throw an exception in preview mode
    EventDispatcher.getInstance().dispatch(this, *with)
}

fun <T : EventHandler> T.registerEventHandler() {
    EventDispatcher.getInstance().registerEventHandler(this)
}

@Composable
fun getCardBgColor(): Color {
    return if (isDarkThem()) {
        Color(34, 34, 34, 255)
    } else {
        Color(233, 233, 233, 255)
    }
}

@Composable
fun getCardFgColor(): Color {
    return if (isDarkThem()) {
        Color(247, 247, 247, 255)
    } else {
        Color(24, 24, 24, 255)
    }
}
