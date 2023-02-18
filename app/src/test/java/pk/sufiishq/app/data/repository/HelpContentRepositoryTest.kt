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

package pk.sufiishq.app.data.repository

import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertNotNull
import org.junit.Test
import pk.sufiishq.app.SufiIshqTest
import pk.sufiishq.app.feature.help.repository.HelpContentRepository
import pk.sufiishq.app.feature.help.resolver.HelpContentResolver

class HelpContentRepositoryTest : SufiIshqTest() {

    @Test
    fun test_loadContent_shouldReturn_flowOfHelpContentList() {
        val helpContentResolver = mockk<HelpContentResolver> { every { resolve() } returns mockk() }

        val helpContentRepository = HelpContentRepository(helpContentResolver)
        assertNotNull(helpContentRepository.loadContent())
    }
}
