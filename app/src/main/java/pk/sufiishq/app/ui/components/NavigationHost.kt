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
import pk.sufiishq.app.core.event.dispatcher.EventDispatcher
import pk.sufiishq.app.data.providers.HomeDataProvider
import pk.sufiishq.app.data.providers.KalamDataProvider
import pk.sufiishq.app.data.providers.PlaylistDataProvider
import pk.sufiishq.app.helpers.GlobalEventHandler
import pk.sufiishq.app.helpers.ScreenType
import pk.sufiishq.app.helpers.TrackListType
import pk.sufiishq.app.ui.screen.DashboardView
import pk.sufiishq.app.ui.screen.PlaylistView
import pk.sufiishq.app.ui.screen.TracksView

@Composable
fun NavigationHost(
    eventDispatcher: EventDispatcher,
    kalamDataProvider: KalamDataProvider,
    playlistDataProvider: PlaylistDataProvider,
    homeDataProvider: HomeDataProvider,
    globalEventHandler: GlobalEventHandler,
    navController: NavHostController
) {

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
                    eventDispatcher
                )
            }

            // tracks screen
            composable(
                ScreenType.Tracks.route + "/{${ScreenType.Tracks.PARAM_TRACK_TYPE}}/{${ScreenType.Tracks.PARAM_TITLE}}/{${ScreenType.Tracks.PARAM_PLAYLIST_ID}}",
                arguments = listOf(
                    navArgument(ScreenType.Tracks.PARAM_TRACK_TYPE) {
                        type = NavType.StringType
                    },
                    navArgument(ScreenType.Tracks.PARAM_TITLE) {
                        type = NavType.StringType
                    },
                    navArgument(ScreenType.Tracks.PARAM_PLAYLIST_ID) {
                        type = NavType.IntType
                        defaultValue = 0
                    }
                )
            ) { backStackEntry ->

                val trackType =
                    backStackEntry.arguments?.getString(ScreenType.Tracks.PARAM_TRACK_TYPE)
                        ?: ScreenType.Tracks.ALL
                val title = backStackEntry.arguments?.getString(ScreenType.Tracks.PARAM_TITLE)
                    ?: stringResource(id = R.string.all)
                val playlistId =
                    backStackEntry.arguments?.getInt(ScreenType.Tracks.PARAM_PLAYLIST_ID) ?: 0

                val trackListType = when (trackType) {
                    ScreenType.Tracks.DOWNLOADS -> TrackListType.Downloads()
                    ScreenType.Tracks.FAVORITES -> TrackListType.Favorites()
                    ScreenType.Tracks.PLAYLIST -> TrackListType.Playlist(title, playlistId)
                    else -> TrackListType.All()
                }

                TracksView(
                    eventDispatcher = eventDispatcher,
                    kalamDataProvider = kalamDataProvider,
                    trackListType = trackListType,
                )
            }

            // playlist screen
            composable(ScreenType.Playlist.route) {
                PlaylistView(
                    playlistDataProvider = playlistDataProvider,
                    navController = navController
                )
            }
        }
    }

}