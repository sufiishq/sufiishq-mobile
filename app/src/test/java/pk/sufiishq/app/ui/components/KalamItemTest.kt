package pk.sufiishq.app.ui.components

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performClick
import io.mockk.every
import io.mockk.slot
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import pk.sufiishq.app.SufiIshqTest
import pk.sufiishq.app.core.event.events.Event
import pk.sufiishq.app.core.event.events.PlayerEvents
import pk.sufiishq.app.helpers.TrackListType
import pk.sufiishq.app.utils.formatDateAs

class KalamItemTest : SufiIshqTest() {

    @get:Rule
    val composeTestRule = createComposeRule()

    private var eventDispatcher = mockEventDispatcher()

    @Before
    fun setUp() {

        composeTestRule.setContent {
            KalamItem(
                kalam = getKalam(),
                trackListType = TrackListType.All(),
                kalamMenuItems = listOf()
            )
        }
    }

    @Test
    fun `test dispatch ChangeTrack even when kalam item gets clicked`() {

        val eventSlot = slot<Event>()
        every { eventDispatcher.dispatch(capture(eventSlot)) } returns Unit

        // kalam item should be exists, visible and has click action
        getKalamItemNode().assertExists().assertIsDisplayed().performClick()

        with(eventSlot.captured as PlayerEvents.ChangeTrack) {
            verify { eventDispatcher.dispatch(this@with) }

            assertEquals(1, kalam.id)
            assertEquals(TrackListType.All().title, trackListType.title)
            assertEquals(TrackListType.All().type, trackListType.type)
        }
    }

    @Test
    fun `test leading icon should be exists and visible`() {
        getLeadingIconNode().assertExists().assertIsDisplayed()
    }

    @Test
    fun `test kalam title text should be exists and displayed`() {
        getKalamTitleNode().assertExists().assertIsDisplayed()
    }

    @Test
    fun `test kalam meta info text should be exists and displayed`() {
        getKalamMetaInfoNode().assertExists().assertIsDisplayed()
    }

    @Test
    fun `test kalam item overflow menu button should be exists, visible and has click action`() {
        getKalamOverflowMenuButton().assertExists().assertIsDisplayed().assertHasClickAction()
    }

    private fun getKalamItemNode(): SemanticsNodeInteraction {
        return composeTestRule.onNode(hasTestTag("kalam_item"))
    }

    private fun getLeadingIconNode(): SemanticsNodeInteraction {
        return composeTestRule.onNode(hasTestTag("leading_icon"), useUnmergedTree = true)
    }

    private fun getKalamTitleNode(): SemanticsNodeInteraction {
        return composeTestRule.onNode(hasText(getKalam().title), useUnmergedTree = true)
    }

    private fun getKalamMetaInfoNode(): SemanticsNodeInteraction {
        return composeTestRule.onNode(
            hasText(
                "${getKalam().location} ${
                    getKalam().recordeDate.formatDateAs(
                        prefix = "- "
                    )
                }"
            ), useUnmergedTree = true
        )
    }

    private fun getKalamOverflowMenuButton(): SemanticsNodeInteraction {
        return composeTestRule.onNode(hasTestTag("overflow_menu_button"), useUnmergedTree = true)
    }
}