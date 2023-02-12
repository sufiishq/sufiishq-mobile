package pk.sufiishq.app.data.providers

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.lifecycle.LiveData
import pk.sufiishq.app.models.HijriDate
import pk.sufiishq.aurora.models.DataMenuItem

interface MainDataProvider {

    fun checkUpdate(activity: ComponentActivity)
    fun showUpdateButton(): LiveData<Boolean>
    fun showUpdateButton(value: Boolean)
    fun popupMenuItems(): List<DataMenuItem>
    fun openFacebookGroup(context: Context, groupUrl: String)
    fun shareApp(activity: ComponentActivity)
    fun handleUpdate()
    fun unregisterListener(activity: ComponentActivity)

    // -------------------------------------------------------------------- //
    // signature hijri date widget
    // -------------------------------------------------------------------- //

    fun getHijriDate(): LiveData<HijriDate?>
}