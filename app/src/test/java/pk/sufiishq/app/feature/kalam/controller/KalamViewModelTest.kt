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

package pk.sufiishq.app.feature.kalam.controller

import androidx.activity.ComponentActivity
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingSource
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.robolectric.util.ReflectionHelpers.callInstanceMethod
import pk.sufiishq.app.StringRes
import pk.sufiishq.app.SufiIshqTest
import pk.sufiishq.app.di.qualifier.KalamItemPopupMenuItems
import pk.sufiishq.app.feature.app.AppManager
import pk.sufiishq.app.feature.kalam.data.repository.KalamRepository
import pk.sufiishq.app.feature.kalam.delete.KalamDeleteManager
import pk.sufiishq.app.feature.kalam.downloader.KalamDownloadManager
import pk.sufiishq.app.feature.kalam.downloader.KalamDownloadState
import pk.sufiishq.app.feature.kalam.favorite.FavoriteManager
import pk.sufiishq.app.feature.kalam.helper.TrackListType
import pk.sufiishq.app.feature.kalam.model.Kalam
import pk.sufiishq.app.feature.kalam.model.KalamDeleteItem
import pk.sufiishq.app.feature.kalam.splitter.KalamSplitManager
import pk.sufiishq.app.feature.kalam.splitter.SplitStatus
import pk.sufiishq.app.feature.player.PlayerManager
import pk.sufiishq.app.feature.playlist.PlaylistManager
import pk.sufiishq.app.feature.playlist.model.Playlist
import pk.sufiishq.app.helpers.ScreenType
import pk.sufiishq.app.helpers.popupmenu.PopupMenu
import pk.sufiishq.app.helpers.popupmenu.PopupMenuItem
import pk.sufiishq.app.utils.getOrAwaitValue
import pk.sufiishq.app.utils.getString
import pk.sufiishq.aurora.models.DataMenuItem
import javax.inject.Inject

@HiltAndroidTest
class KalamViewModelTest : SufiIshqTest() {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    @KalamItemPopupMenuItems
    lateinit var popupMenu: PopupMenu

    @MockK
    private lateinit var kalamDownloadManager: KalamDownloadManager

    @MockK
    private lateinit var kalamDeleteManager: KalamDeleteManager

    @MockK
    private lateinit var kalamSplitManager: KalamSplitManager

    @MockK
    private lateinit var kalamRepository: KalamRepository

    @MockK
    private lateinit var playlistManager: PlaylistManager

    @MockK
    private lateinit var favoriteManager: FavoriteManager

    @MockK
    private lateinit var playerManager: PlayerManager

    @MockK
    private lateinit var appManager: AppManager

    private val sampleKalam = sampleKalam()
    private lateinit var kalamViewModel: KalamViewModel

    @Before
    fun setUp() {
        hiltRule.inject()
        MockKAnnotations.init(this)

        every { kalamRepository.setTrackListType(any()) } returns Unit
        every { kalamRepository.setSearchKeyword(any()) } returns Unit

        kalamViewModel =
            KalamViewModel(
                popupMenu,
                kalamDownloadManager,
                kalamDeleteManager,
                kalamSplitManager,
                kalamRepository,
                playlistManager,
                favoriteManager,
                playerManager,
                appManager,
            )
    }

    @Test
    fun testLoad_shouldLoad_pagingSource() {
        every { kalamRepository.load() } returns mockk()
        callInstanceMethod<() -> PagingSource<Int, Kalam>>(kalamViewModel, "pagingSource").invoke()
        verify(exactly = 1) { kalamRepository.load() }
    }

    @Test
    fun testGetKalamDataFlow_shouldReturn_NonNullObject() {
        assertNotNull(kalamViewModel.getKalamDataFlow())
    }

    @Test
    fun testSearchKalam_shouldSet_givenParamInKalamRepository() {
        every { kalamRepository.setSearchKeyword(any()) } returns Unit
        every { kalamRepository.setTrackListType(any()) } returns Unit

        kalamViewModel.searchKalam("Karachi", TrackListType.Downloads())

        verify { kalamRepository.setSearchKeyword("Karachi") }
        verify { kalamRepository.setTrackListType(any<TrackListType.Downloads>()) }
    }

