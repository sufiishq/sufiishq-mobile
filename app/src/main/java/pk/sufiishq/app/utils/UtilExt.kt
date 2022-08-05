package pk.sufiishq.app.utils

import android.content.Context
import androidx.compose.runtime.*
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import pk.sufiishq.app.SufiIshqApp
import pk.sufiishq.app.models.Kalam
import timber.log.Timber
import java.io.File

val app: SufiIshqApp = SufiIshqApp.getInstance()

fun <T> State<T?>.optValue(default: T) = value ?: default

fun Kalam.copyWithDefaults(
    id: Int = this.id,
    title: String = this.title,
    code: Int = this.code,
    year: String = this.year,
    location: String = this.location,
    onlineSource: String = this.onlineSource,
    offlineSource: String = this.offlineSource,
    isFavorite: Int = this.isFavorite,
    playlistId: Int = this.playlistId
) = Kalam(id, title, code, year, location, onlineSource, offlineSource, isFavorite, playlistId)

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

@Composable
fun <T> rem(value: T): MutableState<T> {
    return remember { mutableStateOf(value) }
}

