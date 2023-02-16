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

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.text.buildAnnotatedString
import io.mockk.mockk
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import pk.sufiishq.app.SufiIshqTest
import pk.sufiishq.app.core.help.HelpData
import pk.sufiishq.app.models.HelpContent
import pk.sufiishq.app.ui.screen.help.HelpExpandableCard

class HelpExpandableCardTest : SufiIshqTest() {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Ignore("will be fixed later")
    @Test
    fun `test in header card icon and title should be exists and displayed`() {
        setContent(mockk(), 1)

        val headerNode = composeTestRule.onNode(hasTestTag("card_header"), useUnmergedTree = true)
        headerNode.onChildAt(0).assertExists().assertIsDisplayed()

        composeTestRule.onNode(hasText("title")).assertExists().assertIsDisplayed()
    }

    @Test
    fun `test paragraph view should be exists and displayed`() {
        val helpData = HelpData.Paragraph(buildAnnotatedString { append("test") })
        setContent(helpData)

        composeTestRule.onNode(hasText("test")).assertExists().assertIsDisplayed()
    }

    @Test
    fun `test divider view should be exists and displayed`() {
        val helpData = HelpData.Divider(1)
        setContent(helpData)

        composeTestRule.onNode(hasTestTag("divider_tag")).assertExists().assertIsDisplayed()
    }

    @Test
    fun `test spacer view should be exists and displayed`() {
        val helpData = HelpData.Spacer(10)
        setContent(helpData)

        composeTestRule.onNode(hasTestTag("spacer_tag")).assertExists()
    }

    private fun setContent(helpData: HelpData, selectedIndex: Int = 0) {
        composeTestRule.setContent {
            HelpExpandableCard(
                expandedIndex = mutableStateOf(selectedIndex),
                index = 0,
                item = HelpContent("title", listOf(helpData)),
            )
        }
    }
}
