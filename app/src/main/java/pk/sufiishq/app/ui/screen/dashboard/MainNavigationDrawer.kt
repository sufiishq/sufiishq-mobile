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

package pk.sufiishq.app.ui.screen.dashboard

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.github.esentsov.PackagePrivate
import kotlinx.coroutines.launch
import pk.sufiishq.app.feature.app.controller.MainController
import pk.sufiishq.app.feature.app.model.NavigationItem
import pk.sufiishq.app.helpers.ScreenType
import pk.sufiishq.app.ui.components.widgets.HijriDate
import pk.sufiishq.app.utils.ImageRes
import pk.sufiishq.app.utils.TextRes
import pk.sufiishq.app.utils.getString
import pk.sufiishq.aurora.components.SIDivider
import pk.sufiishq.aurora.layout.SIColumn
import pk.sufiishq.aurora.widgets.SIDataRow

@PackagePrivate
@Composable
fun MainNavigationDrawer(
    scaffoldState: ScaffoldState,
    mainController: MainController,
    navigationItems: List<NavigationItem>,
    navController: NavController,
) {
    val scope = rememberCoroutineScope()

    HijriDate(mainController)

    SIColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 6.dp, top = 6.dp, end = 12.dp, bottom = 6.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        navigationItems.forEach {
            SIDataRow(
                title = it.title,
                leadingIcon = it.resId,
                rowHeight = 48,
                onClick = {
                    scope.launch {
                        scaffoldState.drawerState.close()
                        navController.navigate(it.route)
                    }
                },
            )
        }
        SIDivider(
            topSpace = 6,
            bottomSpace = 6,
        )
        getSecondaryMenu().forEach {
            SIDataRow(
                title = it.title,
                leadingIcon = it.resId,
                rowHeight = 48,
                onClick = {
                    scope.launch {
                        scaffoldState.drawerState.close()
                        navController.navigate(it.route)
                    }
                },
            )
        }
    }
}

private fun getSecondaryMenu(): List<NavigationItem> {
    return listOf(
        NavigationItem(
            getString(TextRes.menu_item_theme),
            ImageRes.themes,
            ScreenType.Theme.buildRoute(),
        ),
        NavigationItem(
            getString(TextRes.menu_item_app_lock),
            ImageRes.app_lock,
            ScreenType.AppLock.buildRoute(),
        ),
        NavigationItem(
            getString(TextRes.menu_item_help),
            ImageRes.help,
            ScreenType.Help.buildRoute(),
        ),
        NavigationItem(
            getString(TextRes.menu_item_admin_setting),
            ImageRes.setting,
            ScreenType.AdminSettings.buildRoute(),
        ),
    )
}
