package pk.sufiishq.app.helpers

sealed class ScreenType(val route: String) {

    object Dashboard : ScreenType("screen_dashboard")
    object Playlist : ScreenType("screen_playlist")
    object Tracks : ScreenType("screen_tracks") {

        // params
        const val PARAM_TRACK_TYPE = "trackType"
        const val PARAM_TITLE = "title"
        const val PARAM_PLAYLIST_ID = "playlistId"

        // track list types
        const val ALL = "all"
        const val DOWNLOADS = "downloads"
        const val FAVORITES = "favorites"
        const val PLAYLIST = "playlist"

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
