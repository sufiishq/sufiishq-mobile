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

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import pk.sufiishq.app.SufiIshqTest
import pk.sufiishq.app.feature.kalam.helper.TrackListType
import pk.sufiishq.app.ui.screen.tracks.KalamItem
import pk.sufiishq.app.ui.screen.tracks.TEST_TAG_KALAM_DATA_ROW
import pk.sufiishq.app.utils.fakeKalamController

class KalamItemTest : SufiIshqTest() {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setUp() {
        composeTestRule.setContent {
            KalamItem(
                kalam = sampleKalam(),
                trackListType = TrackListType.All(),
                kalamController = fakeKalamController(),
            )
        }
    }

    @Test
    fun `test dispatch ChangeTrack even when kalam item gets clicked`() {
        composeTestRule
            .onNodeWithTag(TEST_TAG_KALAM_DATA_ROW)
            .assertExists()
            .assertIsDisplayed()
    }
}
