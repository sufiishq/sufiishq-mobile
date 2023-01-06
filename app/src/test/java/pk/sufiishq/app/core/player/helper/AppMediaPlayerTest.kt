package pk.sufiishq.app.core.player.helper

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.Handler
import androidx.test.core.app.ApplicationProvider
import io.mockk.*
import java.io.File
import org.junit.Before
import org.junit.Test
import org.robolectric.annotation.Config
import org.robolectric.annotation.Implements
import org.robolectric.shadows.ShadowMediaPlayer
import org.robolectric.util.ReflectionHelpers.getField
import pk.sufiishq.app.SufiIshqTest
import pk.sufiishq.app.core.player.helper.AppMediaPlayer.Companion.UPDATE_DELAY
import pk.sufiishq.app.models.Kalam

@Config(shadows = [AppMediaPlayerTest.ShadowPlayer::class])
class AppMediaPlayerTest : SufiIshqTest() {

    private lateinit var appMediaPlayer: AppMediaPlayer
    private lateinit var context: Context
    private var handler = mockk<Handler>()

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        appMediaPlayer = spyk(AppMediaPlayer(handler))
    }

    @Test
    fun testSetDataSource_shouldSet_offlineDataSource() {
        every { appMediaPlayer.setDataSource(any<String>()) } returns Unit

        appMediaPlayer.setDataSource(context, mockk<Kalam> {
            every { offlineSource } returns "kalam/test.mp3"
        })

        verify { appMediaPlayer.setDataSource(context.filesDir.absolutePath + File.separator + "kalam/test.mp3") }
    }

    @Test
    fun testSetDataSource_shouldSet_onlineDataSource() {
        every { appMediaPlayer.setDataSource(any(), any<Uri>()) } returns Unit

        val onlineUrl = "https://www.sufiishq.pk/media/kalam/mix/pk_1.mp3"
        appMediaPlayer.setDataSource(context, mockk<Kalam> {
            every { offlineSource } returns ""
            every { onlineSource } returns onlineUrl
        })

        verify { appMediaPlayer.setDataSource(context, Uri.parse(onlineUrl)) }
    }

    @Test
    fun testStart_shouldStart_mediaAndHandlerRunnable() {
        val runnableSlot = slot<Runnable>()
        val updateDelaySlot = slot<Long>()

        every { handler.removeCallbacks(capture(runnableSlot)) } returns Unit
        every { handler.postDelayed(capture(runnableSlot), capture(updateDelaySlot)) } returns true

        appMediaPlayer.start()

        verify { handler.removeCallbacks(runnableSlot.captured) }
        verify { handler.postDelayed(runnableSlot.captured, updateDelaySlot.captured) }
    }

    @Test
    fun testPause_shouldRemove_handlerCallback() {
        val runnableSlot = slot<Runnable>()
        every { handler.removeCallbacks(capture(runnableSlot)) } returns Unit

        appMediaPlayer.pause()

        verify { handler.removeCallbacks(runnableSlot.captured) }
    }

    @Test
    fun testStop_shouldRemove_handlerCallback() {
        val runnableSlot = slot<Runnable>()
        every { handler.removeCallbacks(capture(runnableSlot)) } returns Unit

        appMediaPlayer.stop()

        verify { handler.removeCallbacks(runnableSlot.captured) }
    }

    @Test
    fun testSetOnCompletionListener_shouldRemove_handlerCallbackAndVerifyCompletion() {

        val listener = mockk<MediaPlayer.OnCompletionListener>()
        val runnableSlot = slot<Runnable>()

        every { handler.removeCallbacks(capture(runnableSlot)) } returns Unit
        every { listener.onCompletion(any()) } returns Unit

        appMediaPlayer.setOnCompletionListener(listener)

        verify { handler.removeCallbacks(runnableSlot.captured) }
        verify { listener.onCompletion(any()) }
    }

    @Test
    fun testRunnable_shouldCall_progressChangeAndPostDelayed() {

        val positionSlot = slot<Int>()
        val listener = mockk<AppMediaPlayer.OnProgressChangeListener>()

        every { appMediaPlayer.currentPosition } returns 1000
        every { listener.onProgressChanged(capture(positionSlot)) } returns Unit
        every { handler.postDelayed(any(), UPDATE_DELAY) } returns true

        appMediaPlayer.setOnProgressChangeListener(listener)
        getField<Runnable>(appMediaPlayer, "runnable").run()

        verify { handler.postDelayed(any(), UPDATE_DELAY) }
    }

    @Implements(MediaPlayer::class)
    class ShadowPlayer : ShadowMediaPlayer() {
        override fun start() {}
        override fun _pause() {}
        override fun _stop() {}
        override fun setOnCompletionListener(listener: MediaPlayer.OnCompletionListener?) {
            listener?.onCompletion(null)
        }
    }
}