package pk.sufiishq.app.ui.components

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performClick
import androidx.navigation.NavController
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import pk.sufiishq.app.SufiIshqTest
import pk.sufiishq.app.configs.AppConfig
import pk.sufiishq.app.helpers.ScreenType
import pk.sufiishq.app.models.Playlist

class PlaylistItemTest : SufiIshqTest() {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val app = mockApp()
    private val appConfig = AppConfig()

    @Before
    fun setUp() {
        every { app.appConfig } returns appConfig
    }

    @Ignore("will be fixed later")
    @Test
    fun `test should be navigated to track screen when playlist item clicked`() {

        val navController = mockk<NavController>()
        val playlist = getPlaylist()

        val routeSlot = slot<String>()
        every { navController.navigate(capture(routeSlot)) } returns Unit

        setContent(
            playlist = playlist,
            navController = navController
        )

        // playlist item should be exists, visible and has click action
        getPlaylistItemNode().assertExists().assertIsDisplayed().performClick()

        val route = ScreenType.Tracks.withArgs(
            "playlist",
            "${playlist.title} Playlist",
            "${playlist.id}"
        )

        verify { navController.navigate(route) }

        assertEquals(route, routeSlot.captured)
    }

    @Ignore("will be fixed later")
    @Test
    fun `test leading icon should be exist and displayed`() {
        setContent()
        getLeadingIconNode().assertExists().assertIsDisplayed()
    }

    @Test
    fun `test should be exists and displayed playlist title and total number of kalam nodes`() {
        val playlist = getPlaylist()

        setContent(playlist)

        findTextNode(playlist.title).assertExists().assertIsDisplayed()
        findTextNode("Total ${playlist.totalKalam} kalams").assertExists().assertIsDisplayed()
    }

    private fun getPlaylistItemNode(): SemanticsNodeInteraction {
        return composeTestRule.onNode(hasTestTag("playlist_item"))
    }

    private fun getLeadingIconNode(): SemanticsNodeInteraction {
        return composeTestRule.onNode(hasTestTag("leading_icon"), useUnmergedTree = true)
    }

    private fun findTextNode(text: String): SemanticsNodeInteraction {
        return composeTestRule.onNode(hasText(text), useUnmergedTree = true)
    }

    private fun setContent(
        playlist: Playlist = getPlaylist(),
        navController: NavController = mockk()
    ) {
        composeTestRule.setContent {
            PlaylistItem(
                playlist = playlist,
                navController = navController,
                popupMenuItems = listOf()
            )
        }
    }

    private fun getPlaylist() = Playlist(
        id = 1,
        title = "Mix"
    )
}