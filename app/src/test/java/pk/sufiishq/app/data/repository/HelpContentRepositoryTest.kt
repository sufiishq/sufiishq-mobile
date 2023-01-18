package pk.sufiishq.app.data.repository

import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertNotNull
import org.junit.Test
import pk.sufiishq.app.SufiIshqTest
import pk.sufiishq.app.core.help.HelpContentResolver

class HelpContentRepositoryTest : SufiIshqTest() {

    @Test
    fun test_loadContent_shouldReturn_flowOfHelpContentList() {
        val helpContentResolver = mockk<HelpContentResolver> {
            every { resolve() } returns mockk()
        }

        val helpContentRepository = HelpContentRepository(helpContentResolver)
        assertNotNull(helpContentRepository.loadContent())
    }
}