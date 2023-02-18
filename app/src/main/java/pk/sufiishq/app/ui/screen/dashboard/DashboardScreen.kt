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
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import pk.sufiishq.app.BuildConfig
import pk.sufiishq.app.R
import pk.sufiishq.app.annotations.ExcludeFromJacocoGeneratedReport
import pk.sufiishq.app.core.app.controller.DashboardController
import pk.sufiishq.app.core.app.controller.MainController
import pk.sufiishq.app.helpers.ScreenType
import pk.sufiishq.app.core.admin.model.Highlight
import pk.sufiishq.app.utils.fakeDashboardController
import pk.sufiishq.app.utils.fakeMainController
import pk.sufiishq.app.utils.optString
import pk.sufiishq.app.utils.optValue
import pk.sufiishq.app.utils.rem
import pk.sufiishq.app.core.app.controller.DashboardViewModel
import pk.sufiishq.app.core.app.controller.MainViewModel
import pk.sufiishq.aurora.components.SIBadge
import pk.sufiishq.aurora.components.SIImage
import pk.sufiishq.aurora.layout.SIBox
import pk.sufiishq.aurora.layout.SIColumn
import pk.sufiishq.aurora.layout.SIConstraintLayout
import pk.sufiishq.aurora.layout.SIRow
import pk.sufiishq.aurora.layout.SIScaffold
import pk.sufiishq.aurora.theme.AuroraDark
import pk.sufiishq.aurora.theme.AuroraLight

@Composable
fun DashboardScreen(
    navController: NavController,
    mainController: MainController = hiltViewModel<MainViewModel>(),
    dashboardController: DashboardController = hiltViewModel<DashboardViewModel>(),
) {
    val all = rem(stringResource(R.string.title_all_kalam))
    val favorites = rem(stringResource(R.string.title_favorites))
    val downloads = rem(stringResource(R.string.title_downloads))
    val playlist = rem(stringResource(R.string.title_playlist))
    val scaffoldState = rememberScaffoldState()

    SIScaffold(
        drawer = {
            MainNavigationDrawer(
                scaffoldState = scaffoldState,
                mainController = mainController,
                navigationItems = dashboardController.getMainNavigationItems(),
                navController = navController,
            )
        },
        drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
        scaffoldState = scaffoldState,
    ) {
        // highlight available dialog
        val showHighlightDialog = rem<Highlight?>(null)

        HighlightAvailableDialog(
            showDialog = showHighlightDialog,
        )

        SIBox {
            UpdateAvailableDialog(
                isUpdateAvailable =
                rem(
                    mainController.showUpdateButton().observeAsState().optValue(false),
                ),
                mainController = mainController,
            )

            SIConstraintLayout(
                modifier = Modifier.fillMaxSize(),
            ) {
                val (logo, calligraphy, highlightAvailableButton, buttonBox, debugLabel) = createRefs()

                MainAnimatedLogo(
                    modifier =
                    Modifier.constrainAs(logo) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top, 12.dp)
                        end.linkTo(parent.end)
                        bottom.linkTo(calligraphy.top)
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    },
                )

                SIImage(
                    modifier =
                    Modifier.constrainAs(calligraphy) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(buttonBox.top)
                    },
                    resId = R.drawable.caligraphi,
                )

                HighlightAvailableButton(
                    modifier =
                    Modifier.constrainAs(highlightAvailableButton) {
                        start.linkTo(parent.start, 12.dp)
                        bottom.linkTo(buttonBox.top)
                    },
                    highlightDialogControl = showHighlightDialog,
                    dashboardController = dashboardController,
                )

                if (BuildConfig.DEBUG) {
                    SIBadge(
                        text = optString(R.string.label_debug),
                        modifier =
                        Modifier.constrainAs(debugLabel) {
                            end.linkTo(parent.end, 12.dp)
                            bottom.linkTo(buttonBox.top)
                        },
                    )
                }

                SIBox(
                    modifier =
                    Modifier.padding(start = 12.dp, top = 12.dp, end = 12.dp, bottom = 18.dp)
                        .constrainAs(buttonBox) {
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                        },
                ) {
                    SIRow {
                        SIColumn(
                            modifier = Modifier.weight(1f),
                        ) {
                            DashboardButton(
                                title = all.value,
                                count = dashboardController.countAll().observeAsState().optValue(0),
                                icon = R.drawable.round_check_circle_24,
                                paddingModifier = Modifier.padding(0.dp, 0.dp, 6.dp, 6.dp),
                                navigate = {
                                    navController.navigate(
                                        ScreenType.Tracks.buildRoute(
                                            ScreenType.Tracks.ALL,
                                            all.value,
                                            "0",
                                        ),
                                    )
                                },
                            )

                            DashboardButton(
                                title = favorites.value,
                                count = dashboardController.countFavorites().observeAsState().optValue(0),
                                icon = R.drawable.round_favorite_24,
                                paddingModifier = Modifier.padding(0.dp, 6.dp, 6.dp, 0.dp),
                                navigate = {
                                    navController.navigate(
                                        ScreenType.Tracks.buildRoute(
                                            ScreenType.Tracks.FAVORITES,
                                            favorites.value,
                                            "0",
                                        ),
                                    )
                                },
                            )
                        }

                        SIColumn(
                            modifier = Modifier.weight(1f),
                        ) {
                            DashboardButton(
                                title = downloads.value,
                                count = dashboardController.countDownloads().observeAsState().optValue(0),
                                icon = R.drawable.round_cloud_download_24,
                                paddingModifier = Modifier.padding(6.dp, 0.dp, 0.dp, 6.dp),
                                navigate = {
                                    navController.navigate(
                                        ScreenType.Tracks.buildRoute(
                                            ScreenType.Tracks.DOWNLOADS,
                                            downloads.value,
                                            "0",
                                        ),
                                    )
                                },
                            )

                            DashboardButton(
                                title = playlist.value,
                                count = dashboardController.countPlaylist().observeAsState().optValue(0),
                                icon = R.drawable.round_format_list_bulleted_24,
                                paddingModifier = Modifier.padding(6.dp, 6.dp, 0.dp, 0.dp),
                                navigate = { navController.navigate(ScreenType.Playlist.buildRoute()) },
                            )
                        }
                    }

                    // navigation menu button
                    MainNavigationButton(scaffoldState = scaffoldState)
                }
            }
        }
    }
}

@ExcludeFromJacocoGeneratedReport
@Preview(showBackground = true)
@Composable
fun LightPreviewDashboardView() {
    AuroraLight {
        DashboardScreen(
            navController = rememberNavController(),
            mainController = fakeMainController(),
            dashboardController = fakeDashboardController(),
        )
    }
}

@ExcludeFromJacocoGeneratedReport
@Preview(showBackground = true)
@Composable
fun DarkPreviewDashboardView() {
    AuroraDark {
        DashboardScreen(
            navController = rememberNavController(),
            mainController = fakeMainController(),
            dashboardController = fakeDashboardController(),
        )
    }
}
