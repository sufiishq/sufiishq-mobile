package pk.sufiishq.app.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import pk.sufiishq.app.R
import pk.sufiishq.app.data.providers.KalamDataProvider
import pk.sufiishq.app.data.providers.PlayerDataProvider
import pk.sufiishq.app.data.providers.PlaylistDataProvider
import pk.sufiishq.app.helpers.Screen
import pk.sufiishq.app.ui.screen.DashboardView
import pk.sufiishq.app.ui.screen.PlaylistView
import pk.sufiishq.app.ui.screen.TracksView

@Composable
fun NavigationHost(
    playerDataProvider: PlayerDataProvider,
    kalamDataProvider: KalamDataProvider,
    playlistDataProvider: PlaylistDataProvider,
    navController: NavHostController
) {

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        NavHost(navController = navController, startDestination = Screen.Dashboard.route) {

            // dashboard screen
            composable(Screen.Dashboard.route) {

                DashboardView(
                    navController = navController,
                    kalamDataProvider,
                    playlistDataProvider
                )
            }

            // tracks screen
            composable(
                Screen.Tracks.route + "/{${Screen.Tracks.PARAM_TRACK_TYPE}}/{${Screen.Tracks.PARAM_TITLE}}/{${Screen.Tracks.PARAM_PLAYLIST_ID}}",
                arguments = listOf(
                    navArgument(Screen.Tracks.PARAM_TRACK_TYPE) {
                        type = NavType.StringType
                    },
                    navArgument(Screen.Tracks.PARAM_TITLE) {
                        type = NavType.StringType
                    },
                    navArgument(Screen.Tracks.PARAM_PLAYLIST_ID) {
                        type = NavType.IntType
                        defaultValue = 0
                    }
                )
            ) { backStackEntry ->

                val trackType = backStackEntry.arguments?.getString(Screen.Tracks.PARAM_TRACK_TYPE)
                    ?: Screen.Tracks.ALL
                val title = backStackEntry.arguments?.getString(Screen.Tracks.PARAM_TITLE)
                    ?: stringResource(id = R.string.all)
                val playlistId =
                    backStackEntry.arguments?.getInt(Screen.Tracks.PARAM_PLAYLIST_ID) ?: 0

                TracksView(
                    playerDataProvider = playerDataProvider,
                    kalamDataProvider = kalamDataProvider,
                    playlistDataProvider = playlistDataProvider,
                    trackType = trackType,
                    title = title,
                    playlistId = playlistId,
                )
            }

            // playlist screen
            composable(Screen.Playlist.route) {
                PlaylistView(
                    playlistDataProvider = playlistDataProvider,
                    navController = navController
                )
            }
        }
    }

}