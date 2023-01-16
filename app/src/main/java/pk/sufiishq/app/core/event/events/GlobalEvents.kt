package pk.sufiishq.app.core.event.events

import android.content.Context
import pk.sufiishq.app.viewmodels.GlobalViewModel

abstract class GlobalEvents : Event(GlobalViewModel::class) {

    class ShareApp(val context: Context) : GlobalEvents()
    class ShowUpdateButton(val isShow: Boolean) : GlobalEvents()
    class OpenFacebookGroup(val context: Context, val groupUrl: String) : GlobalEvents()
    object StartUpdateFlow : GlobalEvents()
}