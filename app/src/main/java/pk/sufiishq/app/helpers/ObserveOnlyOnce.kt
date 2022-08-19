package pk.sufiishq.app.helpers

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

class ObserveOnlyOnce<T> {

    private var alreadyObserve = false

    fun take(lifecycleOwner: LifecycleOwner, source: LiveData<T>, observer: Observer<T>) {
        source.observe(lifecycleOwner) {
            if (!alreadyObserve) {
                alreadyObserve = true
                observer.onChanged(it)
            }
        }
    }
}