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
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import pk.sufiishq.app.StringRes
import pk.sufiishq.app.SufiIshqTest
import pk.sufiishq.app.di.qualifier.AppBarPopupMenuItems
import pk.sufiishq.app.feature.app.AppManager
import pk.sufiishq.app.feature.hijridate.model.HijriDate
import pk.sufiishq.app.feature.hijridate.repository.HijriDateRepository
import pk.sufiishq.app.feature.update.AppUpdateCheckManager
import pk.sufiishq.app.helpers.popupmenu.PopupMenu
import pk.sufiishq.app.helpers.popupmenu.PopupMenuItem
import pk.sufiishq.app.utils.getOrAwaitValue
import pk.sufiishq.app.utils.getString
import javax.inject.Inject

@HiltAndroidTest
class MainControllerTest : SufiIshqTest() {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    @AppBarPopupMenuItems
    lateinit var popupMenu: PopupMenu

    private val hijriDateRepository = mockk<HijriDateRepository>()
    private val appUpdateCheckManager = mockk<AppUpdateCheckManager>()
    private val appManager = mockk<AppManager>()
    private lateinit var mainViewModel: MainViewModel

    @Before
    fun setUp() {
        hiltRule.inject()
        mainViewModel =
            MainViewModel(popupMenu, hijriDateRepository, appUpdateCheckManager, appManager)
    }

    @Test
    fun testPopupMenuItems_shouldReturn_appBarPopupMenuItems() {
        mainViewModel
            .popupMenuItems()
            .sortedBy {
                it.label
            }
            .zip(
                listOf(
                    PopupMenuItem.Share(getString(StringRes.menu_item_share)),
                    PopupMenuItem.Facebook(getString(StringRes.menu_item_facebook)),
                    PopupMenuItem.Help(getString(StringRes.menu_item_help)),
                    PopupMenuItem.Theme(getString(StringRes.menu_item_theme)),
                ).sortedBy {
                    it.label
                },
            )
            .forEach {
                assertEquals(it.first.label, it.second.label)
                assertEquals(it.first.resId, it.second.resId)
            }
    }

    @Test
    fun testOpenFacebookGroup_shouldStart_intentActionActivity() {
        val context = mockk<Context>()
        val groupUrl = "https://www.imnotavailable.thankyou"
        val intentSlot = slot<Intent>()

        every { context.startActivity(capture(intentSlot)) } returns Unit
        mainViewModel.openFacebookGroup(context, groupUrl)

        verify { context.startActivity(intentSlot.captured) }
        assertEquals(Intent.ACTION_VIEW, intentSlot.captured.action)
        assertEquals(Uri.parse(groupUrl).toString(), intentSlot.captured.data.toString())
    }

    @Test
    fun testShareApp_shouldDelegate_toAppManager() {
        val activity = mockk<ComponentActivity>()
        every { appManager.shareApp(any()) } returns Unit

        mainViewModel.shareApp(activity)
        verify { appManager.shareApp(activity) }
    }

    @Test
    fun testCheckUpdate_shouldDelegate_toInAppUpdateManager() {
        val activity = mockk<ComponentActivity>()
        every { appUpdateCheckManager.checkInAppUpdate(any(), any()) } returns Unit

        mainViewModel.checkUpdate(activity)
        verify { appUpdateCheckManager.checkInAppUpdate(activity, mainViewModel) }
    }

    @Test
    fun testShowUpdateButton_shouldReturn_trueLiveData() {
        mainViewModel.showUpdateDialog(true)
        assertTrue(mainViewModel.showUpdateDialog().getOrAwaitValue()!!)
    }

    @Test
    fun testHandleUpdate_shouldDelegate_toInAppUpdateManager() {
        every { appUpdateCheckManager.routeToPlayStore() } returns Unit

        mainViewModel.handleUpdate()
        verify { appUpdateCheckManager.routeToPlayStore() }
    }

    @Test
    fun testUnregisterListener_shouldDelegate_toInAppUpdateManager() {
        val activity = mockk<ComponentActivity>()
        every { appUpdateCheckManager.unregisterListener(any()) } returns Unit

        mainViewModel.unregisterListener(activity)
        verify { appUpdateCheckManager.unregisterListener(activity) }
    }

    @Test
    fun testGetHijriDate_shouldReturn_nonNullHijriDate() {
        val hijriDate = HijriDate("01", "01", "-", "1444")
        every { hijriDateRepository.getHijriDate() } returns flow {
            emit(hijriDate)
        }.flowOn(Dispatchers.Main.immediate)

        mainViewModel.getHijriDate().getOrAwaitValue()?.apply {
            assertEquals("01", day)
            assertEquals("01", monthEn)
            assertEquals("-", monthAr)
            assertEquals("1444", year)
        }
    }
}
