package pk.sufiishq.app.ui.components

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import dagger.hilt.android.testing.HiltTestApplication
import io.mockk.every
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import pk.sufiishq.app.R
import pk.sufiishq.app.SufiIshqTest
import pk.sufiishq.app.helpers.TrackListType
import pk.sufiishq.app.ui.screen.tracks.KalamItemPopupMenu
import pk.sufiishq.app.utils.fakeKalamController

class KalamItemPopupMenuTest : SufiIshqTest() {

    @get:Rule
    val composeTestRule = createComposeRule()

    private var app = mockApp()
    private var menuItems = getAllMenuItems()

    @Before
    fun setUp() {
        every { app.getString(any()) } answers {
            ApplicationProvider.getApplicationContext<HiltTestApplication>().getString(firstArg())
        }
    }

    @Ignore("will be fixed later")
    @Test
    fun `test dropdown menu should be exists and displayed`() {
        setContent()

        getDropdownMenuNode().assertExists().assertIsDisplayed()
    }

    @Ignore("will fixed later")
    @Test
    fun `test dispatch ShowPlayListDialog event when add to play list menu item clicked`() {

        val isExpanded = mutableStateOf(true)
        setContent(
            isExpanded = isExpanded,
            trackListType = TrackListType.Downloads()
        )

        /*menuItems
            .zip(
                listOf(
                    PlayerEvents.ShowPlaylistDialog::class.java,
                    KalamEvents.MarkAsFavoriteKalam::class.java,
                    KalamEvents.RemoveFavoriteKalam::class.java,
                    PlayerEvents.StartDownload::class.java,
                    KalamEvents.ShowKalamSplitManagerDialog::class.java,
                    KalamEvents.ShowKalamRenameDialog::class.java,
                    KalamEvents.ShareKalam::class.java,
                    KalamEvents.ShowKalamConfirmDeleteDialog::class.java
                )
            )
            .onEach { entry ->
                isExpanded.value = true

                // perform click on add to playlist menu item
                performClickOnMenuItem(entry.first)

                // menu bar should not be visible
                getDropdownMenuNode().assertDoesNotExist()

                // verify dispatched event should be correct
                verify { eventDispatcher.dispatch(entry.second.cast(eventSlot.captured)) }
            } */
    }

    private fun performClickOnMenuItem(testTag: String) {
        findNode(testTag)
            .assertExists()
            .assertHasClickAction()
            .performClick()
    }

    private fun getDropdownMenuNode(): SemanticsNodeInteraction {
        return composeTestRule.onNode(hasTestTag("dropdown_menu"))
    }

    private fun findNode(testTag: String): SemanticsNodeInteraction {
        return composeTestRule.onNode(hasTestTag(testTag))
    }

    private fun setContent(
        isExpanded: MutableState<Boolean> = mutableStateOf(true),
        trackListType: TrackListType = TrackListType.All()
    ) {
        composeTestRule.setContent {
            KalamItemPopupMenu(
                isExpanded = isExpanded,
                kalam = getKalam(),
                kalamController = fakeKalamController(),
                trackListType = trackListType
            )
        }
    }

    private fun getAllMenuItems() = listOf(
        R.string.menu_item_add_to_playlist,
        R.string.menu_item_mark_as_favorite,
        R.string.menu_item_remove_favorite,
        R.string.menu_item_download,
        R.string.menu_item_split_kalam,
        R.string.menu_item_rename,
        R.string.menu_item_share,
        R.string.menu_item_delete
    ).map {
        getString(it)
    }

    private fun getString(resourceId: Int): String {
        return ApplicationProvider.getApplicationContext<HiltTestApplication>()
            .getString(resourceId)
    }
}