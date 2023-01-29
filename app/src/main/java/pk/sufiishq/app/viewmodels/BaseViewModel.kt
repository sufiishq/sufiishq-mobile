package pk.sufiishq.app.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import pk.sufiishq.app.core.event.handler.EventHandler
import pk.sufiishq.app.utils.registerEventHandler

abstract class BaseViewModel(app: Application) : AndroidViewModel(app), EventHandler {

    init {
        registerEventHandler()
    }

}