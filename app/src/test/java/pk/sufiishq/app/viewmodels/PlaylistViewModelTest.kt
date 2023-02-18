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

package pk.sufiishq.app.viewmodels

import android.os.Looper.getMainLooper
import androidx.lifecycle.MutableLiveData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.robolectric.Shadows.shadowOf
import pk.sufiishq.app.SufiIshqApp
import pk.sufiishq.app.SufiIshqTest
import pk.sufiishq.app.feature.kalam.data.repository.KalamRepository
import pk.sufiishq.app.feature.playlist.controller.PlaylistViewModel
import pk.sufiishq.app.feature.playlist.data.repository.PlaylistRepository
import pk.sufiishq.app.feature.playlist.model.Playlist

class PlaylistViewModelTest : SufiIshqTest() {

    private lateinit var playlistViewModel: PlaylistViewModel
    private lateinit var playlistRepository: PlaylistRepository
    private lateinit var kalamRepository: KalamRepository
    private lateinit var appContext: SufiIshqApp

    @Before
    fun setUp() {
        playlistRepository = mockk()
        kalamRepository = mockk()
        appContext = mockApp()
        playlistViewModel = PlaylistViewModel(playlistRepository, kalamRepository, mockk())
    }

    @Ignore("will be fixed later")
    @Test
    fun testShowPlaylistAddUpdateDialog_shouldVerify_isShow() {
        val playlist = Playlist(1, "Islamabad")
        // playlistViewModel.onEvent(PlaylistEvents.ShowAddUpdatePlaylistDialog(playlist))

        shadowOf(getMainLooper()).idle()
        playlistViewModel.showAddUpdatePlaylistDialog().observe(mockLifecycleOwner()) {
            assertNotNull(it)
            assertEquals(playlist.id, it?.id)
            assertEquals(playlist.title, it?.title)
        }
    }

    @Ignore("will be fixed later")
    @Test
    fun testShowPlaylistConfirmDeleteDialog_shouldVerify_isShow() {
        val playlist = Playlist(1, "Islamabad")
        // playlistViewModel.onEvent(PlaylistEvents.ShowConfirmDeletePlaylistDialog(playlist))

        shadowOf(getMainLooper()).idle()
        playlistViewModel.showConfirmDeletePlaylistDialog().observe(mockLifecycleOwner()) {
            assertNotNull(it)
            assertEquals(playlist.id, it?.id)
            assertEquals(playlist.title, it?.title)
        }
    }

    @Ignore("will be fixed later")
    @Test
    fun testAdd_shouldVerify_playlistAddInDatabase() {
        launchViewModelScope(playlistViewModel) { slot ->
            coEvery { playlistRepository.add(any()) } returns Unit
            // playlistViewModel.onEvent(PlaylistEvents.Add(mockk()))
            slot.invoke()
            coVerify { playlistRepository.add(any()) }
        }
    }

    @Ignore("will be fixed later")
    @Test
    fun testUpdate_shouldVerify_playlistUpdateInDatabase() {
        launchViewModelScope(playlistViewModel) { slot ->
            coEvery { playlistRepository.update(any()) } returns Unit
            // playlistViewModel.onEvent(PlaylistEvents.Update(mockk()))
            slot.invoke()
            coVerify { playlistRepository.update(any()) }
        }
    }

    @Ignore("will be fixed later")
    @Test
    fun testDelete_shouldVerify_playlistDeleteAndResetRespectiveKalam() {
        launchViewModelScope(playlistViewModel) { slot ->
            val kalam = sampleKalam().copy(playlistId = 1)
            every { kalamRepository.loadAllPlaylistKalam(any()) } returns
                MutableLiveData(
                    listOf(kalam),
                )

            coEvery { kalamRepository.update(any()) } returns Unit
            coEvery { playlistRepository.delete(any()) } returns Unit

            // playlistViewModel.onEvent(PlaylistEvents.Delete(Playlist(1, "Karachi")))
            slot.invoke()

            assertEquals(0, kalam.playlistId)
            coVerify { kalamRepository.update(any()) }
            coVerify { playlistRepository.delete(any()) }
        }
    }

    /*@Ignore("will be fixed later")
    @Test(expected = UnhandledEventException::class)
    fun testUnknownEven_shouldReturn_unhandledEventException() {
        // playlistViewModel.onEvent(PlayerEvents.PlayPauseEvent)
    }*/
}
