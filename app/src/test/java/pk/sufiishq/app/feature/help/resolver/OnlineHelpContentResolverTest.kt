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
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import pk.sufiishq.app.SufiIshqTest
import pk.sufiishq.app.feature.help.HelpContentTransformer
import pk.sufiishq.app.feature.help.api.HelpContentService
import pk.sufiishq.app.feature.help.di.qualifier.HelpJson
import retrofit2.Response
import javax.inject.Inject

@HiltAndroidTest
class OnlineHelpContentResolverTest : SufiIshqTest() {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var transformer: HelpContentTransformer

    @HelpJson
    @Inject
    lateinit var helpJson: JSONObject
    private lateinit var helpContentService: HelpContentService
    private lateinit var onlineHelpContentResolver: HelpContentResolver

    @Before
    fun setUp() {
        hiltRule.inject()
        helpContentService = mockk()
        onlineHelpContentResolver = OnlineHelpContentResolver(
            Dispatchers.Main.immediate,
            helpJson,
            helpContentService,
            transformer,
        )
    }

    @Test
    fun testResolve_shouldReturn_HelpContentList() = runBlocking {
        val helpUrlSlot = slot<String>()
        every { helpContentService.getHelp(capture(helpUrlSlot)) } returns mockk {
            every { execute() } returns Response.success(
                helpJson.toString().toResponseBody(),
            )
        }

        val result = onlineHelpContentResolver.resolve().first()
        assertEquals(1, result.size)
        assertEquals(HELP_URL, helpUrlSlot.captured)
    }

    @Test
    fun testResolve_shouldReturn_offlineHelpJson_whenUnsuccessful() = runBlocking {
        every { helpContentService.getHelp(any()) } returns mockk {
            every { execute() } returns mockk {
                every { isSuccessful } returns false
            }
        }

        val result = onlineHelpContentResolver.resolve().first()
        assertEquals(1, result.size)
    }

    @Test
    fun testResolve_shouldReturn_offlineHelpJson_whenCaughtException() = runBlocking {
        every { helpContentService.getHelp(any()) } throws RuntimeException("Oops")
        val result = onlineHelpContentResolver.resolve().first()
        assertEquals(1, result.size)
    }

    companion object {
        private const val HELP_URL =
            "https://raw.githubusercontent.com/sufiishq/sufiishq-mobile/master/app/src/main/assets/help/help.json"
    }
}
