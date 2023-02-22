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

package pk.sufiishq.app.feature.help.repository

import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import pk.sufiishq.app.SufiIshqTest
import pk.sufiishq.app.feature.help.di.qualifier.OfflineResolver
import pk.sufiishq.app.feature.help.resolver.HelpContentResolver

@HiltAndroidTest
class HelpContentRepositoryTest : SufiIshqTest() {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @OfflineResolver
    @Inject
    lateinit var helpContentResolver: HelpContentResolver

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun test_loadContent_shouldReturn_flowOfHelpContentList() = runBlocking {
        val helpContentRepository = HelpContentRepository(helpContentResolver)
        val result = helpContentRepository.loadContent().first()
        Assert.assertEquals(1, result.size)
    }
}
