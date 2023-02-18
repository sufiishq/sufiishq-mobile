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

import android.content.Context
import androidx.paging.PagingSource
import androidx.test.core.app.ApplicationProvider
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.verify
import io.reactivex.Completable
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.robolectric.util.ReflectionHelpers.callInstanceMethod
import pk.sufiishq.app.R
import pk.sufiishq.app.SufiIshqApp
import pk.sufiishq.app.SufiIshqTest
import pk.sufiishq.app.core.kalam.controller.KalamViewModel
import pk.sufiishq.app.core.kalam.data.repository.KalamRepository
import pk.sufiishq.app.core.kalam.model.Kalam
import pk.sufiishq.app.core.kalam.model.KalamDeleteItem
import pk.sufiishq.app.core.kalam.splitter.KalamSplitManager
import pk.sufiishq.app.core.player.controller.AudioPlayer
import pk.sufiishq.app.helpers.TrackListType
import pk.sufiishq.app.utils.KALAM_DIR
import pk.sufiishq.app.utils.moveTo
import pk.sufiishq.app.utils.toast
import java.io.File
import java.util.*

class KalamViewModelTest : SufiIshqTest() {

    private lateinit var kalamViewModel: KalamViewModel
    private lateinit var kalamRepository: KalamRepository
    private lateinit var kalamSplitManager: KalamSplitManager
    private lateinit var audioPlayer: AudioPlayer
    private lateinit var realContext: Context
    private lateinit var appContext: SufiIshqApp

    @Before
    fun setUp() {
        appContext = mockApp()
        realContext = ApplicationProvider.getApplicationContext()
        kalamRepository = mockk {
            every { setTrackListType(any()) } returns Unit
            every { setSearchKeyword(any()) } returns Unit
        }
        kalamSplitManager = mockk()
        audioPlayer = mockk()

        /*every { EventDispatcher.registerEventHandler(any()) } returns Unit
        kalamViewModel =
            KalamViewModel(
                appContext,
                kalamRepository,
                kalamSplitManager,
                mockk(),
                mockk(),
                mockk(),
                audioPlayer,
                kalamDeleteStrategyFactory,
                mockk(),
            )*/
    }

    @Test
    fun testLoad_shouldLoad_pagingSource() {
        every { kalamRepository.load() } returns mockk()
        callInstanceMethod<() -> PagingSource<Int, Kalam>>(kalamViewModel, "pagingSource").invoke()
        verify(exactly = 1) { kalamRepository.load() }
    }

    @Test
    fun testVerify_kalamConfirmDeleteDialog() {
        /*val kalamDeleteItem =
            KalamDeleteItem(
                kalam = sampleKalam(),
                trackListType = TrackListType.Downloads,
            )*/

        /*kalamViewModel.onEvent(KalamEvents.ShowKalamConfirmDeleteDialog(kalamDeleteItem))

        shadowOf(Looper.getMainLooper()).idle()
        kalamViewModel.getKalamDeleteConfirmDialog().observe(mockLifecycleOwner()) {
            assertNotNull(it)
            assertEquals(sampleKalam().id, it?.kalam?.id)
            assertTrue(it?.trackListType is TrackListType.Downloads)
        }*/
    }

    @Ignore("will be fixed later")
    @Test
    fun testVerify_kalamSplitManagerDialog() {
        /*every { eventDispatcher.dispatch(any()) } returns Unit

        kalamViewModel.onEvent(KalamEvents.ShowKalamSplitManagerDialog(sampleKalam()))

        shadowOf(Looper.getMainLooper()).idle()
        kalamViewModel.showSplitKalamDialog().observe(mockLifecycleOwner()) { assertNotNull(it) }

        verify { eventDispatcher.dispatch(any<KalamSplitManagerEvents.SetKalam>()) }*/
    }

    @Test
    fun testVerify_kalamRenameDialog() {
        /*kalamViewModel.onEvent(KalamEvents.ShowKalamRenameDialog(sampleKalam()))

        shadowOf(Looper.getMainLooper()).idle()
        kalamViewModel.getKalamRenameDialog().observe(mockLifecycleOwner()) {
            assertNotNull(it)
            assertEquals(sampleKalam().id, it?.id)
        }*/
    }

    @Test
    fun testSearchKalam_shouldSet_givenParamInKalamRepository() {
        /*every { kalamRepository.setSearchKeyword(any()) } returns Unit
        every { kalamRepository.setTrackListType(any()) } returns Unit

        kalamViewModel.onEvent(KalamEvents.SearchKalam("Karachi", TrackListType.Downloads))

        verify { kalamRepository.setSearchKeyword("Karachi") }
        verify { kalamRepository.setTrackListType(any<TrackListType.Downloads>()) }*/
    }

    @Test
    fun testUpdate_shouldUpdate_givenKalamInDB() {
        /*launchViewModelScope(kalamViewModel) { slot ->
            coEvery { kalamRepository.update(any()) } returns Unit
            kalamViewModel.onEvent(KalamEvents.UpdateKalam(mockk()))
            slot.invoke()
            coVerify(exactly = 1) { kalamRepository.update(any()) }
        }*/
    }

    @Test
    fun testGetKalamDataFlow_shouldReturn_NonNullObject() {
        assertNotNull(kalamViewModel.getKalamDataFlow())
    }

