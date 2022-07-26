package pk.sufiishq.app.utils

import androidx.compose.runtime.State
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
    onlineSource: String = this.onlineSource,
    offlineSource: String? = this.offlineSource,
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

