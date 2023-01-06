package pk.sufiishq.app.core.event.events

import android.content.Context
import pk.sufiishq.app.helpers.GlobalEventHandler
import pk.sufiishq.app.models.Kalam

abstract class GlobalEvents : Event(GlobalEventHandler::class) {

    class ShareKalam(val kalam: Kalam, val context: Context) : GlobalEvents()
    class ShareApp(val context: Context) : GlobalEvents()
    class ShowUpdateButton(val isShow: Boolean) : GlobalEvents()
    class OpenFacebookGroup(val context: Context, val groupUrl: String) : GlobalEvents()
    object StartUpdateFlow : GlobalEvents()
}