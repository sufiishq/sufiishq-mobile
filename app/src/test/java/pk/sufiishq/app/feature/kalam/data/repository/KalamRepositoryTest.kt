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

package pk.sufiishq.app.feature.kalam.data.repository

import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import pk.sufiishq.app.SufiIshqTest
import pk.sufiishq.app.feature.kalam.data.dao.KalamDao
import pk.sufiishq.app.feature.kalam.helper.TrackListType
import pk.sufiishq.app.helpers.ScreenType
import pk.sufiishq.app.utils.getOrAwaitValue
import javax.inject.Inject

@HiltAndroidTest
class KalamRepositoryTest : SufiIshqTest() {

    @get:Rule val hiltRule = HiltAndroidRule(this)

    private lateinit var kalamRepository: KalamRepository

    @Inject lateinit var kalamDao: KalamDao

    @Before
    fun setUp() {
        hiltRule.inject()
        kalamRepository = KalamRepository(kalamDao, Dispatchers.Main.immediate)
    }

    @Test
    fun testInsert_shouldVerify_kalamInsertion() = runBlocking {
        val kalam = sampleKalam().copy(id = 1)
        kalamRepository.insert(kalam)
        assertNotNull(kalamRepository.getKalam(1).getOrAwaitValue())
    }

    @Test
    fun testInsert_shouldVerify_kalamBatchInsertion() = runBlocking {
        val kalams = listOf(
            sampleKalam().copy(id = 2),
            sampleKalam().copy(id = 3),
        )
        kalamRepository.insertAll(kalams)

        assertNotNull(kalamRepository.getKalam(2).getOrAwaitValue())
        assertNotNull(kalamRepository.getKalam(3).getOrAwaitValue())
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
    fun testLoadAllPlaylistKalam_shouldReturn_listOfKalam() = runBlocking {
        val kalamList = listOf(
            sampleKalam().copy(id = 4, playlistId = 10),
            sampleKalam().copy(id = 5, playlistId = 10),
        )
        kalamRepository.insertAll(kalamList)

        val result = kalamRepository.loadAllPlaylistKalam(10).getOrAwaitValue()
        assertEquals(2, result?.size)
    }

    @Test
    fun testGetDefaultKalam_shouldReturn_defaultKalam() = runBlocking {
        kalamRepository.insert(sampleKalam().copy(id = 6))
        assertNotNull(kalamRepository.getDefaultKalam())
    }

    @Test
    fun testCountAll_shouldReturn_allKalamCount() = runBlocking {
        kalamRepository.insert(sampleKalam().copy(id = 7))
        assertTrue((kalamRepository.countAll().getOrAwaitValue() ?: 0) > 0)
    }

    @Test
    fun testCountDownloads_shouldReturn_downloadsKalamCount() = runBlocking {
        kalamRepository.insert(sampleKalam().copy(id = 7, offlineSource = "im_downloaded_:)"))
        assertTrue((kalamRepository.countDownloads().getOrAwaitValue() ?: 0) > 0)
    }

    @Test
    fun testCountFavorites_shouldReturn_favoritesKalamCount() = runBlocking {
        kalamRepository.insert(sampleKalam().copy(id = 8, isFavorite = 1))
        assertTrue((kalamRepository.countFavorites().getOrAwaitValue() ?: 0) > 0)
    }

    @Test
    fun testUpdate_shouldVerify_kalamUpdateInDB() = runBlocking {
        kalamRepository.insert(sampleKalam().copy(id = 9, title = "test kalam"))
        val kalam = kalamRepository.getKalam(9).getOrAwaitValue()!!.apply {
            title = "updated title"
        }
        kalamRepository.update(kalam)
        assertEquals("updated title", kalamRepository.getKalam(9).getOrAwaitValue()?.title)
    }

    @Test
    fun testDelete_shouldVerify_kalamDeleteInDB() = runBlocking {
        kalamRepository.insert(sampleKalam().copy(id = 10))
        val kalam = kalamRepository.getKalam(10).getOrAwaitValue()!!
        assertNotNull(kalam)
        kalamRepository.delete(kalam)
        assertNull(kalamRepository.getKalam(10).getOrAwaitValue())
    }

    @Test
    fun testLoadAllFromAssets_shouldReturn_listOfKalamLiveData() = runBlocking {
        val allKalams = kalamRepository.loadAllFromAssets(appContext)

        assertEquals(467, allKalams.size)
    }
}
