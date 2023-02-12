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
import pk.sufiishq.app.data.providers.DashboardDataProvider
import pk.sufiishq.app.data.providers.MainDataProvider
import pk.sufiishq.app.helpers.ScreenType
import pk.sufiishq.app.models.Highlight
import pk.sufiishq.app.utils.fakeDashboardDataProvider
import pk.sufiishq.app.utils.fakeMainDataProvider
import pk.sufiishq.app.utils.optString
import pk.sufiishq.app.utils.optValue
import pk.sufiishq.app.utils.rem
import pk.sufiishq.app.viewmodels.DashboardViewModel
import pk.sufiishq.app.viewmodels.MainViewModel
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
    mainDataProvider: MainDataProvider = hiltViewModel<MainViewModel>(),
    dashboardDataProvider: DashboardDataProvider = hiltViewModel<DashboardViewModel>()
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
                mainDataProvider = mainDataProvider,
                navigationItems = dashboardDataProvider.getMainNavigationItems(),
                navController = navController
            )
        },
        drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
        scaffoldState = scaffoldState
    ) {

        // highlight available dialog
        val showHighlightDialog = rem<Highlight?>(null)

        HighlightAvailableDialog(
            showDialog = showHighlightDialog
        )

        SIBox {

            UpdateAvailableDialog(
                isUpdateAvailable = rem(
                    mainDataProvider.showUpdateButton().observeAsState().optValue(false)
                ),
                mainDataProvider = mainDataProvider
            )

            SIConstraintLayout(
                modifier = Modifier.fillMaxSize()
            ) {

                val (logo, calligraphy, highlightAvailableButton, buttonBox, debugLabel) = createRefs()

                MainAnimatedLogo(
                    modifier = Modifier.constrainAs(logo) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top, 12.dp)
                        end.linkTo(parent.end)
                        bottom.linkTo(calligraphy.top)
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    }
                )

                SIImage(
                    modifier = Modifier.constrainAs(calligraphy) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(buttonBox.top)
                    },
                    resId = R.drawable.caligraphi
                )

                HighlightAvailableButton(
                    modifier = Modifier.constrainAs(highlightAvailableButton) {
                        start.linkTo(parent.start, 12.dp)
                        bottom.linkTo(buttonBox.top)
                    },
                    highlightDialogControl = showHighlightDialog,
                    dashboardDataProvider = dashboardDataProvider
                )

                if (BuildConfig.DEBUG) {
                    SIBadge(
                        text = optString(R.string.label_debug),
                        modifier = Modifier.constrainAs(debugLabel) {
                            end.linkTo(parent.end, 12.dp)
                            bottom.linkTo(buttonBox.top)
                        }
                    )
                }

                SIBox(
                    modifier = Modifier
                        .padding(start = 12.dp, top = 12.dp, end = 12.dp, bottom = 18.dp)
                        .constrainAs(buttonBox) {
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                        },
                ) {
                    SIRow {
                        SIColumn(
                            modifier = Modifier.weight(1f)
                        ) {

                            DashboardButton(
                                title = all.value,
                                count = dashboardDataProvider.countAll().observeAsState()
                                    .optValue(0),
                                icon = R.drawable.ic_outline_check_circle_24,
                                paddingModifier = Modifier.padding(0.dp, 0.dp, 6.dp, 6.dp),
                                navigate = {
                                    navController.navigate(
                                        ScreenType.Tracks.buildRoute(
                                            ScreenType.Tracks.ALL,
                                            all.value,
                                            "0"
                                        )
                                    )
                                }
                            )

                            DashboardButton(
                                title = favorites.value,
                                count = dashboardDataProvider.countFavorites().observeAsState()
                                    .optValue(0),
                                icon = R.drawable.ic_round_favorite_border_24,
                                paddingModifier = Modifier.padding(0.dp, 6.dp, 6.dp, 0.dp),
                                navigate = {
                                    navController.navigate(
                                        ScreenType.Tracks.buildRoute(
                                            ScreenType.Tracks.FAVORITES,
                                            favorites.value,
                                            "0"
                                        )
                                    )
                                }
                            )
                        }

                        SIColumn(
                            modifier = Modifier
                                .weight(1f)
                        ) {

                            DashboardButton(
                                title = downloads.value,
                                count = dashboardDataProvider.countDownloads().observeAsState()
                                    .optValue(0),
                                icon = R.drawable.ic_outline_cloud_download_24,
                                paddingModifier = Modifier.padding(6.dp, 0.dp, 0.dp, 6.dp),
                                navigate = {
                                    navController.navigate(
                                        ScreenType.Tracks.buildRoute(
                                            ScreenType.Tracks.DOWNLOADS,
                                            downloads.value,
                                            "0"
                                        )
                                    )
                                }
                            )

                            DashboardButton(
                                title = playlist.value,
                                count = dashboardDataProvider.countPlaylist().observeAsState()
                                    .optValue(0),
                                icon = R.drawable.ic_outline_playlist_play_24,
                                paddingModifier = Modifier.padding(6.dp, 6.dp, 0.dp, 0.dp),
                                navigate = {
                                    navController.navigate(ScreenType.Playlist.buildRoute())
                                }
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
            mainDataProvider = fakeMainDataProvider(),
            dashboardDataProvider = fakeDashboardDataProvider()
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
            mainDataProvider = fakeMainDataProvider(),
            dashboardDataProvider = fakeDashboardDataProvider()
        )
    }
}
