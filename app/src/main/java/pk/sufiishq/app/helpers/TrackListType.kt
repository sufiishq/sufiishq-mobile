package pk.sufiishq.app.helpers

import pk.sufiishq.app.R
import pk.sufiishq.app.utils.getString

sealed class TrackListType(val type: String, val title: String) {
    class All(title: String = getString(R.string.title_all_kalam)) :
        TrackListType(ScreenType.Tracks.ALL, title)

    class Downloads(title: String = getString(R.string.title_downloads)) :
        TrackListType(ScreenType.Tracks.DOWNLOADS, title)

    class Favorites(title: String = getString(R.string.title_favorites)) :
        TrackListType(ScreenType.Tracks.FAVORITES, title)

    class Playlist(title: String, val playlistId: Int) :
        TrackListType(ScreenType.Tracks.PLAYLIST, title)
}

fun String.createTrackListType(
    playlistTitle: String? = null,
    playlistId: Int? = null
): TrackListType {
    return when (this) {
        ScreenType.Tracks.ALL -> TrackListType.All()
        ScreenType.Tracks.DOWNLOADS -> TrackListType.Downloads()
        ScreenType.Tracks.FAVORITES -> TrackListType.Favorites()
        ScreenType.Tracks.PLAYLIST -> TrackListType.Playlist(playlistTitle!!, playlistId!!)
        else -> throw IllegalArgumentException("type: $this is not a valid argument")
    }
}