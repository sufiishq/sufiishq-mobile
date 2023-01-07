package pk.sufiishq.app.helpers

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.navArgument
import pk.sufiishq.app.R
import pk.sufiishq.app.utils.app

sealed class ScreenType(val route: String) {

    object Dashboard : ScreenType("screen_dashboard")
    object Playlist : ScreenType("screen_playlist")
    object Help : ScreenType("screen_help")
    object Tracks : ScreenType("screen_tracks") {

        // params
        private const val PARAM_TRACK_TYPE = "trackType"
        private const val PARAM_TITLE = "title"
        private const val PARAM_PLAYLIST_ID = "playlistId"

        // track list types
        const val ALL = "all"
        const val DOWNLOADS = "downloads"
        const val FAVORITES = "favorites"
        const val PLAYLIST = "playlist"

        fun routeWithPath(): String {
            return "$route/{$PARAM_TRACK_TYPE}/{$PARAM_TITLE}/{$PARAM_PLAYLIST_ID}"
        }

        fun navArgs(): List<NamedNavArgument> {
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

        fun getTrackListType(navBackStackEntry: NavBackStackEntry): TrackListType {

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
    }

    fun withArgs(vararg args: String): String {

        return buildString {
            append(route)

            args.forEach { arg ->
                append("/").append(arg)
            }
        }
    }
}
