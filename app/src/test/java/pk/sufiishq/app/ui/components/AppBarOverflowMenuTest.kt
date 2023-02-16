/*
 * Copyright 2022-2023 SufiIshq
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
import io.mockk.verify
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import pk.sufiishq.app.SufiIshqTest
import pk.sufiishq.app.helpers.ScreenType
import pk.sufiishq.app.ui.main.topbar.AppBarOverflowMenu
import pk.sufiishq.app.utils.fakeMainController

@Ignore("will be fixed later")
class AppBarOverflowMenuTest : SufiIshqTest() {

    @get:Rule val composeTestRule = createComposeRule()

    @Test
    fun `test overflow menu button should be exists and visible by default`() {
        composeTestRule.setContent {
            AppBarOverflowMenu(mockk(), mainController = fakeMainController())
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
            AppBarOverflowMenu(mockk(), mainController = fakeMainController())
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

    @Ignore("will be fixed later")
    @Test
    fun `test dispatch even should be as ShareApp when ShareApp menu item clicked`() {
        composeTestRule.setContent {
            AppBarOverflowMenu(mockk(), mainController = fakeMainController())
        }

        // show menu bar
        getOverflowMenuButtonNode().performClick()

        // perform click on share-app menu item
        performClickOnMenuItem(0)

        // menu bar should not be visible
        getDropdownMenuNode().assertDoesNotExist()

        // verify dispatched event should be correct
        // verify { EventDispatcher.dispatch(eventSlot.captured as GlobalEvents.ShareApp) }
    }

    @Ignore("will be fixed later")
    @Test
    fun `test dispatch even should be as OpenFacebookGroup when OpenFacebookGroup menu item clicked`() {
    /*mockkObject(EventDispatcher)

    composeTestRule.setContent {
        AppBarOverflowMenu(mockk(), mainController = fakeMainController())
    }

    val eventSlot = slot<Event>()
    every { EventDispatcher.dispatch(capture(eventSlot)) } returns Unit*/

        // show menu bar
        getOverflowMenuButtonNode().performClick()

        // perform click on facebook menu item
        performClickOnMenuItem(1)

        // menu bar should not be visible
        getDropdownMenuNode().assertDoesNotExist()

        // verify dispatched event should be correct
        // verify { EventDispatcher.dispatch(eventSlot.captured as GlobalEvents.OpenFacebookGroup) }
    }

    @Test
    fun `test nav controller should navigate to Help screen when Help menu item clicked`() {
        val navController =
            mockk<NavController> {
                every { navigate(any(), any<(NavOptionsBuilder) -> Unit>()) } returns Unit
            }

        composeTestRule.setContent {
            AppBarOverflowMenu(mockk(), mainController = fakeMainController())
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
                ScreenType.Help.buildRoute(),
                any<(NavOptionsBuilder) -> Unit>(),
            )
        }
    }

    private fun performClickOnMenuItem(index: Int) {
        getDropdownMenuNode().onChildAt(index).assertExists().assertHasClickAction().performClick()
    }

    private fun getOverflowMenuButtonNode(): SemanticsNodeInteraction {
        return composeTestRule.onNode(hasTestTag("overflow_menu_button"))
    }

    private fun getDropdownMenuNode(): SemanticsNodeInteraction {
        return composeTestRule.onNode(hasTestTag("dropdown_menu"))
    }
}
