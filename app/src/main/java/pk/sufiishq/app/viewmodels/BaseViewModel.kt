package pk.sufiishq.app.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import pk.sufiishq.app.core.event.events.Event
import pk.sufiishq.app.core.event.handler.EventHandler

open class BaseViewModel(app: Application) : AndroidViewModel(app), EventHandler {

    override fun onEvent(event: Event) {}
}