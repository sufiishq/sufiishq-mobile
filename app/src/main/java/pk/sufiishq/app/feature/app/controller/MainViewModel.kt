/*
 * Copyright 2022-2023 SufiIshq
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pk.sufiishq.app.feature.app.controller

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import pk.sufiishq.app.di.qualifier.AppBarPopupMenuItems
import pk.sufiishq.app.feature.app.AppManager
import pk.sufiishq.app.feature.hijridate.model.HijriDate
import pk.sufiishq.app.feature.hijridate.repository.HijriDateRepository
import pk.sufiishq.app.feature.update.InAppUpdateManager
import pk.sufiishq.app.helpers.popupmenu.PopupMenu
import pk.sufiishq.aurora.models.DataMenuItem
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject
constructor(
    @AppBarPopupMenuItems private val popupMenu: PopupMenu,
    private val hijriDateRepository: HijriDateRepository,
    private val inAppUpdateManager: InAppUpdateManager,
    private val appManager: AppManager,
) : ViewModel(), MainController {

    private val showUpdateDialog = MutableLiveData(false)

    // -------------------------------------------------------------------- //
    // different controls
    // -------------------------------------------------------------------- //

    override fun popupMenuItems(): List<DataMenuItem> {
        return popupMenu.getPopupMenuItems()
    }

    override fun openFacebookGroup(context: Context, groupUrl: String) {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(groupUrl)))
    }

    override fun shareApp(activity: ComponentActivity) {
        appManager.shareApp(activity)
    }

    // -------------------------------------------------------------------- //
    // handle in-app updates
    // -------------------------------------------------------------------- //

    override fun checkUpdate(activity: ComponentActivity) {
        inAppUpdateManager.checkInAppUpdate(activity, this)
    }

    override fun showUpdateButton(value: Boolean) {
        showUpdateDialog.postValue(value)
    }

    override fun showUpdateButton(): LiveData<Boolean> {
        return showUpdateDialog
    }

    override fun handleUpdate() {
        inAppUpdateManager.startUpdateFlow()
    }

    override fun unregisterListener(activity: ComponentActivity) {
        inAppUpdateManager.unregisterListener(activity)
    }

    // -------------------------------------------------------------------- //
    // hijri date
    // -------------------------------------------------------------------- //

    override fun getHijriDate(): LiveData<HijriDate?> {
        return hijriDateRepository.getHijriDate().asLiveData()
    }
}
