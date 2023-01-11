package pk.sufiishq.app.ui.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule
import org.junit.Test
import pk.sufiishq.app.SufiIshqTest

class SIAnimatedLogTest : SufiIshqTest() {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `test logo image should be exists and displayed`() {
        composeTestRule.setContent {
            SIAnimatedLog()
        }

        composeTestRule.onNode(hasTestTag("logo")).assertExists().assertIsDisplayed()
    }
}