package pk.sufiishq.app.config

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.slot
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import pk.sufiishq.app.SufiIshqApp
import pk.sufiishq.app.SufiIshqTest
import pk.sufiishq.app.configs.AppConfig
import pk.sufiishq.app.core.storage.KeyValueStorage

class AppConfigTest : SufiIshqTest() {


    private lateinit var storage: KeyValueStorage
    private lateinit var appConfig: AppConfig

    @Before
    fun setUp() {

        storage = mockk()
        mockkObject(SufiIshqApp)

        every { SufiIshqApp.getInstance() } returns mockk {
            every { keyValueStorage } returns storage
        }

        appConfig = AppConfig()
    }

    @Test
    fun testIsShuffle_shouldReturn_false() {
        val keySlot = slot<String>()
        every { storage.get(capture(keySlot), false) } returns false
        assertFalse(appConfig.isShuffle())
    }

    @Test
    fun testIsShuffle_shouldReturn_true() {
        val keySlot = slot<String>()
        every { storage.get(capture(keySlot), false) } returns true
        assertTrue(appConfig.isShuffle())
    }
}