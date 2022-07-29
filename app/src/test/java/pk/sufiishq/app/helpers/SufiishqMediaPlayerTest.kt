package pk.sufiishq.app.helpers

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import pk.sufiishq.app.SufiIshqTest
import pk.sufiishq.app.models.Kalam

class SufiishqMediaPlayerTest : SufiIshqTest() {

    private lateinit var sufiishqMediaPlayer: SufiishqMediaPlayer
    private lateinit var appContext: Context

    @Before
    fun setUp() {
        appContext = ApplicationProvider.getApplicationContext()
        sufiishqMediaPlayer = spyk(SufiishqMediaPlayer())
    }

    @Test
    fun testSetDataSource_shouldSetOfflineDataSource() {

        val kalam = mockk<Kalam> {
            every { offlineSource } returns "kalam/mock_kalam.mp3"
        }
        sufiishqMediaPlayer.setDataSource(appContext, kalam)

        verify(exactly = 1) { sufiishqMediaPlayer.setDataSource(appContext.filesDir.absolutePath + "/" + kalam.offlineSource) }
    }
}