package pk.sufiishq.app.viewmodels

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.paging.compose.LazyPagingItems
import androidx.test.core.app.ApplicationProvider
import io.mockk.*
import io.reactivex.Completable
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import pk.sufiishq.app.R
import pk.sufiishq.app.SufiIshqApp
import pk.sufiishq.app.SufiIshqTest
import pk.sufiishq.app.data.repository.KalamRepository
import pk.sufiishq.app.helpers.ScreenType
import pk.sufiishq.app.models.Kalam
import pk.sufiishq.app.utils.KALAM_DIR
import pk.sufiishq.app.utils.copyWithDefaults
import pk.sufiishq.app.utils.moveTo
import pk.sufiishq.app.utils.toast
import java.io.File
import java.util.*

class KalamViewModelTest : SufiIshqTest() {

    private lateinit var kalamViewModel: KalamViewModel
    private lateinit var kalamRepository: KalamRepository
    private lateinit var context: Context
    private lateinit var realContext: Context
/*
    @Before
    fun setUp() {
        realContext = ApplicationProvider.getApplicationContext()
        context = mockk()
        kalamRepository = mockk {
            every { setTrackType(any()) } returns Unit
            every { setPlaylistId(any()) } returns Unit
            every { setSearchKeyword(any()) } returns Unit
        }

        kalamViewModel = KalamViewModel(context, kalamRepository, mockk())
    }

    @Test
    fun testLoad_shouldLoad_pagingSource() {
        every { kalamRepository.load() } returns mockk()
        kalamViewModel.pagingSource().invoke()
        verify(exactly = 1) { kalamRepository.load() }
    }

    @Test
    fun testInit_shouldSet_givenParamInKalamRepository() {

        kalamViewModel.init(ScreenType.Tracks.ALL, 1)

        kalamRepository.run {
            verify(exactly = 1) { setTrackType(ScreenType.Tracks.ALL) }
            verify(exactly = 1) { setPlaylistId(1) }
            verify(exactly = 1) { setSearchKeyword("") }
        }
    }

    @Test
    fun testGetKalamDataFlow_shouldReturn_NonNullObject() {
        assertNotNull(kalamViewModel.getKalamDataFlow())
    }

    @Test
    fun testSearchKalam_shouldSet_givenParamInKalamRepository() {

        kalamViewModel.searchKalam("Karachi", ScreenType.Tracks.DOWNLOADS, 2)

        kalamRepository.run {
            verify(exactly = 1) { setTrackType(ScreenType.Tracks.DOWNLOADS) }
            verify(exactly = 1) { setPlaylistId(2) }
            verify(exactly = 1) { setSearchKeyword("Karachi") }
        }
    }

    @Test
    fun testUpdate_shouldUpdate_givenKalamInDB() {
        launchViewModelScope(kalamViewModel) { slot ->
            coEvery { kalamRepository.update(any()) } returns Unit
            kalamViewModel.update(mockk())
            slot.invoke()
            coVerify(exactly = 1) { kalamRepository.update(any()) }
        }
    }

    @Test
    fun testDelete_shouldUpdateKalam_whenTrackTypeIsPlaylist() {
        launchViewModelScope(kalamViewModel) { slot ->
            val kalamSlot = slot<Kalam>()
            coEvery { kalamRepository.update(capture(kalamSlot)) } returns Unit
            val kalam = sampleKalam().copyWithDefaults(playlistId = 10)
            val trackType = ScreenType.Tracks.PLAYLIST
            kalamViewModel.delete(kalam, trackType)
            slot.invoke()
            assertEquals(0, kalamSlot.captured.playlistId)
            coVerify(exactly = 1) { kalamRepository.update(any()) }
        }
    }

    @Test
    fun testDelete_shouldUpdateKalam_whenSourceIsOnline() {

        every { context.filesDir } returns mockk {
            every { absolutePath } returns ""
        }

        val kalamViewModelSpy = spyk(kalamViewModel)
        launchViewModelScope(kalamViewModelSpy) { slot ->
            every { kalamViewModelSpy.canDelete(any()) } returns true
            val kalam = sampleKalam().copyWithDefaults()
            coEvery { kalamRepository.update(any()) } returns Unit
            kalamViewModelSpy.delete(kalam, ScreenType.Tracks.ALL)
            slot.invoke()
            coVerify(exactly = 1) { kalamRepository.update(any()) }
        }
    }

    @Test
    fun testDelete_shouldDeleteKalam_whenSourceIsOffline() {

        every { context.filesDir } returns mockk {
            every { absolutePath } returns ""
        }

        val kalamViewModelSpy = spyk(kalamViewModel)
        launchViewModelScope(kalamViewModelSpy) { slot ->
            every { kalamViewModelSpy.canDelete(any()) } returns true
            val kalam = sampleKalam().copyWithDefaults(
                onlineSource = ""
            )
            coEvery { kalamRepository.delete(any()) } returns Unit
            kalamViewModelSpy.delete(kalam, ScreenType.Tracks.ALL)
            slot.invoke()
            coVerify(exactly = 1) { kalamRepository.delete(any()) }
        }
    }

    @Test
    fun testDelete_shouldPreventToDelete_whenKalamIsCurrentlyPlaying() {

        mockkStatic(Context::toast)

        val textSlot = slot<String>()

        every { context.toast(capture(textSlot)) } returns Unit
        every { context.getString(any()) } answers {
            realContext.getString(firstArg())
        }
        val kalamViewModelSpy = spyk(kalamViewModel)

        launchViewModelScope(kalamViewModelSpy) { slot ->
            every { kalamViewModelSpy.canDelete(any()) } returns false
            val kalam = sampleKalam().copyWithDefaults(
                title = "test kalam"
            )

            kalamViewModelSpy.delete(kalam, ScreenType.Tracks.ALL)
            slot.invoke()
            assertEquals(
                realContext.getString(R.string.error_kalam_delete_on_playing).format("test kalam"),
                textSlot.captured
            )
        }
    }

    @Test
    fun testCanDelete_shouldReturnFalse_whenActiveTrackEqualToGiveTrack() {
        mockkObject(SufiIshqApp)

        every { SufiIshqApp.getInstance() } returns mockk {
            every { getPlayerController() } returns mockk {
                every { getActiveTrack() } returns sampleKalam().copyWithDefaults(
                    id = 10
                )
            }
        }

        val kalam = sampleKalam().copyWithDefaults(id = 10)

        assertFalse(kalamViewModel.canDelete(kalam))
    }

    @Test
    fun testCanDelete_shouldReturnTrue_whenActiveTrackNotEqualToGiveTrack() {
        mockkObject(SufiIshqApp)

        every { SufiIshqApp.getInstance() } returns mockk {
            every { getPlayerController() } returns mockk {
                every { getActiveTrack() } returns sampleKalam().copyWithDefaults(
                    id = 10
                )
            }
        }

        assertTrue(kalamViewModel.canDelete(sampleKalam()))
    }

    @Test
    fun testCanDelete_shouldReturnTrue_whenActiveTrackGetNull() {
        mockkObject(SufiIshqApp)

        every { SufiIshqApp.getInstance() } returns mockk {
            every { getPlayerController() } returns mockk {
                every { getActiveTrack() } returns null
            }
        }

        assertTrue(kalamViewModel.canDelete(sampleKalam()))
    }

    @Test
    fun testCanDelete_shouldReturnTrue_whenPlayerControllerGetNull() {
        mockkObject(SufiIshqApp)

        every { SufiIshqApp.getInstance() } returns mockk {
            every { getPlayerController() } returns null
        }

        assertTrue(kalamViewModel.canDelete(sampleKalam()))
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

            every { context.toast(capture(textSlot)) } returns Unit
            every { context.getString(any()) } answers {
                realContext.getString(firstArg())
            }
            coEvery { kalamRepository.insert(capture(kalamSlot)) } returns Unit

            every { context.filesDir } returns File("files")
            every { calendar.timeInMillis } returns 1000L
            every { Calendar.getInstance() } returns calendar
            every { splitFile.moveTo(capture(destinationSlot)) } returns completable
            every { completable.subscribeOn(any()) } returns completable
            every { completable.observeOn(any()) } returns completable
            every { completable.subscribe() } returns mockk()

            kalamViewModel.saveSplitKalam(sourceKalam, splitFile, "Kalam Title")
            slot.invoke()

            val updatedKalam = kalamSlot.captured
            val destFile = destinationSlot.captured
            val filename = "$KALAM_DIR/kalam_title_1000.mp3"

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
                context.getString(R.string.kalam_saved_label).format("Kalam Title"),
                textSlot.captured
            )
        }
    }

    @Test
    fun testCountAll_shouldReturn_allKalamCount() {
        every { kalamRepository.countAll() } returns MutableLiveData(100)
        kalamViewModel.countAll().observe(mockLifecycleOwner()) {
            assertEquals(100, it)
        }
    }

    @Test
    fun testCountFavorites_shouldReturn_allFavoriteCount() {
        every { kalamRepository.countFavorites() } returns MutableLiveData(50)
        kalamViewModel.countFavorites().observe(mockLifecycleOwner()) {
            assertEquals(50, it)
        }
    }

    @Test
    fun testCountDownloads_shouldReturn_allDownloadsCount() {
        every { kalamRepository.countDownloads() } returns MutableLiveData(20)
        kalamViewModel.countDownloads().observe(mockLifecycleOwner()) {
            assertEquals(20, it)
        }
    }

    @Test
    fun testMarkAsFavorite_shouldUpdate_kalamWithFavoriteStatus() {
        mockkStatic(Context::toast)

        val kalamViewModelSpy = spyk(kalamViewModel)
        val textSlot = slot<String>()
        val kalamSlot = slot<Kalam>()

        every { kalamViewModelSpy.update(capture(kalamSlot)) } returns Unit
        every { context.toast(capture(textSlot)) } returns Unit
        every { context.getString(any()) } answers {
            realContext.getString(firstArg())
        }

        val sourceKalam = sampleKalam()
        kalamViewModelSpy.markAsFavorite(sourceKalam)

        assertEquals(
            context.getString(R.string.favorite_added).format(sourceKalam.title),
            textSlot.captured
        )
        assertEquals(1, kalamSlot.captured.isFavorite)
        verify(exactly = 1) { kalamViewModelSpy.update(sourceKalam) }
    }

    @Test
    fun testMarkAsFavorite_shouldUpdate_kalamWithRemoveFavoriteStatus() {
        mockkStatic(Context::toast)

        val kalamViewModelSpy = spyk(kalamViewModel)
        val textSlot = slot<String>()
        val kalamSlot = slot<Kalam>()
        val searchSlot = slot<String>()
        val trackTypeSlot = slot<String>()
        val playlistIdSlot = slot<Int>()
        val lazyPagingItems = mockk<LazyPagingItems<Kalam>>()
        val kalamItemParam = KalamItemParam(
            sampleKalam(),
            mockk(),
            mockk(),
            mockk(),
            mockk(),
            lazyPagingItems,
            mockk(),
            mutableStateOf("new"),
            ScreenType.Tracks.ALL,
            0
        )

        every { lazyPagingItems.refresh() } returns Unit
        every { kalamViewModelSpy.update(capture(kalamSlot)) } returns Unit
        every {
            kalamViewModelSpy.searchKalam(
                capture(searchSlot),
                capture(trackTypeSlot),
                capture(playlistIdSlot)
            )
        } returns Unit
        every { context.toast(capture(textSlot)) } returns Unit
        every { context.getString(any()) } answers {
            realContext.getString(firstArg())
        }

        kalamViewModelSpy.removeFavorite(kalamItemParam)

        assertEquals(
            context.getString(R.string.favorite_removed).format(kalamItemParam.kalam.title),
            textSlot.captured
        )
        assertEquals(0, kalamSlot.captured.isFavorite)
        assertEquals(kalamItemParam.searchText.value, searchSlot.captured)
        assertEquals(kalamItemParam.trackType, trackTypeSlot.captured)
        assertEquals(kalamItemParam.playlistId, playlistIdSlot.captured)

        verify(exactly = 1) { kalamViewModelSpy.update(kalamItemParam.kalam) }
        verify(exactly = 1) {
            kalamViewModelSpy.searchKalam(
                kalamItemParam.searchText.value,
                kalamItemParam.trackType,
                kalamItemParam.playlistId
            )
        }
    }

    @Test
    fun testGetKalamSplitManager_shouldReturn_nonNullObject() {
        assertNotNull(kalamViewModel.getKalamSplitManager())
    }*/
}