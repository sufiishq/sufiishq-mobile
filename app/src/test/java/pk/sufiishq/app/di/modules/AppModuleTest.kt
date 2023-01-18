package pk.sufiishq.app.di.modules

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import pk.sufiishq.app.SufiIshqTest
import pk.sufiishq.app.core.player.SufiishqMediaPlayer
import pk.sufiishq.app.core.player.helper.AppMediaPlayer


class AppModuleTest : SufiIshqTest() {

    private lateinit var appModule: AppModule
    private lateinit var appContext: Context

    @Before
    fun setUp() {
        appModule = AppModule()
        appContext = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun test_providesHandler_shouldReturn_AndroidHandler() {
        assertNotNull(appModule.providesHandler())
    }

    @Test
    fun test_providesPreviewAudioPlayer_shouldReturn_PreviewAudioPlayer() {
        assertNotNull(appModule.providesPreviewAudioPlayer(appModule.providesHandler()))
    }

    @Test
    fun test_providesAndroidMediaPlayer_shouldReturn_SufiishqMediaPlayer() {
        val appMediaPlayer = AppMediaPlayer(appModule.providesHandler())
        val player = appModule.providesAndroidMediaPlayer(appContext, appMediaPlayer)

        assertNotNull(player)
        assertTrue(player is SufiishqMediaPlayer)
    }

    @Test
    fun test_helpContentJson_shouldReturn_helpJson() {
        val mockContext = mockk<Context>()

        every { mockContext.assets } returns mockk {
            every { open(any()) } answers {
                javaClass.classLoader.getResourceAsStream(firstArg())
            }
        }

        val helpJson = appModule.helpContentJson(mockContext)
        assertNotNull(helpJson)

        val data = helpJson.getJSONArray("data").getJSONObject(0)
        assertEquals("test title", data.getString("title"))
        assertEquals("help content", data.getJSONArray("content").getString(0))
    }

}