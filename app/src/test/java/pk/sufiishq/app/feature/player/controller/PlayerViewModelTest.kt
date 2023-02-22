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

package pk.sufiishq.app.feature.player.controller

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.robolectric.util.ReflectionHelpers.ClassParameter
import org.robolectric.util.ReflectionHelpers.callInstanceMethod
import org.robolectric.util.ReflectionHelpers.getField
import pk.sufiishq.app.SufiIshqTest
import pk.sufiishq.app.feature.kalam.helper.TrackListType
import pk.sufiishq.app.feature.kalam.model.Kalam
import pk.sufiishq.app.feature.kalam.model.KalamInfo
import pk.sufiishq.app.feature.player.PlayerState
import pk.sufiishq.app.feature.player.state.MediaState
import pk.sufiishq.app.utils.getOrAwaitValue

class PlayerViewModelTest : SufiIshqTest() {

    private lateinit var player: AudioPlayer
    private lateinit var sampleKalamInfo: KalamInfo
    private lateinit var sampleKalam: Kalam
    private lateinit var playerViewModel: PlayerViewModel

    @Before
    fun setUp() {
        player = mockk()
        every { player.registerListener(any()) } returns true

        playerViewModel = PlayerViewModel(player)

        verify { player.registerListener(any()) }
        sampleKalamInfo = sampleKalamInfo()
        sampleKalam = sampleKalam()
    }

    @Test
    fun testUpdateSeekbarValue_shouldUpdate_kalamInfo_and_disableSeekbarOnPlay() {
        setDefaultKalamInfo()
        playerViewModel.updateSeekbarValue(10f)
        val result = playerViewModel.getKalamInfo().getOrAwaitValue()!!
        assertEquals(10, result.currentProgress)
        assertFalse(getField(playerViewModel, "seekbarEnableOnPlaying"))
    }

    @Test
    fun testOnSeekbarChanged_shouldDelegate_toPlayer_and_enableSeekbarOnPlay() {
        every { player.seekTo(any()) } returns Unit
        playerViewModel.onSeekbarChanged(50)

        verify { player.seekTo(50) }
        assertTrue(getField(playerViewModel, "seekbarEnableOnPlaying"))
    }

    @Test
    fun testDoPlayOrPause_shouldDelegate_toPlayer() {
        every { player.doPlayOrPause() } returns Unit
        playerViewModel.doPlayOrPause()
        verify { player.doPlayOrPause() }
    }

    @Test
    fun testSnStateChange_shouldMap_toLoadingState() {
        changeState(
            MediaState.Loading(sampleKalam, TrackListType.All())
        )

        assertEquals(
            PlayerState.LOADING,
            playerViewModel.getKalamInfo().getOrAwaitValue()!!.playerState
        )
    }

    @Test
    fun testSnStateChange_shouldMap_toPlayingState() {
        changeState(
            MediaState.Playing(sampleKalam, 0, 100, TrackListType.All())
        )

        assertEquals(
            PlayerState.PLAYING,
            playerViewModel.getKalamInfo().getOrAwaitValue()!!.playerState
        )
    }

    @Test
    fun testSnStateChange_shouldMap_toPauseState() {
        changeState(
            MediaState.Pause(sampleKalam, 0, 100, TrackListType.All())
        )

        assertEquals(
            PlayerState.PAUSE,
            playerViewModel.getKalamInfo().getOrAwaitValue()!!.playerState
        )
    }

    @Test
    fun testSnStateChange_shouldMap_toResumetate() {
        changeState(
            MediaState.Resume(sampleKalam, 0, 100, TrackListType.All())
        )

        assertEquals(
            PlayerState.PLAYING,
            playerViewModel.getKalamInfo().getOrAwaitValue()!!.playerState
        )
    }

    @Test
    fun testSnStateChange_shouldMap_toIdleState() {
        changeState(MediaState.Idle(sampleKalam, TrackListType.All()))
        assertEquals(
            PlayerState.IDLE,
            playerViewModel.getKalamInfo().getOrAwaitValue()!!.playerState
        )

        changeState(MediaState.Stop(sampleKalam, TrackListType.All()))
        assertEquals(
            PlayerState.IDLE,
            playerViewModel.getKalamInfo().getOrAwaitValue()!!.playerState
        )

        changeState(MediaState.Complete(sampleKalam, TrackListType.All()))
        assertEquals(
            PlayerState.IDLE,
            playerViewModel.getKalamInfo().getOrAwaitValue()!!.playerState
        )

        changeState(MediaState.Error(sampleKalam, 0, 0, "", TrackListType.All()))
        assertEquals(
            PlayerState.IDLE,
            playerViewModel.getKalamInfo().getOrAwaitValue()!!.playerState
        )
    }

    private fun changeState(mediaState: MediaState) {
        setDefaultKalamInfo()
        playerViewModel.onStateChange(mediaState)
    }

    private fun setDefaultKalamInfo() {
        callInstanceMethod<Unit>(
            playerViewModel, "updateKalamInfo",
            ClassParameter(
                KalamInfo::class.java,
                sampleKalamInfo
            )
        )
    }

}
