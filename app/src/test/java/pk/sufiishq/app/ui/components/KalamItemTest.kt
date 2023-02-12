package pk.sufiishq.app.ui.components

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performClick
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.slot
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import pk.sufiishq.app.SufiIshqTest
import pk.sufiishq.app.core.event.dispatcher.EventDispatcher
import pk.sufiishq.app.core.event.events.Event
import pk.sufiishq.app.core.event.events.PlayerEvents
import pk.sufiishq.app.helpers.TrackListType
import pk.sufiishq.app.utils.fakeKalamDataProvider
import pk.sufiishq.app.utils.fakePlayerDataProvider
import pk.sufiishq.app.utils.formatDateAs

class KalamItemTest : SufiIshqTest() {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setUp() {

        composeTestRule.setContent {
            KalamItem(
                kalam = getKalam(),
                trackListType = TrackListType.All,
                kalamDataProvider = fakeKalamDataProvider(),
                playerDataProvider = fakePlayerDataProvider()
            )
        }
    }

    @Ignore("will be fixed later")
    @Test
    fun `test dispatch ChangeTrack even when kalam item gets clicked`() {
        mockkObject(EventDispatcher)

        val eventSlot = slot<Event>()
        every { EventDispatcher.dispatch(capture(eventSlot)) } returns Unit

        // kalam item should be exists, visible and has click action
        getKalamItemNode().assertExists().assertIsDisplayed().performClick()

       /* with(eventSlot.captured as PlayerEvents.ChangeTrack) {
            verify { EventDispatcher.dispatch(this@with) }

            assertEquals(1, kalam.id)
            assertEquals(TrackListType.All.title, trackListType.title)
            assertEquals(TrackListType.All.type, trackListType.type)
        }*/
    }

    @Ignore("will be fixed later")
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

    @Ignore("will be fixed later")
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