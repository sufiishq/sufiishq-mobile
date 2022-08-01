package pk.sufiishq.app.data.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.test.core.app.ApplicationProvider
import io.mockk.*
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.robolectric.util.ReflectionHelpers.getField
import pk.sufiishq.app.SufiIshqTest
import pk.sufiishq.app.data.dao.KalamDao
import pk.sufiishq.app.helpers.Screen
import pk.sufiishq.app.models.Kalam

class KalamRepositoryTest : SufiIshqTest() {

    private lateinit var kalamRepository: KalamRepository
    private lateinit var kalamDao: KalamDao

    @Before
    fun setUp() {
        kalamDao = mockk()
        kalamRepository = KalamRepository(kalamDao)
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
    fun testSetTrackType_shouldUpdate_withGiveTrackType() {
        kalamRepository.setTrackType(Screen.Tracks.DOWNLOADS)
        assertEquals(Screen.Tracks.DOWNLOADS, getField<String>("trackType"))
    }

    @Test
    fun testSetPlaylistId_shouldUpdate_withGivePlaylistId() {
        kalamRepository.setPlaylistId(2)
        assertEquals(2, getField<Int>("playlistId"))
    }

    @Test
    fun testSetSearchKeyword_shouldUpdate_withGiveText() {
        kalamRepository.setSearchKeyword("Karachi")
        assertEquals("Karachi", getField<String>("searchKeyword"))
    }

    @Test
    fun testLoad_shouldVerify_allKalamCalls() {
        val searchSlot = slot<String>()
        every { kalamDao.getAllKalam(capture(searchSlot)) } returns mockk()

        kalamRepository.setSearchKeyword("Pakpattan")
        kalamRepository.setTrackType(Screen.Tracks.ALL)
        kalamRepository.load()

        verify(exactly = 1) { kalamDao.getAllKalam(searchSlot.captured) }
        assertEquals("%Pakpattan%", searchSlot.captured)
    }

    @Test
    fun testLoad_shouldVerify_downloadsKalamCalls() {
        val searchSlot = slot<String>()
        every { kalamDao.getDownloadsKalam(capture(searchSlot)) } returns mockk()

        kalamRepository.setSearchKeyword("Jhang")
        kalamRepository.setTrackType(Screen.Tracks.DOWNLOADS)
        kalamRepository.load()

        verify(exactly = 1) { kalamDao.getDownloadsKalam(searchSlot.captured) }
        assertEquals("%Jhang%", searchSlot.captured)
    }

    @Test
    fun testLoad_shouldVerify_favoritesKalamCalls() {
        val searchSlot = slot<String>()
        every { kalamDao.getFavoritesKalam(capture(searchSlot)) } returns mockk()

        kalamRepository.setSearchKeyword("Lahore")
        kalamRepository.setTrackType(Screen.Tracks.FAVORITES)
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
                capture(searchSlot)
            )
        } returns mockk()

        kalamRepository.setSearchKeyword("Dehli")
        kalamRepository.setTrackType(Screen.Tracks.PLAYLIST)
        kalamRepository.setPlaylistId(3)
        kalamRepository.load()

        verify(exactly = 1) {
            kalamDao.getPlaylistKalam(
                playlistIdSlot.captured,
                searchSlot.captured
            )
        }
        assertEquals(3, playlistIdSlot.captured)
        assertEquals("%Dehli%", searchSlot.captured)
    }

    @Test
    fun testLoadAllPlaylistKalam_shouldReturn_listOfKalam() {
        val playlistIdSlot = slot<Int>()
        every { kalamDao.getAllPlaylistKalam(capture(playlistIdSlot)) } returns MutableLiveData(
            listOf(mockk(), mockk())
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
        kalamRepository.countAll().observe(mockLifecycleOwner()) {
            assertEquals(100, it)
        }
    }

    @Test
    fun testCountDownloads_shouldReturn_downloadsKalamCount() {
        every { kalamDao.countDownloads() } returns MutableLiveData(70)
        kalamRepository.countDownloads().observe(mockLifecycleOwner()) {
            assertEquals(70, it)
        }
    }

    @Test
    fun testCountFavorites_shouldReturn_favoritesKalamCount() {
        every { kalamDao.countFavorites() } returns MutableLiveData(30)
        kalamRepository.countFavorites().observe(mockLifecycleOwner()) {
            assertEquals(30, it)
        }
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
    fun testLoadAllFromAssets_shouldReturn_listOfKalamLiveData() {

        mockkStatic(Observable::class)

        val slot = slot<ObservableOnSubscribe<List<Kalam>>>()
        val kalamListSLot = slot<List<Kalam>>()
        every { Observable.create(capture(slot)) } returns mockk()

        val context = ApplicationProvider.getApplicationContext<Context>()
        kalamRepository.loadAllFromAssets(context)

        slot.captured.subscribe(mockk {
            every { onNext(capture(kalamListSLot)) } returns Unit
        })


        assertEquals(407, kalamListSLot.captured.size)
    }

    private fun <T> getField(fieldName: String): T {
        return getField(kalamRepository, fieldName)
    }
}