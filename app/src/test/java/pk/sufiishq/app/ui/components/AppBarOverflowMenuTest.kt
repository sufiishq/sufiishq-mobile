package pk.sufiishq.app.ui.components

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.performClick
import io.mockk.every
import io.mockk.slot
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import pk.sufiishq.app.SufiIshqTest
import pk.sufiishq.app.core.event.events.Event
import pk.sufiishq.app.core.event.events.GlobalEvents

class AppBarOverflowMenuTest : SufiIshqTest() {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `test overflow menu button should be exists and visible by default`() {

        composeTestRule.setContent {
            AppBarOverflowMenu()
        }

        // acquire overflow menu button node
        getOverflowMenuButtonNode()

            // should be exists
            .assertExists()

            // should be visible
            .assertIsDisplayed()

            // must has click action
            .assertHasClickAction()
    }

    @Test
    fun `test dropdown menu should be visible when overflow menu button clicked`() {

        composeTestRule.setContent {
            AppBarOverflowMenu()
        }

        // acquire dropdown menu node
        val dropdownMenuNode = getDropdownMenuNode()

        // dropdown should not be visible by default
        dropdownMenuNode.assertDoesNotExist()

        // show popup menu items
        getOverflowMenuButtonNode().performClick()

        // verify all menu item existence and visibility
        dropdownMenuNode.assertExists().assertIsDisplayed()
    }

    @Test
    fun `test dispatch even should be as ShareApp when ShareApp menu item clicked`() {

        composeTestRule.setContent {
            AppBarOverflowMenu()
        }

        val eventDispatcher = mockEventDispatcher()

        val eventSlot = slot<Event>()
        every { eventDispatcher.dispatch(capture(eventSlot)) } returns Unit

        // show menu bar
        getOverflowMenuButtonNode().performClick()

        // perform click on share-app menu item
        performClickOnMenuItem(0)

        // menu bar should not be visible
        getDropdownMenuNode().assertDoesNotExist()

        // verify dispatched event should be correct
        verify { eventDispatcher.dispatch(eventSlot.captured as GlobalEvents.ShareApp) }
    }

    @Test
    fun `test dispatch even should be as OpenFacebookGroup when OpenFacebookGroup menu item clicked`() {

        composeTestRule.setContent {
            AppBarOverflowMenu()
        }

        val eventDispatcher = mockEventDispatcher()

        val eventSlot = slot<Event>()
        every { eventDispatcher.dispatch(capture(eventSlot)) } returns Unit

        // show menu bar
        getOverflowMenuButtonNode().performClick()

        // perform click on share-app menu item
        performClickOnMenuItem(1)

        // menu bar should not be visible
        getDropdownMenuNode().assertDoesNotExist()

        // verify dispatched event should be correct
        verify { eventDispatcher.dispatch(eventSlot.captured as GlobalEvents.OpenFacebookGroup) }
    }

    private fun performClickOnMenuItem(index: Int) {
        getDropdownMenuNode()
            .onChildAt(index)
            .assertExists()
            .assertHasClickAction()
            .performClick()
    }

    private fun getOverflowMenuButtonNode(): SemanticsNodeInteraction {
        return composeTestRule.onNode(hasTestTag("overflow_menu_button"))
    }

    private fun getDropdownMenuNode(): SemanticsNodeInteraction {
        return composeTestRule.onNode(hasTestTag("dropdown_menu"))
    }
}