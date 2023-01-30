package pk.sufiishq.app.ui.components

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.performClick
import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.slot
import io.mockk.verify
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import pk.sufiishq.app.SufiIshqTest
import pk.sufiishq.app.core.event.dispatcher.EventDispatcher
import pk.sufiishq.app.core.event.events.Event
import pk.sufiishq.app.core.event.events.GlobalEvents
import pk.sufiishq.app.helpers.ScreenType
import pk.sufiishq.app.utils.fakeGlobalDataProvider

@Ignore("will be fixed later")
class AppBarOverflowMenuTest : SufiIshqTest() {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `test overflow menu button should be exists and visible by default`() {

        composeTestRule.setContent {
            AppBarOverflowMenu(mockk(), globalDataProvider = fakeGlobalDataProvider())
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
            AppBarOverflowMenu(mockk(), globalDataProvider = fakeGlobalDataProvider())
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

        mockkObject(EventDispatcher)

        composeTestRule.setContent {
            AppBarOverflowMenu(mockk(), globalDataProvider = fakeGlobalDataProvider())
        }

        val eventSlot = slot<Event>()
        every { EventDispatcher.dispatch(capture(eventSlot)) } returns Unit

        // show menu bar
        getOverflowMenuButtonNode().performClick()

        // perform click on share-app menu item
        performClickOnMenuItem(0)

        // menu bar should not be visible
        getDropdownMenuNode().assertDoesNotExist()

        // verify dispatched event should be correct
        verify { EventDispatcher.dispatch(eventSlot.captured as GlobalEvents.ShareApp) }
    }

    @Test
    fun `test dispatch even should be as OpenFacebookGroup when OpenFacebookGroup menu item clicked`() {

        mockkObject(EventDispatcher)

        composeTestRule.setContent {
            AppBarOverflowMenu(mockk(), globalDataProvider = fakeGlobalDataProvider())
        }

        val eventSlot = slot<Event>()
        every { EventDispatcher.dispatch(capture(eventSlot)) } returns Unit

        // show menu bar
        getOverflowMenuButtonNode().performClick()

        // perform click on facebook menu item
        performClickOnMenuItem(1)

        // menu bar should not be visible
        getDropdownMenuNode().assertDoesNotExist()

        // verify dispatched event should be correct
        verify { EventDispatcher.dispatch(eventSlot.captured as GlobalEvents.OpenFacebookGroup) }
    }

    @Test
    fun `test nav controller should navigate to Help screen when Help menu item clicked`() {

        val navController = mockk<NavController> {
            every { navigate(any(), any<(NavOptionsBuilder) -> Unit>()) } returns Unit
        }

        composeTestRule.setContent {
            AppBarOverflowMenu(mockk(), globalDataProvider = fakeGlobalDataProvider())
        }

        // show menu bar
        getOverflowMenuButtonNode().performClick()

        // perform click on help menu item
        performClickOnMenuItem(2)

        // menu bar should not be visible
        getDropdownMenuNode().assertDoesNotExist()

        // verify dispatched event should be correct
        verify {
            navController.navigate(
                ScreenType.Help.route(),
                any<(NavOptionsBuilder) -> Unit>()
            )
        }
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