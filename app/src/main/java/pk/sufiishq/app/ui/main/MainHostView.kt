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

package pk.sufiishq.app.ui.main

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import pk.sufiishq.app.R
import pk.sufiishq.app.feature.admin.maintenance.MaintenanceManager
import pk.sufiishq.app.feature.applock.AppLockManager
import pk.sufiishq.app.feature.player.controller.AudioPlayer
import pk.sufiishq.app.ui.components.ContentBackground
import pk.sufiishq.app.ui.main.player.PlayerView
import pk.sufiishq.app.ui.main.topbar.AboutIconButton
import pk.sufiishq.app.ui.main.topbar.AppBarOverflowMenu
import pk.sufiishq.aurora.layout.SIScaffold
import pk.sufiishq.aurora.widgets.SITopAppBar

@Composable
fun MainHostView(
    maintenanceManager: MaintenanceManager,
    appLockManager: AppLockManager,
    audioPlayer: AudioPlayer,
) {
    val navController = rememberNavController()

    SIScaffold(
        topBar = {
            SITopAppBar(
                navigationIcon = { fgColor -> AboutIconButton(fgColor) },
                actions = { fgColor -> AppBarOverflowMenu(navController, fgColor) },
                centerDrawable = R.drawable.hp_logo,
            )
        },
        bottomBar = { PlayerView() },
    ) {
        ContentBackground { NavigationHost(navController) }
    }

    AppLockAndMaintenance(maintenanceManager, appLockManager, audioPlayer)
}
