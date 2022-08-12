package pk.sufiishq.app.helpers

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import javax.inject.Inject

class ObserveOnlyOnce<T> @Inject constructor() {

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