    @Test
    fun testDelete_shouldDeleteKalam_usingStrategy() {
        /*launchViewModelScope(kalamViewModel) { slot ->
            val kalam = sampleKalam()
            val kalamDeleteItem = KalamDeleteItem(kalam, TrackListType.Downloads)
            val deleteFromDownloadsStrategy = mockk<DeleteFromDownloadsStrategy>()

            every { audioPlayer.getActiveTrack() } returns kalam.copyWithDefaults(id = 22)
            every { kalamDeleteStrategyFactory.create(any()) } returns deleteFromDownloadsStrategy
            coEvery { deleteFromDownloadsStrategy.delete(any()) } returns Unit

            kalamViewModel.onEvent(KalamEvents.DeleteKalam(kalamDeleteItem))

            slot.invoke()

            verify { kalamDeleteStrategyFactory.create(eq(TrackListType.Downloads.type)) }
            coVerify { deleteFromDownloadsStrategy.delete(any()) }
        }*/
    }

    @Test
    fun testDelete_shouldShow_toast() {
        mockkStatic(Context::toast)

        every { appContext.toast(any()) } returns mockk()
        every { appContext.getString(any()) } answers { realContext.getString(firstArg()) }

        launchViewModelScope(kalamViewModel) { slot ->
            val kalam = sampleKalam()
            val kalamDeleteItem = KalamDeleteItem(kalam, TrackListType.Downloads())

            every { audioPlayer.getActiveTrack() } returns kalam

            // kalamViewModel.onEvent(KalamEvents.DeleteKalam(kalamDeleteItem))

            slot.invoke()

            verify {
                appContext.toast(
                    realContext
                        .getString(R.string.dynamic_delete_on_playing_error)
                        .format(kalamDeleteItem.kalam.title),
                )
            }
        }
    }

    @Test
    fun testSave_should_insertAndMoveKalam() {
        mockkStatic(Context::toast)
        mockkStatic(File::moveTo)
        mockkStatic(Calendar::class)

        launchViewModelScope(kalamViewModel) { slot ->
            val sourceKalam = sampleKalam()
            val splitFile = mockk<File>()
            val kalamSlot = slot<Kalam>()
            val destinationSlot = slot<File>()
            val completable = mockk<Completable>()
            val calendar = mockk<Calendar>()
            val textSlot = slot<String>()

            every { appContext.toast(capture(textSlot)) } returns mockk()
            every { appContext.getString(any()) } answers { realContext.getString(firstArg()) }
            coEvery { kalamRepository.insert(capture(kalamSlot)) } returns Unit

            every { appContext.filesDir } returns File("files")
            every { calendar.timeInMillis } returns 1000L
            every { Calendar.getInstance() } returns calendar
            every { splitFile.moveTo(capture(destinationSlot)) } returns mockk()
            every { completable.subscribeOn(any()) } returns completable
            every { completable.observeOn(any()) } returns completable
            every { completable.subscribe() } returns mockk()

            /*kalamViewModel.onEvent(
                KalamEvents.SaveSplitKalam(
                    sourceKalam,
                    splitFile,
                    "Kalam Title",
                ),
            )*/

            slot.invoke()

            val updatedKalam = kalamSlot.captured
            val destFile = destinationSlot.captured
            val filename = KALAM_DIR + File.separator + "kalam_title_1000.mp3"

            assertEquals(File("files", filename).absolutePath, destFile.absolutePath)
            assertEquals(0, updatedKalam.id)
            assertEquals("Kalam Title", updatedKalam.title)
            assertEquals("", updatedKalam.onlineSource)
            assertEquals(filename, updatedKalam.offlineSource)
            assertEquals(0, updatedKalam.isFavorite)
            assertEquals(0, updatedKalam.playlistId)

            coVerify(exactly = 1) { kalamRepository.insert(any()) }
            verify(exactly = 1) { splitFile.moveTo(destFile) }
            verify(exactly = 1) { completable.subscribeOn(any()) }
            verify(exactly = 1) { completable.observeOn(any()) }
            verify(exactly = 1) { completable.subscribe() }

            assertEquals(
                realContext.getString(R.string.dynamic_kalam_saved).format("Kalam Title"),
                textSlot.captured,
            )
        }
    }

    @Ignore("will be fixed later")
    @Test
    fun testMarkAsFavorite_shouldUpdate_kalamWithFavoriteStatus() {
        launchViewModelScope(kalamViewModel) { slot ->
            // val addToFavoriteStrategy = mockk<AddToFavoriteStrategy>()
            // every { favoriteChangeFactory.create<AddToFavoriteStrategy>(any()) } returns
            // addToFavoriteStrategy
            // coEvery { addToFavoriteStrategy.change(any()) } returns Unit

            // kalamViewModel.onEvent(KalamEvents.MarkAsFavoriteKalam(sampleKalam()))

            slot.invoke()

            // verify { favoriteChangeFactory.create<AddToFavoriteStrategy>(any()) }
            // coVerify { addToFavoriteStrategy.change(any()) }
        }
    }

    @Ignore("will be fixed later")
    @Test
    fun testRemoveFavorite_shouldUpdate_kalamWithRemoveStatus() {
        launchViewModelScope(kalamViewModel) { slot ->
            // val removeFromFavoriteStrategy = mockk<RemoveFromFavoriteStrategy>()
            // every { favoriteChangeFactory.create<RemoveFromFavoriteStrategy>(any()) } returns
            // removeFromFavoriteStrategy
            // coEvery { removeFromFavoriteStrategy.change(any()) } returns Unit

            // kalamViewModel.onEvent(KalamEvents.RemoveFavoriteKalam(sampleKalam()))

            slot.invoke()

            // verify { favoriteChangeFactory.create<RemoveFromFavoriteStrategy>(any()) }
            // coVerify { removeFromFavoriteStrategy.change(any()) }
        }
    }
}
