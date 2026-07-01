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

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import pk.sufiishq.app.helpers.MainScreens
import pk.sufiishq.app.helpers.ScreenType
import pk.sufiishq.aurora.layout.SIColumn
import pk.sufiishq.aurora.layout.SIScaffold
import pk.sufiishq.aurora.theme.AuroraColor
import pk.sufiishq.aurora.theme.getForegroundColor

@Composable
fun NavigationHost(
    navController: NavHostController,
) {
    val snackbarHostState: SnackbarHostState = remember {
        SnackbarHostState()
    }

    SIScaffold(
        snackbarHostState = snackbarHostState,
        snackbarHost = {
            SnackbarHost(it) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = AuroraColor.SecondaryContainer.color(),
                    contentColor = AuroraColor.SecondaryContainer.getForegroundColor(AuroraColor.SecondaryContainer.color())
                        .color(),
                )
            }
        },
    ) {
        SIColumn(modifier = Modifier.fillMaxSize()) {
            NavHost(
                navController = navController,
                startDestination = ScreenType.Dashboard.buildRoute(),
            ) {
                MainScreens.onEach { screen ->
                    composable(screen.buildRoute(), screen.arguments(), screen.deepLinks()) {
                        screen.Compose(navController, it, snackbarHostState)
                    }
                }
            }
        }
    }
}