    @Test
    fun testPopupMenuItems_shouldReturn_filterMenuItem_basedOnAttributes() {
        assertPopupMenuItemList(
            kalamViewModel.popupMenuItems(
                sampleKalam.copy(
                    isFavorite = 0,
                    onlineSource = "online:)",
                    offlineSource = "",
                ),
                ScreenType.Tracks.ALL,
            ),
            listOf(
                PopupMenuItem.MarkAsFavorite(getString(StringRes.menu_item_mark_as_favorite)),
                PopupMenuItem.Download(getString(StringRes.menu_item_download)),
                PopupMenuItem.AddToPlaylist(getString(StringRes.menu_item_add_to_playlist)),
                PopupMenuItem.Share(getString(StringRes.menu_item_share)),
            ),
        )

        assertPopupMenuItemList(
            kalamViewModel.popupMenuItems(
                sampleKalam.copy(
                    isFavorite = 1,
                    onlineSource = "online:)",
                    offlineSource = "",
                ),
                ScreenType.Tracks.ALL,
            ),
            listOf(
                PopupMenuItem.MarkAsNotFavorite(getString(StringRes.menu_item_remove_favorite)),
                PopupMenuItem.Download(getString(StringRes.menu_item_download)),
                PopupMenuItem.AddToPlaylist(getString(StringRes.menu_item_add_to_playlist)),
                PopupMenuItem.Share(getString(StringRes.menu_item_share)),
            ),
        )

        assertPopupMenuItemList(
            kalamViewModel.popupMenuItems(
                sampleKalam.copy(
                    isFavorite = 0,
                    onlineSource = "online:(",
                    offlineSource = "offline:)",
                ),
                ScreenType.Tracks.ALL,
            ),
            listOf(
                PopupMenuItem.MarkAsFavorite(getString(StringRes.menu_item_mark_as_favorite)),
                PopupMenuItem.AddToPlaylist(getString(StringRes.menu_item_add_to_playlist)),
                PopupMenuItem.Share(getString(StringRes.menu_item_share)),
            ),
        )

        assertPopupMenuItemList(
            kalamViewModel.popupMenuItems(
                sampleKalam.copy(
                    isFavorite = 0,
                    onlineSource = "online:(",
                    offlineSource = "offline:)",
                ),
                ScreenType.Tracks.DOWNLOADS,
            ),
            listOf(
                PopupMenuItem.MarkAsFavorite(getString(StringRes.menu_item_mark_as_favorite)),
                PopupMenuItem.AddToPlaylist(getString(StringRes.menu_item_add_to_playlist)),
                PopupMenuItem.Share(getString(StringRes.menu_item_share)),
                PopupMenuItem.Split(getString(StringRes.menu_item_split_kalam)),
                PopupMenuItem.Delete(getString(StringRes.menu_item_delete)),
            ),
        )

        assertPopupMenuItemList(
            kalamViewModel.popupMenuItems(
                sampleKalam.copy(
                    isFavorite = 0,
                    onlineSource = "online:(",
                    offlineSource = "offline:)",
                ),
                ScreenType.Tracks.PLAYLIST,
            ),
            listOf(
                PopupMenuItem.MarkAsFavorite(getString(StringRes.menu_item_mark_as_favorite)),
                PopupMenuItem.Share(getString(StringRes.menu_item_share)),
                PopupMenuItem.Delete(getString(StringRes.menu_item_delete)),
            ),
        )
    }

    @Test
    fun testChangeTrack_shouldDelegate_toPlayerManager() {
        val trackListType = TrackListType.Downloads()

        every { playerManager.changeTrack(any(), any()) } returns Unit
        kalamViewModel.changeTrack(sampleKalam, trackListType)

        verify { playerManager.changeTrack(sampleKalam, trackListType) }
    }

