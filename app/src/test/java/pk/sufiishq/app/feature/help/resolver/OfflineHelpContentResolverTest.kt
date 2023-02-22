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

package pk.sufiishq.app.feature.help.resolver

import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import pk.sufiishq.app.SufiIshqTest
import pk.sufiishq.app.feature.help.HelpContentTransformer
import pk.sufiishq.app.feature.help.di.qualifier.HelpJson
import javax.inject.Inject

@HiltAndroidTest
class OfflineHelpContentResolverTest : SufiIshqTest() {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var transformer: HelpContentTransformer

    @HelpJson
    @Inject
    lateinit var helpJson: JSONObject
    private lateinit var offlineHelpContentResolver: HelpContentResolver

    @Before
    fun setUp() {
        hiltRule.inject()

        offlineHelpContentResolver = OfflineHelpContentResolver(
            Dispatchers.Main.immediate,
            helpJson,
            transformer,
        )
    }

    @Test
    fun testResolve_shouldReturn_HelpContentList() = runBlocking {
        val result = offlineHelpContentResolver.resolve().first()
        assertEquals(1, result.size)
    }
}
