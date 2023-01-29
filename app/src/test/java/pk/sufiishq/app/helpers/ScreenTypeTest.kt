package pk.sufiishq.app.helpers

import androidx.core.os.bundleOf
import androidx.navigation.NavBackStackEntry
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.robolectric.util.ReflectionHelpers
import org.robolectric.util.ReflectionHelpers.callInstanceMethod
import pk.sufiishq.app.SufiIshqTest

class ScreenTypeTest : SufiIshqTest() {

    @Test
    fun test_dashboard_shouldVerify_allMethods() {
        val dashboardType = ScreenType.Dashboard

        assertEquals("screen_dashboard", dashboardType.route())
        assertEquals(0, dashboardType.arguments().size)
        assertEquals(0, dashboardType.deepLinks().size)
    }

    @Test
    fun test_playlist_shouldVerify_allMethods() {
        val playlistType = ScreenType.Playlist

        assertEquals("screen_playlist", playlistType.route())
        assertEquals(0, playlistType.arguments().size)
        assertEquals(0, playlistType.deepLinks().size)
    }

    @Test
    fun test_help_shouldVerify_allMethods() {
        val helpType = ScreenType.Help

        assertEquals("screen_help", helpType.route())
        assertEquals(0, helpType.arguments().size)
        assertEquals(0, helpType.deepLinks().size)
    }

    @Test
    fun test_tracks_shouldVerify_allMethods() {
        val tracksType = ScreenType.Tracks

        assertEquals("screen_tracks/{trackType}/{title}/{playlistId}", tracksType.route())
        assertEquals(0, tracksType.deepLinks().size)


        val args = tracksType.arguments()
        assertEquals(3, args.size)
        assertEquals("trackType", args[0].name)
        assertEquals("title", args[1].name)
        assertEquals("playlistId", args[2].name)

        val withArgs = tracksType.withArgs(
            ScreenType.Tracks.FAVORITES,
            "Favorites",
            "0"
        )

        assertEquals("screen_tracks/favorites/Favorites/0", withArgs)

        val tracksListTypeAll =
            getTrackListType(tracksType, getNavBackStackEntry("all", "All Kalam"))
        assertTrue(tracksListTypeAll is TrackListType.All)

        val tracksListTypeDownload =
            getTrackListType(tracksType, getNavBackStackEntry("downloads", "Downloads"))
        assertTrue(tracksListTypeDownload is TrackListType.Downloads)

        val tracksListTypeFavorite =
            getTrackListType(tracksType, getNavBackStackEntry("favorites", "Favorites"))
        assertTrue(tracksListTypeFavorite is TrackListType.Favorites)

        val tracksListTypePlaylist =
            getTrackListType(tracksType, getNavBackStackEntry("playlist", "Faisalabad", 10))
        assertTrue(tracksListTypePlaylist is TrackListType.Playlist)
        assertEquals("Faisalabad", tracksListTypePlaylist.title)
        assertEquals(10, (tracksListTypePlaylist as TrackListType.Playlist).playlistId)
    }

    private fun getNavBackStackEntry(
        trackType: String,
        title: String,
        playlistId: Int = 0
    ): NavBackStackEntry {
        return mockk {
            every { arguments } returns bundleOf(
                "trackType" to trackType,
                "title" to title,
                "playlistId" to playlistId
            )
        }
    }

    private fun getTrackListType(
        tracksType: ScreenType.Tracks,
        navBackStackEntry: NavBackStackEntry
    ): TrackListType {
        return callInstanceMethod(
            tracksType, "getTrackListType", ReflectionHelpers.ClassParameter(
                NavBackStackEntry::class.java,
                navBackStackEntry
            )
        )
    }
}