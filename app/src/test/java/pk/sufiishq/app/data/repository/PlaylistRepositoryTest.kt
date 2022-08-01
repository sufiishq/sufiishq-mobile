package pk.sufiishq.app.data.repository

import androidx.lifecycle.MutableLiveData
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import pk.sufiishq.app.SufiIshqTest
import pk.sufiishq.app.data.dao.PlaylistDao
import pk.sufiishq.app.models.Playlist

class PlaylistRepositoryTest : SufiIshqTest() {

    private lateinit var playlistRepository: PlaylistRepository
    private lateinit var playlistDao: PlaylistDao

    @Before
    fun setUp() {
        playlistDao = mockk()
        playlistRepository = PlaylistRepository(playlistDao)
    }

    @Test
    fun testLoadAll_shouldReturn_allPlaylist() {
        every { playlistDao.getAll() } returns MutableLiveData(
            listOf(mockk(), mockk())
        )

        playlistRepository.loadAll().observe(mockLifecycleOwner()) {
            assertEquals(2, it.size)
        }
    }

    @Test
    fun testLoad_shouldReturn_playlistById() {
        val idSlot = slot<Int>()
        every { playlistDao.get(capture(idSlot)) } answers {
            MutableLiveData(Playlist(idSlot.captured, "Karachi"))
        }

        playlistRepository.load(1).observe(mockLifecycleOwner()) { playlist ->
            assertEquals(1, playlist.id)
            assertEquals("Karachi", playlist.title)
            verify(exactly = 1) { playlistDao.get(idSlot.captured) }
        }
    }

    @Test
    fun testAdd_shouldVerify_playlistAdd() = runBlocking {

        val playlistSlot = slot<Playlist>()
        coEvery { playlistDao.add(capture(playlistSlot)) } returns Unit

        playlistRepository.add(mockk())

        coVerify(exactly = 1) { playlistDao.add(playlistSlot.captured) }
    }

    @Test
    fun testUpdate_shouldVerify_playlistUpdate() = runBlocking {

        val playlistSlot = slot<Playlist>()
        coEvery { playlistDao.update(capture(playlistSlot)) } returns Unit

        playlistRepository.update(mockk())

        coVerify(exactly = 1) { playlistDao.update(playlistSlot.captured) }
    }

    @Test
    fun testDelete_shouldVerify_playlistDelete() = runBlocking {

        val playlistSlot = slot<Playlist>()
        coEvery { playlistDao.delete(capture(playlistSlot)) } returns Unit

        playlistRepository.delete(mockk())

        coVerify(exactly = 1) { playlistDao.delete(playlistSlot.captured) }
    }

    @Test
    fun testCountAll_shouldReturn_allPlaylistCount() {
        every { playlistDao.countAll() } returns MutableLiveData(5)

        playlistRepository.countAll().observe(mockLifecycleOwner()) {
            assertEquals(5, it)
        }
    }
}