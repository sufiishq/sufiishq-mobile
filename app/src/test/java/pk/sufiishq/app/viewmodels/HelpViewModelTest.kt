package pk.sufiishq.app.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import pk.sufiishq.app.SufiIshqTest
import pk.sufiishq.app.data.repository.HelpContentRepository
import pk.sufiishq.app.models.HelpContent

class HelpViewModelTest : SufiIshqTest() {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var helpContentRepository: HelpContentRepository
    private lateinit var helpViewModel: HelpViewModel

    @Before
    fun setUp() {
        helpContentRepository = mockk()
        helpViewModel = HelpViewModel(helpContentRepository)
    }

    @Test
    fun testGetHelpContent_shouldReturn_listOfHelpContent_asLiveData() = runBlocking {

        every { helpContentRepository.loadContent() } returns flow {
            emit(
                listOf(
                    HelpContent("title", listOf())
                )
            )
        }

        helpViewModel.getHelpContent().observe(mockLifecycleOwner()) {
            assertEquals(1, it.size)
        }
    }
}