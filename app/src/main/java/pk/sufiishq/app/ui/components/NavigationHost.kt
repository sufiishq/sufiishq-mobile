package pk.sufiishq.app.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import pk.sufiishq.app.data.providers.HomeDataProvider
import pk.sufiishq.app.data.providers.KalamDataProvider
import pk.sufiishq.app.data.providers.PlaylistDataProvider
import pk.sufiishq.app.helpers.GlobalEventHandler
import pk.sufiishq.app.helpers.ScreenType
import pk.sufiishq.app.ui.screen.DashboardView
import pk.sufiishq.app.ui.screen.HelpView
import pk.sufiishq.app.ui.screen.PlaylistView
import pk.sufiishq.app.ui.screen.TracksView
import pk.sufiishq.app.utils.app

@Composable
fun NavigationHost(
    kalamDataProvider: KalamDataProvider,
    playlistDataProvider: PlaylistDataProvider,
    homeDataProvider: HomeDataProvider,
    globalEventHandler: GlobalEventHandler,
    navController: NavHostController
) {


    val appConfig = app().appConfig

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        NavHost(navController = navController, startDestination = ScreenType.Dashboard.route) {

            // dashboard screen
            composable(ScreenType.Dashboard.route) {

                DashboardView(
                    navController,
                    homeDataProvider,
                    globalEventHandler,
                    appConfig
                )
            }

            // tracks screen
            composable(
                ScreenType.Tracks.routeWithPath(),
                arguments = ScreenType.Tracks.navArgs()
            ) { backStackEntry ->

                TracksView(
                    kalamDataProvider = kalamDataProvider,
                    trackListType = ScreenType.Tracks.getTrackListType(backStackEntry),
                )
            }

            // playlist screen
            composable(ScreenType.Playlist.route) {
                PlaylistView(
                    playlistDataProvider = playlistDataProvider,
                    navController = navController
                )
            }

            // help screen
            composable(ScreenType.Help.route) {
                HelpView()
            }
        }
    }

}