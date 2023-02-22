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

package pk.sufiishq.app.feature.playlist

import androidx.lifecycle.MutableLiveData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.robolectric.util.ReflectionHelpers.setField
import pk.sufiishq.app.SufiIshqTest
import pk.sufiishq.app.feature.kalam.data.repository.KalamRepository
import pk.sufiishq.app.feature.playlist.data.repository.PlaylistRepository
import pk.sufiishq.app.feature.playlist.model.Playlist
import pk.sufiishq.app.utils.getOrAwaitValue

class PlaylistManagerTest : SufiIshqTest() {

    private val playlistRepository = mockk<PlaylistRepository>()
    private val kalamRepository = mockk<KalamRepository>()
    private val dispatcher = Dispatchers.Main.immediate
    private val sampleKalam = sampleKalam()
    private lateinit var playlistManager: PlaylistManager

    @Before
    fun setUp() {
        playlistManager = PlaylistManager(playlistRepository, kalamRepository, dispatcher)
    }

    @Test
    fun testShouldAndHide_playlistDialog() {
        every { playlistRepository.loadAll() } returns MutableLiveData(
            listOf(
                Playlist(1, "test"),
            ),
        )

        playlistManager.showPlaylistDialog(sampleKalam)
        playlistManager.showPlaylistDialog().getOrAwaitValue()?.apply {
            assertEquals(sampleKalam.id, first.id)
            assertEquals(1, second[0].id)
            assertEquals("test", second[0].title)
        }
    }

    @Test
    fun testAddToPlaylist_showUpdate_kalamAndDismissPlaylistDialog() {
        coEvery { kalamRepository.update(any()) } returns Unit

        val job = mockk<Job> {
            every { cancel() } returns Unit
        }

        setField(playlistManager, "job", job)

        playlistManager.addToPlaylist(sampleKalam, Playlist(10, "my way"))

        verify { job.cancel() }
        coVerify { kalamRepository.update(sampleKalam) }

        assertEquals(10, sampleKalam.playlistId)
        assertNull(playlistManager.showPlaylistDialog().getOrAwaitValue())
    }
}
