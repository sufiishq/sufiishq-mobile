package pk.sufiishq.app.utils

import androidx.compose.runtime.*
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import pk.sufiishq.app.models.Kalam

fun <T> State<T?>.optValue(default: T) = value ?: default

fun Kalam.copyAsNew(
    id: Int = this.id,
    title: String = this.title,
    year: String = this.year,
    location: String = this.location,
    onlineSource: String = this.onlineSource
) = Kalam(id, title, code, year, location, onlineSource)

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

@Composable
fun <T> rem(value: T): MutableState<T> {
    return remember { mutableStateOf(value) }
}

