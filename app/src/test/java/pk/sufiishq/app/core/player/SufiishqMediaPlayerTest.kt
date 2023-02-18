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

package pk.sufiishq.app.core.player

import android.content.Context
import android.media.AudioManager
import androidx.test.core.app.ApplicationProvider
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.spyk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.robolectric.util.ReflectionHelpers.callInstanceMethod
import org.robolectric.util.ReflectionHelpers.setField
import pk.sufiishq.app.SufiIshqTest
import pk.sufiishq.app.core.kalam.model.Kalam
import pk.sufiishq.app.core.player.controller.SufiishqMediaPlayer
import pk.sufiishq.app.core.player.helper.AppMediaPlayer
import pk.sufiishq.app.core.player.listener.PlayerStateListener
import pk.sufiishq.app.core.player.state.MediaState
import pk.sufiishq.app.helpers.TrackListType

class SufiishqMediaPlayerTest : SufiIshqTest(), PlayerStateListener {

    private lateinit var sufiishqMediaPlayer: SufiishqMediaPlayer
    private lateinit var appContext: Context
    private lateinit var mediaPlayer: AppMediaPlayer
    private var sampleKalam = sampleKalam()
    private var currentMediaState: MediaState = MediaState.Idle(sampleKalam, TrackListType.All())

    @Before
    fun setUp() {
        appContext = spyk(ApplicationProvider.getApplicationContext())
        mediaPlayer = mockk {
            every { stop() } returns Unit
            every { reset() } returns Unit
            every { setOnProgressChangeListener(any()) } returns Unit
            every { setOnCompletionListener(any()) } returns Unit
            every { setOnPreparedListener(any()) } returns Unit
            every { setOnErrorListener(any()) } returns Unit
            every { setDataSource(any(), any<Kalam>()) } returns Unit
            every { playbackParams = any() } returns Unit
            every { isPlaying } returns true
            every { currentPosition } returns 0
            every { duration } returns 100
        }

        sufiishqMediaPlayer = SufiishqMediaPlayer(appContext, mockk(), mockk(), mockk())
        sufiishqMediaPlayer.registerListener(this)
        setField(sufiishqMediaPlayer, "activeKalam", sampleKalam)
    }

    @Test
    fun testPlay_shouldSet_loadingState() {
        every { mediaPlayer.prepareAsync() } returns Unit
        call("play")

        verify(exactly = 1) { mediaPlayer.prepareAsync() }
        assertTrue(currentMediaState is MediaState.Loading)
    }

    @Test
    fun testPlay_shouldSet_pauseState() {
        every { mediaPlayer.pause() } returns Unit
        call("pause")

        verify(exactly = 1) { mediaPlayer.pause() }
        assertTrue(currentMediaState is MediaState.Pause)
    }

    @Test
    fun testResume_shouldSet_resumeState() {
        every { mediaPlayer.start() } returns Unit
        call("resume")

        verify(exactly = 1) { mediaPlayer.start() }
        assertTrue(currentMediaState is MediaState.Resume)
    }

    @Test
    fun testRePlay_shouldSet_LoadingState() {
        every { mediaPlayer.prepareAsync() } returns Unit
        call("rePlay")

        verify(exactly = 1) { mediaPlayer.prepareAsync() }
        assertTrue(currentMediaState is MediaState.Loading)
    }

    @Test
    fun testIsPlaying_shouldReturn_true() {
        assertTrue(sufiishqMediaPlayer.isPlaying())
    }

    @Test
    fun testSeekTo_shouldSet_loadingStateAndSeekPlayer() {
        val seekToSlot = slot<Int>()
        every { mediaPlayer.seekTo(capture(seekToSlot)) } returns Unit

        sufiishqMediaPlayer.seekTo(100)

        assertEquals(100, seekToSlot.captured)
        assertTrue(currentMediaState is MediaState.Loading)
    }

    @Test
    fun testRelease_shouldSet_stopState() {
        every { mediaPlayer.stop() } returns Unit
        every { mediaPlayer.reset() } returns Unit

        sufiishqMediaPlayer.release()

        verify { mediaPlayer.stop() }
        verify { mediaPlayer.reset() }
        assertTrue(currentMediaState is MediaState.Stop)
    }

    @Test
    fun testOnProgressChanged_shouldSet_playingState() {
        sufiishqMediaPlayer.onProgressChanged(1)
        assertTrue(currentMediaState is MediaState.Playing)
    }

    @Test
    fun testOnCompletion_shouldSet_CompleteState() {
        sufiishqMediaPlayer.onCompletion(null)
        assertTrue(currentMediaState is MediaState.Complete)
    }

    @Test
    fun testOnError_shouldSet_errorState() {
        sufiishqMediaPlayer.onError(null, 0, 0)
        assertTrue(currentMediaState is MediaState.Error)
    }

    @Test
    fun testOnAudioFocusChange_shouldSet_idleState() {
        sufiishqMediaPlayer.onAudioFocusChange(AudioManager.AUDIOFOCUS_LOSS)
        assertTrue(currentMediaState is MediaState.Idle)
    }

    @Test
    fun testOnAudioFocusChange_shouldSet_pauseState() {
        every { mediaPlayer.pause() } returns Unit
        sufiishqMediaPlayer.onAudioFocusChange(AudioManager.AUDIOFOCUS_LOSS_TRANSIENT)
        assertTrue(currentMediaState is MediaState.Pause)
    }

    @Test
    fun testOnAudioFocusChange_shouldSet_downPlayerVolume() {
        val leftVolumeSlot = slot<Float>()
        val rightVolumeSlot = slot<Float>()

        every {
            mediaPlayer.setVolume(
                capture(leftVolumeSlot),
                capture(rightVolumeSlot),
            )
        } returns Unit

        sufiishqMediaPlayer.onAudioFocusChange(AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK)

        verify { mediaPlayer.setVolume(any(), any()) }
        assertEquals(0.2f, leftVolumeSlot.captured)
        assertEquals(0.2f, rightVolumeSlot.captured)
    }

    @Test
    fun testOnAudioFocusChange_shouldSet_resetPlayerVolume() {
        val leftVolumeSlot = slot<Float>()
        val rightVolumeSlot = slot<Float>()

        every {
            mediaPlayer.setVolume(
                capture(leftVolumeSlot),
                capture(rightVolumeSlot),
            )
        } returns Unit

        sufiishqMediaPlayer.onAudioFocusChange(AudioManager.AUDIOFOCUS_GAIN)

        verify { mediaPlayer.setVolume(any(), any()) }
        assertEquals(1f, leftVolumeSlot.captured)
        assertEquals(1f, rightVolumeSlot.captured)
    }

    override fun onStateChange(mediaState: MediaState) {
        currentMediaState = mediaState
    }

    private fun call(methodName: String) {
        callInstanceMethod<Any>(sufiishqMediaPlayer, methodName)
    }
}
