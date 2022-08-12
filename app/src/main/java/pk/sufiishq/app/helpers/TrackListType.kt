package pk.sufiishq.app.helpers

sealed class TrackListType(val type: String, val title: String) {
    class All : TrackListType(ScreenType.Tracks.ALL, "All Kalam")
    class Downloads : TrackListType(ScreenType.Tracks.DOWNLOADS, "Downloads")
    class Favorites : TrackListType(ScreenType.Tracks.FAVORITES, "Favorites")
    class Playlist(title: String, val playlistId: Int) :
        TrackListType(ScreenType.Tracks.PLAYLIST, title)
}