    @Test
    fun testShareKalam_shouldDelegate_toAppManager() {
        val componentActivity = mockk<ComponentActivity>()

        every { appManager.shareKalam(any(), any()) } returns Unit
        kalamViewModel.shareKalam(sampleKalam, componentActivity)

        verify { appManager.shareKalam(sampleKalam, componentActivity) }
    }

    @Test
    fun testMarkAsFavorite_shouldDelegate_toFavoriteManager() {
        every { favoriteManager.markAsFavorite(any()) } returns Unit
        kalamViewModel.markAsFavorite(sampleKalam)

        verify { favoriteManager.markAsFavorite(sampleKalam) }
    }

    @Test
    fun testRemoveFavorite_shouldDelegate_toFavoriteManager() {
        every { favoriteManager.removeFavorite(any()) } returns Unit
        kalamViewModel.removeFavorite(sampleKalam)

        verify { favoriteManager.removeFavorite(sampleKalam) }
    }

    @Test
    fun testDelete_shouldDelegate_toKalamDeleteManager() {
        val kalamDeleteItem = KalamDeleteItem(
            sampleKalam,
            TrackListType.Downloads(),
        )

        every { kalamDeleteManager.delete(any()) } returns Unit
        kalamViewModel.delete(kalamDeleteItem)

        verify { kalamDeleteManager.delete(kalamDeleteItem) }
    }

    @Test
    fun testShowKalamConfirmDeleteDialog_shouldReturn_KalamDeleteItem() {
        var kalamDeleteItem: KalamDeleteItem? = null

        every { kalamDeleteManager.showKalamConfirmDeleteDialog(any()) } answers {
            kalamDeleteItem = firstArg()
        }

        kalamViewModel.showKalamConfirmDeleteDialog(
            KalamDeleteItem(
                sampleKalam,
                TrackListType.Downloads(),
            ),
        )

        every { kalamDeleteManager.showKalamConfirmDeleteDialog() } returns MutableLiveData(
            kalamDeleteItem,
        )

        val result = kalamViewModel.showKalamConfirmDeleteDialog().getOrAwaitValue()
        assertNotNull(result)
        assertEquals(sampleKalam.id, result?.kalam?.id)
        assertTrue(result?.trackListType is TrackListType.Downloads)
    }

    @Test
    fun testAddToPlaylist_shouldDelegate_toPlaylistManager() {
        val playlist = Playlist(0, "test")

        every { playlistManager.addToPlaylist(any(), any()) } returns Unit

        kalamViewModel.addToPlaylist(sampleKalam, playlist)
        verify { playlistManager.addToPlaylist(sampleKalam, playlist) }
    }

    @Test
    fun testShowPlaylistDialog_shouldReturn_pairKalamWithPlaylistItems() {
        var kalam: Kalam? = null

        every { playlistManager.showPlaylistDialog(any()) } answers {
            kalam = firstArg()
        }

        kalamViewModel.showPlaylistDialog(sampleKalam)

        every { playlistManager.showPlaylistDialog() } returns MutableLiveData(
            Pair(
                kalam!!,
                listOf(
                    Playlist(1, "tiny :)"),
                ),
            ),
        )

        val result = kalamViewModel.showPlaylistDialog().getOrAwaitValue()!!

        assertNotNull(result)
        assertEquals(sampleKalam.id, result.first.id)
        assertEquals(1, result.second[0].id)
        assertEquals("tiny :)", result.second[0].title)
    }

    @Test
    fun testDismissPlaylistDialog_shouldDelegate_toPlaylistManager() {
        every { playlistManager.dismissPlaylistDialog() } returns Unit
        kalamViewModel.dismissPlaylistDialog()
        verify { playlistManager.dismissPlaylistDialog() }
    }

    @Test
    fun testGetKalamDownloadState_shouldReturn_idleState() {
        every { kalamDownloadManager.getKalamDownloadState() } returns MutableLiveData(
            KalamDownloadState.Idle(false),
        )

        assertTrue(
            kalamViewModel.getKalamDownloadState().getOrAwaitValue() is KalamDownloadState.Idle,
        )
    }

