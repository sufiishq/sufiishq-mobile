package pk.sufiishq.app.ui.components

import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasAnyChild
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onParent
import androidx.compose.ui.test.performClick
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AppBarOverflowMenuTest {

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