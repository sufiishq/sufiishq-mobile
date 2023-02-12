package pk.sufiishq.app.viewmodels

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import pk.sufiishq.app.core.app.AppManager
import pk.sufiishq.app.data.providers.MainDataProvider
import pk.sufiishq.app.di.qualifier.AppBarPopupMenuItems
import pk.sufiishq.app.core.update.InAppUpdateManager
import pk.sufiishq.app.data.repository.HijriDateRepository
import pk.sufiishq.app.helpers.popupmenu.PopupMenu
import pk.sufiishq.app.models.HijriDate
import pk.sufiishq.aurora.models.DataMenuItem

@HiltViewModel
class MainViewModel @Inject constructor(
    @AppBarPopupMenuItems private val popupMenu: PopupMenu,
    private val hijriDateRepository: HijriDateRepository,
    private val inAppUpdateManager: InAppUpdateManager,
    private val appManager: AppManager
) : ViewModel(), MainDataProvider {

    private val showUpdateDialog = MutableLiveData(false)

    override fun checkUpdate(activity: ComponentActivity) {
        inAppUpdateManager.checkInAppUpdate(activity, this)
    }

    override fun showUpdateButton(value: Boolean) = showUpdateDialog.postValue(value)
    override fun showUpdateButton(): LiveData<Boolean> = showUpdateDialog
    override fun handleUpdate() = inAppUpdateManager.startUpdateFlow()

    override fun unregisterListener(activity: ComponentActivity) {
        inAppUpdateManager.unregisterListener(activity)
    }

    override fun popupMenuItems(): List<DataMenuItem> = popupMenu.getPopupMenuItems()

    override fun openFacebookGroup(context: Context, groupUrl: String) {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(groupUrl)))
    }

    override fun shareApp(activity: ComponentActivity) {
        appManager.shareApp(activity)
    }

    // -------------------------------------------------------------------- //
    // hijri date
    // -------------------------------------------------------------------- //

    override fun getHijriDate(): LiveData<HijriDate?> {
        return hijriDateRepository.getHijriDate().asLiveData()
    }
}