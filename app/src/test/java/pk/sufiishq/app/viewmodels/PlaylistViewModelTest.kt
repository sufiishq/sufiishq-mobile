package pk.sufiishq.app.viewmodels

import androidx.lifecycle.MutableLiveData
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import pk.sufiishq.app.SufiIshqTest
import pk.sufiishq.app.data.repository.KalamRepository
import pk.sufiishq.app.data.repository.PlaylistRepository
import pk.sufiishq.app.models.Playlist

class PlaylistViewModelTest : SufiIshqTest() {

    private lateinit var playlistViewModel: PlaylistViewModel
    private lateinit var playlistRepository: PlaylistRepository
    private lateinit var kalamRepository: KalamRepository

    @Before
    fun setUp() {
        playlistRepository = mockk()
        kalamRepository = mockk()
        playlistViewModel = PlaylistViewModel(playlistRepository, kalamRepository, Dispatchers.Main)
    }

    @Test
    fun testGetAll_shouldReturn_allPlaylist() {
        every { playlistRepository.loadAll() } returns MutableLiveData(
            listOf(
                Playlist(1, "Karachi"),
                Playlist(2, "Lahore")
            )
        )

        playlistViewModel.getAll().observe(mockLifecycleOwner()) {
            assertTrue(it.size == 2)
        }
    }

    @Test
    fun testGet_shouldFindAndReturn_singlePlaylist() {
        every { playlistRepository.load(1) } returns MutableLiveData(
            Playlist(1, "Jhang")
        )

        playlistViewModel.get(1).observe(mockLifecycleOwner()) {
            assertEquals(1, it.id)
            assertEquals("Jhang", it.title)
        }
    }

    @Test
    fun testAdd_shouldAdd_playlistInDatabase() {

        launchViewModelScope(playlistViewModel) { slot ->
            coEvery { playlistRepository.add(any()) } returns Unit
            playlistViewModel.add(mockk())
            slot.invoke()
            coVerify(exactly = 1) { playlistRepository.add(any()) }
        }

    }

    @Test
    fun testUpdate_shouldUpdate_givePlaylist() {

        launchViewModelScope(playlistViewModel) { slot ->
            coEvery { playlistRepository.update(any()) } returns Unit
            playlistViewModel.update(mockk())
            slot.invoke()
            coVerify(exactly = 1) { playlistRepository.update(any()) }
        }
    }

    @Test
    fun testDelete_should_resetAndDeletePlaylist() {

        launchViewModelScope(playlistViewModel) { slot ->
            every { kalamRepository.loadAllPlaylistKalam(any()) } returns MutableLiveData(
                listOf(sampleKalam(), sampleKalam())
            )

            coEvery { kalamRepository.update(any()) } returns Unit
            coEvery { playlistRepository.delete(any()) } returns Unit

            playlistViewModel.delete(Playlist(1, ""))

            slot.invoke()
            coVerify(exactly = 2) { kalamRepository.update(any()) }
            coVerify(exactly = 1) { playlistRepository.delete(any()) }
        }
    }

    @Test
    fun testCountAll_shouldReturn_playlistCount() {
        every { playlistRepository.countAll() } returns MutableLiveData(10)
        playlistViewModel.countAll().observe(mockLifecycleOwner()) {
            assertEquals(10, it)
        }
    }
}