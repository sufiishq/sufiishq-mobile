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

package pk.sufiishq.app.data.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.test.core.app.ApplicationProvider
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import pk.sufiishq.app.SufiIshqTest
import pk.sufiishq.app.data.dao.KalamDao
import pk.sufiishq.app.helpers.ScreenType
import pk.sufiishq.app.helpers.TrackListType
import pk.sufiishq.app.models.Kalam

class KalamRepositoryTest : SufiIshqTest() {

    private lateinit var kalamRepository: KalamRepository
    private lateinit var kalamDao: KalamDao

    @Before
    fun setUp() {
        kalamDao = mockk()
        kalamRepository = KalamRepository(kalamDao, Dispatchers.Main)
    }

    @Test
    fun testInsert_shouldVerify_kalamInsertion() = runBlocking {
        val kalam = sampleKalam()
        val kalamSlot = slot<Kalam>()

        coEvery { kalamDao.insert(capture(kalamSlot)) } returns Unit
        kalamRepository.insert(kalam)

        coVerify { kalamDao.insert(kalamSlot.captured) }
    }

    @Test
    fun testInsert_shouldVerify_kalamBatchInsertion() = runBlocking {
        val kalams = listOf(sampleKalam())
        val kalamSlot = slot<List<Kalam>>()

        coEvery { kalamDao.insertAll(capture(kalamSlot)) } returns Unit
        kalamRepository.insertAll(kalams)

        coVerify { kalamDao.insertAll(kalamSlot.captured) }
    }

    @Test
    fun testSetTrackListType_shouldUpdate_withGiveTrackListType() {
        kalamRepository.setTrackListType(TrackListType.Downloads())

        val trackListType = kalamRepository.getTrackListType()
        assertEquals(ScreenType.Tracks.DOWNLOADS, trackListType.type)
    }

    @Test
    fun testSetSearchKeyword_shouldUpdate_withGiveText() {
        kalamRepository.setSearchKeyword("Karachi")
        assertEquals("Karachi", kalamRepository.getSearchKeyword())
    }

    @Test
    fun testLoad_shouldVerify_allKalamCalls() {
        val searchSlot = slot<String>()
        every { kalamDao.getAllKalam(capture(searchSlot)) } returns mockk()

        kalamRepository.setSearchKeyword("Pakpattan")
        kalamRepository.setTrackListType(TrackListType.All())
        kalamRepository.load()

        verify(exactly = 1) { kalamDao.getAllKalam(searchSlot.captured) }
        assertEquals("%Pakpattan%", searchSlot.captured)
    }

    @Test
    fun testLoad_shouldVerify_downloadsKalamCalls() {
        val searchSlot = slot<String>()
        every { kalamDao.getDownloadsKalam(capture(searchSlot)) } returns mockk()

        kalamRepository.setSearchKeyword("Jhang")
        kalamRepository.setTrackListType(TrackListType.Downloads())
        kalamRepository.load()

        verify(exactly = 1) { kalamDao.getDownloadsKalam(searchSlot.captured) }
        assertEquals("%Jhang%", searchSlot.captured)
    }

    @Test
    fun testLoad_shouldVerify_favoritesKalamCalls() {
        val searchSlot = slot<String>()
        every { kalamDao.getFavoritesKalam(capture(searchSlot)) } returns mockk()

        kalamRepository.setSearchKeyword("Lahore")
        kalamRepository.setTrackListType(TrackListType.Favorites())
        kalamRepository.load()

        verify(exactly = 1) { kalamDao.getFavoritesKalam(searchSlot.captured) }
        assertEquals("%Lahore%", searchSlot.captured)
    }

    @Test
    fun testLoad_shouldVerify_playlistKalamCalls() {
        val playlistIdSlot = slot<Int>()
        val searchSlot = slot<String>()
        every {
            kalamDao.getPlaylistKalam(
                capture(playlistIdSlot),
                capture(searchSlot),
            )
        } returns mockk()

        kalamRepository.setSearchKeyword("Dehli")
        kalamRepository.setTrackListType(TrackListType.Playlist("Dehli", 3))
        kalamRepository.load()

        verify(exactly = 1) {
            kalamDao.getPlaylistKalam(
                playlistIdSlot.captured,
                searchSlot.captured,
            )
        }
        assertEquals(3, playlistIdSlot.captured)
        assertEquals("%Dehli%", searchSlot.captured)
    }

    @Test
    fun testLoadAllPlaylistKalam_shouldReturn_listOfKalam() {
        val playlistIdSlot = slot<Int>()
        every { kalamDao.getAllPlaylistKalam(capture(playlistIdSlot)) } returns
            MutableLiveData(
                listOf(mockk(), mockk()),
            )

        kalamRepository.loadAllPlaylistKalam(2).observe(mockLifecycleOwner()) {
            assertEquals(2, it.size)
        }

        verify(exactly = 1) { kalamDao.getAllPlaylistKalam(playlistIdSlot.captured) }
    }

    @Test
    fun testGetDefaultKalam_shouldReturn_defaultKalam() {
        every { kalamDao.getFirstKalam() } returns MutableLiveData(sampleKalam())
        kalamRepository.getDefaultKalam().observe(mockLifecycleOwner()) {
            val sampleKalam = sampleKalam()
            assertEquals(sampleKalam.id, it.id)
        }
    }

    @Test
    fun testCountAll_shouldReturn_allKalamCount() {
        every { kalamDao.countAll() } returns MutableLiveData(100)
        kalamRepository.countAll().observe(mockLifecycleOwner()) { assertEquals(100, it) }
    }

    @Test
    fun testCountDownloads_shouldReturn_downloadsKalamCount() {
        every { kalamDao.countDownloads() } returns MutableLiveData(70)
        kalamRepository.countDownloads().observe(mockLifecycleOwner()) { assertEquals(70, it) }
    }

    @Test
    fun testCountFavorites_shouldReturn_favoritesKalamCount() {
        every { kalamDao.countFavorites() } returns MutableLiveData(30)
        kalamRepository.countFavorites().observe(mockLifecycleOwner()) { assertEquals(30, it) }
    }

    @Test
    fun testUpdate_shouldVerify_kalamUpdateInDB() = runBlocking {
        val kalam = sampleKalam()
        val kalamSlot = slot<Kalam>()

        coEvery { kalamDao.update(capture(kalamSlot)) } returns Unit
        kalamRepository.update(kalam)

        coVerify(exactly = 1) { kalamDao.update(kalamSlot.captured) }
    }

    @Test
    fun testDelete_shouldVerify_kalamDeleteInDB() = runBlocking {
        val kalam = sampleKalam()
        val kalamSlot = slot<Kalam>()

        coEvery { kalamDao.delete(capture(kalamSlot)) } returns Unit
        kalamRepository.delete(kalam)

        coVerify(exactly = 1) { kalamDao.delete(kalamSlot.captured) }
    }

    @Test
    fun testLoadAllFromAssets_shouldReturn_listOfKalamLiveData() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val allKalams = kalamRepository.loadAllFromAssets(context)

        assertEquals(467, allKalams.size)
    }
}
