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
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import pk.sufiishq.app.di.qualifier.AppBarPopupMenuItems
import pk.sufiishq.app.di.qualifier.IoDispatcher
import pk.sufiishq.app.feature.app.AppManager
import pk.sufiishq.app.feature.events.data.repository.EventRepository
import pk.sufiishq.app.feature.events.model.Event
import pk.sufiishq.app.feature.hijridate.model.HijriDate
import pk.sufiishq.app.feature.hijridate.repository.HijriDateRepository
import pk.sufiishq.app.feature.update.AppUpdateCheckManager
import pk.sufiishq.app.helpers.popupmenu.PopupMenu
import pk.sufiishq.aurora.models.DataMenuItem
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class MainViewModel
@Inject
constructor(
    @IoDispatcher private val dispatcher: CoroutineContext,
    @AppBarPopupMenuItems private val popupMenu: PopupMenu,
    private val hijriDateRepository: HijriDateRepository,
    private val appUpdateCheckManager: AppUpdateCheckManager,
    private val appManager: AppManager,
    private val eventRepository: EventRepository,
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

    override fun getUpcomingEvents(): LiveData<List<Event>> {
        val upcomingEventLiveData = MutableLiveData<List<Event>>()
        viewModelScope.launch(dispatcher) {
            upcomingEventLiveData.postValue(eventRepository.getUpcomingEvents())
        }
        return upcomingEventLiveData
    }

    // -------------------------------------------------------------------- //
    // handle in-app updates
    // -------------------------------------------------------------------- //

    override fun checkUpdate(activity: ComponentActivity) {
        appUpdateCheckManager.checkInAppUpdate(activity, this)
    }

    override fun showUpdateDialog(value: Boolean) {
        showUpdateDialog.postValue(value)
    }

    override fun showUpdateDialog(): LiveData<Boolean> {
        return showUpdateDialog
    }

    override fun handleUpdate(context: Context) {
        appUpdateCheckManager.routeToPlayStore(context)
    }

    // -------------------------------------------------------------------- //
    // hijri date
    // -------------------------------------------------------------------- //

    override fun getHijriDate(): LiveData<HijriDate?> {
        return hijriDateRepository.getHijriDate().asLiveData()
    }
}
