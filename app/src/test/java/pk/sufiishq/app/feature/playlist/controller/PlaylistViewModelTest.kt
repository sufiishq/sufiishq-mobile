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

package pk.sufiishq.app.feature.playlist.controller

import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import pk.sufiishq.app.StringRes
import pk.sufiishq.app.SufiIshqTest
import pk.sufiishq.app.di.qualifier.PlaylistItemPopupMenuItems
import pk.sufiishq.app.feature.kalam.data.repository.KalamRepository
import pk.sufiishq.app.feature.playlist.data.repository.PlaylistRepository
import pk.sufiishq.app.feature.playlist.model.Playlist
import pk.sufiishq.app.helpers.popupmenu.PopupMenu
import pk.sufiishq.app.helpers.popupmenu.PopupMenuItem
import pk.sufiishq.app.utils.getOrAwaitValue
import pk.sufiishq.app.utils.getString
import javax.inject.Inject

@HiltAndroidTest
class PlaylistViewModelTest : SufiIshqTest() {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    @PlaylistItemPopupMenuItems
    lateinit var popupMenu: PopupMenu

    private lateinit var playlistViewModel: PlaylistViewModel
    private lateinit var playlistRepository: PlaylistRepository
    private lateinit var kalamRepository: KalamRepository

    @Before
    fun setUp() {
        hiltRule.inject()
        playlistRepository = mockk()
        kalamRepository = mockk()
        playlistViewModel = PlaylistViewModel(playlistRepository, kalamRepository, popupMenu)
    }

    @Test
    fun testGetPopupMenuItems_shouldReturn_playlistPopupMenuItems() {
        playlistViewModel
            .getPopupMenuItems()
            .sortedBy {
                it.label
            }
            .zip(
                listOf(
                    PopupMenuItem.Edit(getString(StringRes.menu_item_rename)),
                    PopupMenuItem.Delete(getString(StringRes.menu_item_delete)),
                ).sortedBy {
                    it.label
                },
            )
            .forEach {
                assertEquals(it.first.label, it.second.label)
                assertEquals(it.first.resId, it.second.resId)
            }
    }

    @Test
    fun testAddUpdatePlaylistDialog_shouldVerify_isShow() {
        val playlist = Playlist(1, "Islamabad")

        playlistViewModel.showAddUpdatePlaylistDialog(playlist)

        playlistViewModel.showAddUpdatePlaylistDialog().getOrAwaitValue().let {
            assertNotNull(it)
            assertEquals(playlist.id, it?.id)
            assertEquals(playlist.title, it?.title)
        }
    }

    @Test
    fun testShowPlaylistConfirmDeleteDialog_shouldVerify_isShow() {
        val playlist = Playlist(1, "Islamabad")

        playlistViewModel.showConfirmDeletePlaylistDialog(playlist)

        playlistViewModel.showConfirmDeletePlaylistDialog().getOrAwaitValue().let {
            assertNotNull(it)
            assertEquals(playlist.id, it?.id)
            assertEquals(playlist.title, it?.title)
        }
    }

    @Test
    fun testGetAll_shouldReturn_allPlaylist() {
        every { playlistRepository.loadAll() } returns MutableLiveData(
            listOf(Playlist(1, "first :(")),
        )

        playlistViewModel.getAll().getOrAwaitValue()?.let {
            assertEquals(1, it.size)
            assertEquals(1, it[0].id)
            assertEquals("first :(", it[0].title)
        }
    }

    @Test
    fun testGet_shouldReturn_singlePlaylist() {
        every { playlistRepository.load(any()) } returns MutableLiveData(
            Playlist(1, "first :("),
        )

        playlistViewModel.get(1).getOrAwaitValue()?.let {
            assertEquals(1, it.id)
            assertEquals("first :(", it.title)
        }
    }

    @Test
    fun testAdd_shouldAdd_playlistToPlaylistRepository() {
        val playlist = mockk<Playlist>()
        coEvery { playlistRepository.add(any()) }
        playlistViewModel.add(playlist)
        coVerify { playlistRepository.add(playlist) }
    }

    @Test
    fun testUpdate_shouldUpdate_playlistToPlaylistRepository() {
        val playlist = mockk<Playlist>()
        coEvery { playlistRepository.update(any()) }
        playlistViewModel.update(playlist)
        coVerify { playlistRepository.update(playlist) }
    }

    @Test
    fun testDelete_shouldDeletePlaylist_andResetReferencedKalam() {
        val kalam = sampleKalam()
        val playlist = Playlist(1, "test")
        every { kalamRepository.loadAllPlaylistKalam(any()) } returns MutableLiveData(
            listOf(
                kalam,
            ),
        )

        coEvery { kalamRepository.update(any()) } returns Unit
        coEvery { playlistRepository.delete(any()) } returns Unit

        playlistViewModel.delete(playlist)

        coVerify { kalamRepository.update(kalam) }
        coVerify { playlistRepository.delete(playlist) }
        assertEquals(0, kalam.playlistId)
    }
}
