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

package pk.sufiishq.app.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import pk.sufiishq.app.SufiIshqTest
import pk.sufiishq.app.core.help.controller.HelpViewModel
import pk.sufiishq.app.core.help.model.HelpContent
import pk.sufiishq.app.core.help.repository.HelpContentRepository

@Ignore("will be fixed later")
class HelpViewModelTest : SufiIshqTest() {

    @get:Rule val rule = InstantTaskExecutorRule()

    private lateinit var helpContentRepository: HelpContentRepository
    private lateinit var helpViewModel: HelpViewModel

    @Before
    fun setUp() {
        helpContentRepository = mockk()
        helpViewModel = HelpViewModel(helpContentRepository)
    }

    @Test
    fun testGetHelpContent_shouldReturn_listOfHelpContent_asLiveData() = runBlocking {
        every { helpContentRepository.loadContent() } returns
            flow {
                emit(
                    listOf(
                        HelpContent("title", listOf()),
                    ),
                )
            }

    /*helpViewModel.getHelpContent().observe(mockLifecycleOwner()) {
        assertEquals(1, it.size)
    }*/
    }
}
