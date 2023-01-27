package pk.sufiishq.app.helpers

import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDeepLink
import androidx.navigation.NavType
import androidx.navigation.navArgument
import pk.sufiishq.app.R
import pk.sufiishq.app.ui.screen.DashboardView
import pk.sufiishq.app.ui.screen.HelpView
import pk.sufiishq.app.ui.screen.PlaylistView
import pk.sufiishq.app.ui.screen.ThemeView
import pk.sufiishq.app.ui.screen.TracksView
import pk.sufiishq.app.utils.app

val ALL_SCREENS: List<ScreenType> = ScreenType::class
    .nestedClasses
    .toList()
    .map {
        it.objectInstance as ScreenType
    }

sealed interface ScreenType {
    fun route(): String
    fun arguments(): List<NamedNavArgument> = emptyList()
    fun deepLinks(): List<NavDeepLink> = emptyList()

    @Composable
    fun Compose(navController: NavController, navBackStackEntry: NavBackStackEntry)

    object Dashboard : ScreenType {
        override fun route() = "screen_dashboard"

        @Composable
        override fun Compose(navController: NavController, navBackStackEntry: NavBackStackEntry) {
            DashboardView(
                navController
            )
        }
    }

    object Playlist : ScreenType {
        override fun route() = "screen_playlist"

        @Composable
        override fun Compose(navController: NavController, navBackStackEntry: NavBackStackEntry) {
            PlaylistView(
                navController = navController
            )
        }
    }

    object Help : ScreenType {
        override fun route() = "screen_help"

        @Composable
        override fun Compose(navController: NavController, navBackStackEntry: NavBackStackEntry) {
            HelpView()
        }
    }

    object Tracks : ScreenType {

        // route
        private const val route = "screen_tracks"

        // params
        private const val PARAM_TRACK_TYPE = "trackType"
        private const val PARAM_TITLE = "title"
        private const val PARAM_PLAYLIST_ID = "playlistId"

        // track list types
        const val ALL = "all"
        const val DOWNLOADS = "downloads"
        const val FAVORITES = "favorites"
        const val PLAYLIST = "playlist"

        override fun route(): String {
            return "$route/{$PARAM_TRACK_TYPE}/{$PARAM_TITLE}/{$PARAM_PLAYLIST_ID}"
        }

        private fun getTrackType(navBackStackEntry: NavBackStackEntry): String {
            return navBackStackEntry.arguments?.getString(PARAM_TRACK_TYPE) ?: ALL
        }

        private fun getTitle(navBackStackEntry: NavBackStackEntry): String {
            return navBackStackEntry.arguments?.getString(PARAM_TITLE)
                ?: app().getString(R.string.all)
        }

        private fun getPlaylistId(navBackStackEntry: NavBackStackEntry): Int {
            return navBackStackEntry.arguments?.getInt(PARAM_PLAYLIST_ID) ?: 0
        }

        private fun getTrackListType(navBackStackEntry: NavBackStackEntry): TrackListType {

            val trackType = getTrackType(navBackStackEntry)
            val title = getTitle(navBackStackEntry)
            val playlistId = getPlaylistId(navBackStackEntry)

            return when (trackType) {
                DOWNLOADS -> TrackListType.Downloads()
                FAVORITES -> TrackListType.Favorites()
                PLAYLIST -> TrackListType.Playlist(title, playlistId)
                else -> TrackListType.All()
            }
        }

        fun withArgs(vararg args: String): String {

            return buildString {
                append(route)

                args.forEach { arg ->
                    append("/").append(arg)
                }
            }
        }

        override fun arguments(): List<NamedNavArgument> {
            return listOf(
                navArgument(PARAM_TRACK_TYPE) {
                    type = NavType.StringType
                },
                navArgument(PARAM_TITLE) {
                    type = NavType.StringType
                },
                navArgument(PARAM_PLAYLIST_ID) {
                    type = NavType.IntType
                    defaultValue = 0
                }
            )
        }

        @Composable
        override fun Compose(navController: NavController, navBackStackEntry: NavBackStackEntry) {
            TracksView(
                trackListType = getTrackListType(navBackStackEntry),
            )
        }
    }

    object Theme : ScreenType {
        override fun route() = "screen_theme"

        @Composable
        override fun Compose(navController: NavController, navBackStackEntry: NavBackStackEntry) {
            ThemeView()
        }
    }
}
