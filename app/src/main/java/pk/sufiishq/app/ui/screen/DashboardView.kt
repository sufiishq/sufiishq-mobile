package pk.sufiishq.app.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import pk.sufiishq.app.data.providers.GlobalDataProvider
import pk.sufiishq.app.data.providers.HomeDataProvider
import pk.sufiishq.app.helpers.ScreenType
import pk.sufiishq.app.ui.components.SIAnimatedLog
import pk.sufiishq.app.ui.components.buttons.DashboardButton
import pk.sufiishq.app.ui.components.dialogs.UpdateAvailableDialog
import pk.sufiishq.app.utils.dummyGlobalDataProvider
import pk.sufiishq.app.utils.dummyHomeDataProvider
import pk.sufiishq.app.utils.optValue
import pk.sufiishq.app.utils.rem
import pk.sufiishq.app.viewmodels.GlobalViewModel
import pk.sufiishq.app.viewmodels.HomeViewModel
import pk.sufiishq.aurora.components.SIBadge
import pk.sufiishq.aurora.components.SIImage
import pk.sufiishq.aurora.layout.SIBox
import pk.sufiishq.aurora.layout.SIColumn
import pk.sufiishq.aurora.layout.SIConstraintLayout
import pk.sufiishq.aurora.layout.SIRow
import pk.sufiishq.aurora.theme.AuroraDark
import pk.sufiishq.aurora.theme.AuroraLight

@Composable
fun DashboardView(
    navController: NavController,
    homeDataProvider: HomeDataProvider = hiltViewModel<HomeViewModel>(),
    globalDataProvider: GlobalDataProvider = hiltViewModel<GlobalViewModel>()
) {

    val all = rem(stringResource(R.string.all))
    val favorites = rem(stringResource(R.string.favorites))
    val downloads = rem(stringResource(R.string.downloads))
    val playlist = rem(stringResource(R.string.playlist))

    SIBox {

        UpdateAvailableDialog(
            isUpdateAvailable = rem(
                globalDataProvider.getShowUpdateButton().observeAsState().optValue(false)
            )
        )

        SIConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {

            val (logo, calligraphy, buttonBox, debugLabel) = createRefs()

            SIAnimatedLog(
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

            if (BuildConfig.DEBUG) {
                SIBadge(
                    text = "DEBUG",
                    modifier = Modifier.constrainAs(debugLabel) {
                        end.linkTo(parent.end, 12.dp)
                        bottom.linkTo(buttonBox.top)
                    }
                )
            }

            SIBox(
                modifier = Modifier
                    .constrainAs(buttonBox) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    },
                padding = 12
            ) {
                SIRow {
                    SIColumn(
                        modifier = Modifier.weight(1f)
                    ) {

                        DashboardButton(
                            title = all.value,
                            count = homeDataProvider.countAll().observeAsState().optValue(0),
                            icon = R.drawable.ic_outline_check_circle_24,
                            paddingModifier = Modifier.padding(0.dp, 0.dp, 6.dp, 6.dp),
                            navigate = {
                                navController.navigate(
                                    ScreenType.Tracks.withArgs(
                                        ScreenType.Tracks.ALL,
                                        all.value,
                                        "0"
                                    )
                                )
                            }
                        )

                        DashboardButton(
                            title = favorites.value,
                            count = homeDataProvider.countFavorites().observeAsState()
                                .optValue(0),
                            icon = R.drawable.ic_round_favorite_border_24,
                            paddingModifier = Modifier.padding(0.dp, 6.dp, 6.dp, 0.dp),
                            navigate = {
                                navController.navigate(
                                    ScreenType.Tracks.withArgs(
                                        ScreenType.Tracks.FAVORITES,
                                        favorites.value,
                                        "0"
                                    )
                                )
                            }
                        )
                    }

                    Column(
                        modifier = Modifier
                            .weight(1f)
                    ) {

                        DashboardButton(
                            title = downloads.value,
                            count = homeDataProvider.countDownloads().observeAsState()
                                .optValue(0),
                            icon = R.drawable.ic_outline_cloud_download_24,
                            paddingModifier = Modifier.padding(6.dp, 0.dp, 0.dp, 6.dp),
                            navigate = {
                                navController.navigate(
                                    ScreenType.Tracks.withArgs(
                                        ScreenType.Tracks.DOWNLOADS,
                                        downloads.value,
                                        "0"
                                    )
                                )
                            }
                        )

                        DashboardButton(
                            title = playlist.value,
                            count = homeDataProvider.countPlaylist().observeAsState()
                                .optValue(0),
                            icon = R.drawable.ic_outline_playlist_play_24,
                            paddingModifier = Modifier.padding(6.dp, 6.dp, 0.dp, 0.dp),
                            navigate = {
                                navController.navigate(ScreenType.Playlist.route())
                            }
                        )
                    }
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
        DashboardView(
            navController = rememberNavController(),
            homeDataProvider = dummyHomeDataProvider(),
            globalDataProvider = dummyGlobalDataProvider()
        )
    }
}

@ExcludeFromJacocoGeneratedReport
@Preview(showBackground = true)
@Composable
fun DarkPreviewDashboardView() {
    AuroraDark {
        DashboardView(
            navController = rememberNavController(),
            homeDataProvider = dummyHomeDataProvider(),
            globalDataProvider = dummyGlobalDataProvider()
        )
    }
}
