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

package pk.sufiishq.app.data.controller

import androidx.activity.ComponentActivity
import androidx.lifecycle.LiveData
import pk.sufiishq.app.core.firebase.AuthState
import pk.sufiishq.app.core.firebase.HighlightStatus
import pk.sufiishq.app.models.Maintenance
import java.util.Calendar

interface AdminController {

    // -------------------------------------------------------------------- //
    // signatures for base functionality
    // -------------------------------------------------------------------- //

    fun showLoader(): LiveData<Boolean>
    fun showLoader(isShow: Boolean)
    fun showSnackbar(message: String?)
    fun showSnackbar(): LiveData<String?>

    // -------------------------------------------------------------------- //
    // signatures authentication
    // -------------------------------------------------------------------- //

    fun checkAuthentication(): LiveData<AuthState>
    fun signIn(activity: ComponentActivity)
    fun signOut(activity: ComponentActivity)

    // -------------------------------------------------------------------- //
    // signatures for manage highlight
    // -------------------------------------------------------------------- //

    fun highlightStatus(): LiveData<HighlightStatus?>
    fun deleteHighlight()
    fun startDateChanged(calendar: Calendar)
    fun endDateChanged(calendar: Calendar)
    fun startDate(): LiveData<Calendar>
    fun endDate(): LiveData<Calendar>
    fun minEndDate(): LiveData<Calendar>
    fun startTimeChanged(calendar: Calendar)
    fun endTimeChanged(calendar: Calendar)
    fun startTime(): LiveData<Calendar>
    fun endTime(): LiveData<Calendar>
    fun getTitle(): LiveData<String?>
    fun getDetail(): LiveData<String?>
    fun getContacts(): LiveData<MutableList<Pair<String, String>?>>
    fun setTitle(newTitle: String)
    fun setDetail(newDetail: String)
    fun fetchHighlight()
    fun addOrUpdateHighlight()

    // -------------------------------------------------------------------- //
    // signatures for update and read maintenance
    // -------------------------------------------------------------------- //

    fun setMaintenanceStatus(status: Boolean)
    fun setMaintenanceStrict(mode: Boolean)
    fun getMaintenance(): LiveData<Maintenance?>
}
