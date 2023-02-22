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

package pk.sufiishq.app.feature.playlist.data.repository

import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import pk.sufiishq.app.SufiIshqTest
import pk.sufiishq.app.feature.playlist.data.dao.PlaylistDao
import pk.sufiishq.app.feature.playlist.model.Playlist
import pk.sufiishq.app.utils.getOrAwaitValue
import javax.inject.Inject

@HiltAndroidTest
class PlaylistRepositoryTest : SufiIshqTest() {

    @get:Rule val hiltRule = HiltAndroidRule(this)

    private lateinit var playlistRepository: PlaylistRepository

    @Inject lateinit var playlistDao: PlaylistDao

    @Before
    fun setUp() {
        hiltRule.inject()
        playlistRepository = PlaylistRepository(playlistDao)
    }

    @Test
    fun testLoadAll_shouldReturn_allPlaylist() = runBlocking {
        playlistRepository.add(Playlist(0, ""))
        assertTrue(playlistRepository.loadAll().getOrAwaitValue()!!.isNotEmpty())
    }

    @Test
    fun testLoad_shouldReturn_playlistById() = runBlocking {
        playlistRepository.add(Playlist(2, ""))
        assertNotNull(playlistRepository.load(2).getOrAwaitValue())
    }

    @Test
    fun testUpdate_shouldVerify_playlistUpdate() = runBlocking {
        playlistRepository.add(Playlist(3, ""))
        val playlist = playlistRepository.load(3).getOrAwaitValue()!!
        playlistRepository.update(playlist.copy(title = "updated title"))
        assertEquals("updated title", playlistRepository.load(3).getOrAwaitValue()!!.title)
    }

    @Test
    fun testDelete_shouldVerify_playlistDelete() = runBlocking {
        playlistRepository.add(Playlist(4, ""))
        val playlist = playlistRepository.load(4).getOrAwaitValue()!!
        playlistRepository.delete(playlist)
        assertNull(playlistRepository.load(4).getOrAwaitValue())
    }

    @Test
    fun testCountAll_shouldReturn_allPlaylistCount() = runBlocking {
        playlistRepository.add(Playlist(5, ""))
        assertTrue(playlistRepository.countAll().getOrAwaitValue()!! > 0)
    }
}
