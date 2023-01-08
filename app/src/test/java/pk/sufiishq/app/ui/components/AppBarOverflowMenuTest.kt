package pk.sufiishq.app.ui.components

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.performClick
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import pk.sufiishq.app.SufiIshqTest

class AppBarOverflowMenuTest : SufiIshqTest() {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setUp() {
        composeTestRule.setContent {
            AppBarOverflowMenu()
        }
    }

    @Test
    fun testOverflowMenuWithMenuItems() {

        val menuBarMatcher = hasTestTag("menu_button_tag")
        val dropdownMenuMatcher = hasTestTag("dropdown_menu_tag")

        // verify menu bar button existence, visibility and click action
        composeTestRule.onNode(menuBarMatcher).assertExists()
        composeTestRule.onNode(menuBarMatcher).assertIsDisplayed()
        composeTestRule.onNode(menuBarMatcher).assertHasClickAction()

        // dropdown should be hide before perform click action
        composeTestRule.onNode(dropdownMenuMatcher).assertDoesNotExist()

        // perform click on menu bar
        composeTestRule.onNode(menuBarMatcher).performClick()

        // verify all menu item existence and visibility
        composeTestRule.onNode(dropdownMenuMatcher).assertExists()
        composeTestRule.onNode(dropdownMenuMatcher).assertIsDisplayed()

        // verify share menu item existence and visibility
        composeTestRule.onNode(dropdownMenuMatcher).onChildAt(0).assertExists()
        composeTestRule.onNode(dropdownMenuMatcher).onChildAt(0).assertHasClickAction()

        // verify facebook menu item existence and visibility
        composeTestRule.onNode(dropdownMenuMatcher).onChildAt(1).assertExists()
        composeTestRule.onNode(dropdownMenuMatcher).onChildAt(1).assertHasClickAction()
    }
}