    @Test
    fun testStartDownload_shouldDelegate_toKalamDownloadManager() {
        every { kalamDownloadManager.startDownload(any()) } returns Unit

        kalamViewModel.startDownload(sampleKalam, false)
        verify { kalamDownloadManager.startDownload(sampleKalam) }
    }

    @Test
    fun testDismissDownload_shouldDelegate_toKalamDownloadManager() {
        every { kalamDownloadManager.dismissDownload() } returns Unit

        kalamViewModel.dismissDownload()
        verify { kalamDownloadManager.dismissDownload() }
    }

    @Test
    fun testShowKalamSplitDialog_shouldDelegate_toKalamSplitManager() {
        every { kalamSplitManager.showKalamSplitDialog(any()) } returns Unit

        kalamViewModel.showKalamSplitDialog(sampleKalam)
        verify { kalamSplitManager.showKalamSplitDialog(sampleKalam) }
    }

    @Test
    fun testShowKalamSplitDialog_shouldReturn_mockSplitKalamInfo() {
        every { kalamSplitManager.showKalamSplitDialog() } returns MutableLiveData(
            mockk(),
        )
        assertNotNull(kalamViewModel.showKalamSplitDialog().getOrAwaitValue())
    }

    @Test
    fun testStartSplitting_shouldDelegate_toKalamSplitManager() {
        every { kalamSplitManager.startSplitting() } returns Unit

        kalamViewModel.startSplitting()
        verify { kalamSplitManager.startSplitting() }
    }

    @Test
    fun testPlaySplitKalamPreview_shouldDelegate_toKalamSplitManager() {
        every { kalamSplitManager.playSplitKalamPreview() } returns Unit

        kalamViewModel.playSplitKalamPreview()
        verify { kalamSplitManager.playSplitKalamPreview() }
    }

    @Test
    fun testSetSplitStart_shouldReturn_toKalamSplitManager() {
        every { kalamSplitManager.setSplitStart(any()) } returns Unit

        kalamViewModel.setSplitStart(10)
        verify { kalamSplitManager.setSplitStart(10) }
    }

    @Test
    fun testSetSplitEnd_shouldReturn_toKalamSplitManager() {
        every { kalamSplitManager.setSplitEnd(any()) } returns Unit

        kalamViewModel.setSplitEnd(20)
        verify { kalamSplitManager.setSplitEnd(20) }
    }

    @Test
    fun testSetSplitStatus_shouldReturn_toKalamSplitManager() {
        every { kalamSplitManager.setSplitStatus(any()) } returns Unit

        kalamViewModel.setSplitStatus(SplitStatus.Start)
        verify { kalamSplitManager.setSplitStatus(SplitStatus.Start) }
    }

    @Test
    fun testUpdateSplitSeekbarValue_shouldReturn_toKalamSplitManager() {
        every { kalamSplitManager.updateSplitSeekbarValue(any()) } returns Unit

        kalamViewModel.updateSplitSeekbarValue(3f)
        verify { kalamSplitManager.updateSplitSeekbarValue(3f) }
    }

    @Test
    fun testSaveSplitKalam_shouldReturn_toKalamSplitManager() {
        every { kalamSplitManager.saveSplitKalam(any(), any()) } returns Unit

        kalamViewModel.saveSplitKalam(sampleKalam, "bye im saving :)")
        verify { kalamSplitManager.saveSplitKalam(sampleKalam, "bye im saving :)") }
    }

    @Test
    fun testDismissKalamSplitDialog_shouldReturn_toKalamSplitManager() {
        every { kalamSplitManager.dismissKalamSplitDialog() } returns Unit

        kalamViewModel.dismissKalamSplitDialog()
        verify { kalamSplitManager.dismissKalamSplitDialog() }
    }

    private fun assertPopupMenuItemList(expected: List<DataMenuItem>, actual: List<DataMenuItem>) {
        expected
            .sortedBy {
                it.label
            }
            .zip(
                actual.sortedBy {
                    it.label
                },
            )
            .forEach {
                assertEquals(it.first.label, it.second.label)
                assertEquals(it.first.resId, it.second.resId)
            }
    }
}
