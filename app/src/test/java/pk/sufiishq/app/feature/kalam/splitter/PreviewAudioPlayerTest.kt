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

package pk.sufiishq.app.feature.kalam.splitter

import android.media.MediaPlayer
import android.os.Handler
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import io.mockk.verifySequence
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import pk.sufiishq.app.SufiIshqTest

class PreviewAudioPlayerTest : SufiIshqTest() {

    private lateinit var previewAudioPlayer: PreviewAudioPlayer
    private lateinit var handler: Handler
    private lateinit var sourcePlayer: MediaPlayer

    @Before
    fun setUp() {
        handler = mockk()
        sourcePlayer = mockk()
        previewAudioPlayer = PreviewAudioPlayer(handler, sourcePlayer)
    }

    @Test
    fun testStart_shouldVerify_handlerUpdatesWithRunnable() {
        var progress = -1

        previewAudioPlayer.setOnProgressListener { progress = it }

        val runnableSlot = slot<Runnable>()
        val delaySLot = slot<Long>()

        every { sourcePlayer.start() } returns Unit
        every { handler.removeCallbacks(any()) } returns Unit
        every { handler.postDelayed(capture(runnableSlot), capture(delaySLot)) } returns true
        every { sourcePlayer.currentPosition } returns 20000

        previewAudioPlayer.start()

        verifySequence {
            sourcePlayer.start()
            handler.removeCallbacks(any())
            handler.postDelayed(runnableSlot.captured, delaySLot.captured)
        }

        runnableSlot.captured.run()

        assertEquals(20000, progress)
        verify(exactly = 2) { handler.postDelayed(runnableSlot.captured, delaySLot.captured) }
    }

    @Test
    fun testPause_shouldVerify_pauseAndHandlerRemoveCallback() {
        val runnableSlot = slot<Runnable>()

        every { sourcePlayer.pause() } returns Unit
        every { handler.removeCallbacks(capture(runnableSlot)) } returns Unit

        previewAudioPlayer.pause()

        verifySequence {
            sourcePlayer.pause()
            handler.removeCallbacks(runnableSlot.captured)
        }
    }

    @Test
    fun testGetDuration_shouldReturn_kalamDuration() {
        val path = "path-to-the-source"
        val pathSlot = slot<String>()

        every { sourcePlayer.reset() } returns Unit
        every { sourcePlayer.setDataSource(capture(pathSlot)) } returns Unit
        every { sourcePlayer.prepare() } returns Unit
        every { sourcePlayer.duration } returns 9999

        val duration = previewAudioPlayer.getDuration(path)

        assertEquals(9999, duration)
        assertEquals(path, pathSlot.captured)

        verifySequence {
            sourcePlayer.reset()
            sourcePlayer.setDataSource(pathSlot.captured)
            sourcePlayer.prepare()
            sourcePlayer.duration
        }
    }

    @Test
    fun testStop_shouldVerify_stopAndHandlerRemoveCallback() {
        val runnableSlot = slot<Runnable>()

        every { sourcePlayer.stop() } returns Unit
        every { handler.removeCallbacks(capture(runnableSlot)) } returns Unit

        previewAudioPlayer.stop()

        verifySequence {
            sourcePlayer.stop()
            handler.removeCallbacks(runnableSlot.captured)
        }
    }

    @Test
    fun testReleaseProgressListener_shouldVerify_HandlerRemoveCallback() {
        val runnableSlot = slot<Runnable>()
        every { handler.removeCallbacks(capture(runnableSlot)) } returns Unit
        previewAudioPlayer.releaseProgressListener()

        verifySequence { handler.removeCallbacks(runnableSlot.captured) }
    }

    @Test
    fun testIsPlaying_shouldReturn_false() {
        every { sourcePlayer.isPlaying } returns false
        assertFalse(previewAudioPlayer.isPlaying())
    }

    @Test
    fun testSeekTo_shouldVerify_sourcePlayerSeekTo() {
        val slotMsec = slot<Int>()

        every { sourcePlayer.seekTo(capture(slotMsec)) } returns Unit
        previewAudioPlayer.seekTo(2500)

        assertEquals(2500, slotMsec.captured)
        verifySequence { sourcePlayer.seekTo(slotMsec.captured) }
    }

    @Test
    fun testSetOnCompletionListener_shouldVerify_sourcePlayerCompleteListenerSet() {
        var isComplete = false

        val listener = MediaPlayer.OnCompletionListener { isComplete = true }

        val listenerSlot = slot<MediaPlayer.OnCompletionListener>()
        every { sourcePlayer.setOnCompletionListener(capture(listenerSlot)) } returns Unit

        previewAudioPlayer.setOnCompletionListener(listener)
        listenerSlot.captured.onCompletion(mockk())

        assertTrue(isComplete)
        verifySequence { sourcePlayer.setOnCompletionListener(listenerSlot.captured) }
    }
}
