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
import androidx.activity.ComponentActivity
import androidx.lifecycle.LiveData
import pk.sufiishq.app.feature.events.model.Event
import pk.sufiishq.app.feature.hijridate.model.HijriDate
import pk.sufiishq.aurora.models.DataMenuItem

interface MainController {

    // -------------------------------------------------------------------- //
    // signature for different controls
    // -------------------------------------------------------------------- //

    fun popupMenuItems(): List<DataMenuItem>
    fun openFacebookGroup(context: Context, groupUrl: String)
    fun shareApp(activity: ComponentActivity)
    fun getUpcomingEvents(): LiveData<List<Event>>

    // -------------------------------------------------------------------- //
    // signature in-app update controls
    // -------------------------------------------------------------------- //

    fun checkUpdate(activity: ComponentActivity)
    fun showUpdateDialog(): LiveData<Boolean>
    fun showUpdateDialog(value: Boolean)
    fun handleUpdate(context: Context)

    // -------------------------------------------------------------------- //
    // signature hijri date widget
    // -------------------------------------------------------------------- //

    fun getHijriDate(): LiveData<HijriDate?>
}
