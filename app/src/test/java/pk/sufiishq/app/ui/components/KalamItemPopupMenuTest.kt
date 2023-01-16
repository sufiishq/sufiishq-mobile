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
import io.mockk.slot
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import pk.sufiishq.app.R
import pk.sufiishq.app.SufiIshqTest
import pk.sufiishq.app.core.event.events.Event
import pk.sufiishq.app.core.event.events.KalamEvents
import pk.sufiishq.app.core.event.events.PlayerEvents
import pk.sufiishq.app.helpers.TrackListType

class KalamItemPopupMenuTest : SufiIshqTest() {

    @get:Rule
    val composeTestRule = createComposeRule()

    private var app = mockApp()
    private var eventDispatcher = mockEventDispatcher()
    private var menuItems = getAllMenuItems()

    @Before
    fun setUp() {
        every { app.getString(any()) } answers {
            ApplicationProvider.getApplicationContext<HiltTestApplication>().getString(firstArg())
        }
    }

    @Test
    fun `test dropdown menu should be exists and displayed`() {
        setContent()

        getDropdownMenuNode().assertExists().assertIsDisplayed()
    }

    @Test
    fun `test dispatch ShowPlayListDialog event when add to play list menu item clicked`() {

        val isExpanded = mutableStateOf(true)
        setContent(
            isExpanded = isExpanded,
            trackListType = TrackListType.Downloads()
        )

        val eventSlot = slot<Event>()
        every { eventDispatcher.dispatch(capture(eventSlot)) } returns Unit

        menuItems
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
            }
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
                kalamMenuItems = menuItems,
                trackListType = trackListType
            )
        }
    }

    private fun getAllMenuItems() = listOf(
        R.string.add_to_playlist,
        R.string.mark_as_favorite,
        R.string.remove_favorite,
        R.string.download_label,
        R.string.split_kalam,
        R.string.rename_label,
        R.string.share_label,
        R.string.delete_label
    ).map {
        getString(it)
    }

    private fun getString(resourceId: Int): String {
        return ApplicationProvider.getApplicationContext<HiltTestApplication>()
            .getString(resourceId)
    }
}