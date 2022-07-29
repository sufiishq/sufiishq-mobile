package pk.sufiishq.app.helpers

import android.content.Context
import android.net.Uri
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
    fun testSetDataSource_should_setOfflineDataSource() {

        every { sufiishqMediaPlayer.setDataSource(any<String>()) } returns Unit

        val kalam = mockk<Kalam> {
            every { offlineSource } returns "kalam/mock_kalam.mp3"
        }

        sufiishqMediaPlayer.setDataSource(appContext, kalam)

        val path = appContext.filesDir.absolutePath + "/" + kalam.offlineSource
        verify(exactly = 1) { sufiishqMediaPlayer.setDataSource(path) }
    }

    @Test
    fun testSetDataSource_should_setOnlineDataSource() {

        every { sufiishqMediaPlayer.setDataSource(any(), any<Uri>()) } returns Unit

        val kalam = mockk<Kalam> {
            every { offlineSource } returns ""
            every { onlineSource } returns "https://sufiishq.pk/media/kalam/dehli/dehli_02.mp3"
        }

        sufiishqMediaPlayer.setDataSource(appContext, kalam)

        val uri = Uri.parse(kalam.onlineSource)
        verify(exactly = 1) { sufiishqMediaPlayer.setDataSource(appContext, uri) }
    }
}