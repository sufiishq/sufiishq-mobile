package pk.sufiishq.app.viewmodels

import android.os.Looper.getMainLooper
import androidx.lifecycle.MutableLiveData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.robolectric.Shadows.shadowOf
import pk.sufiishq.app.SufiIshqApp
import pk.sufiishq.app.SufiIshqTest
import pk.sufiishq.app.core.event.exception.UnhandledEventException
import pk.sufiishq.app.data.repository.KalamRepository
import pk.sufiishq.app.data.repository.PlaylistRepository
import pk.sufiishq.app.models.Playlist
import pk.sufiishq.app.utils.copyWithDefaults

class PlaylistViewModelTest : SufiIshqTest() {

    private lateinit var playlistViewModel: PlaylistViewModel
    private lateinit var playlistRepository: PlaylistRepository
    private lateinit var kalamRepository: KalamRepository
    private lateinit var appContext: SufiIshqApp

    @Before
    fun setUp() {
        playlistRepository = mockk()
        kalamRepository = mockk()
        appContext = mockApp()
        playlistViewModel = PlaylistViewModel(playlistRepository, kalamRepository, mockk())
    }

    @Ignore("will be fixed later")
    @Test
    fun testShowPlaylistAddUpdateDialog_shouldVerify_isShow() {
        val playlist = Playlist(1, "Islamabad")
        //playlistViewModel.onEvent(PlaylistEvents.ShowAddUpdatePlaylistDialog(playlist))

        shadowOf(getMainLooper()).idle()
        playlistViewModel.showAddUpdatePlaylistDialog().observe(mockLifecycleOwner()) {
            assertNotNull(it)
            assertEquals(playlist.id, it?.id)
            assertEquals(playlist.title, it?.title)
        }
    }

    @Ignore("will be fixed later")
    @Test
    fun testShowPlaylistConfirmDeleteDialog_shouldVerify_isShow() {
        val playlist = Playlist(1, "Islamabad")
        //playlistViewModel.onEvent(PlaylistEvents.ShowConfirmDeletePlaylistDialog(playlist))

        shadowOf(getMainLooper()).idle()
        playlistViewModel.showConfirmDeletePlaylistDialog().observe(mockLifecycleOwner()) {
            assertNotNull(it)
            assertEquals(playlist.id, it?.id)
            assertEquals(playlist.title, it?.title)
        }
    }

    @Ignore("will be fixed later")
    @Test
    fun testAdd_shouldVerify_playlistAddInDatabase() {
        launchViewModelScope(playlistViewModel) { slot ->
            coEvery { playlistRepository.add(any()) } returns Unit
            //playlistViewModel.onEvent(PlaylistEvents.Add(mockk()))
            slot.invoke()
            coVerify { playlistRepository.add(any()) }
        }
    }

    @Ignore("will be fixed later")
    @Test
    fun testUpdate_shouldVerify_playlistUpdateInDatabase() {
        launchViewModelScope(playlistViewModel) { slot ->
            coEvery { playlistRepository.update(any()) } returns Unit
            //playlistViewModel.onEvent(PlaylistEvents.Update(mockk()))
            slot.invoke()
            coVerify { playlistRepository.update(any()) }
        }
    }

    @Ignore("will be fixed later")
    @Test
    fun testDelete_shouldVerify_playlistDeleteAndResetRespectiveKalam() {
        launchViewModelScope(playlistViewModel) { slot ->
            val kalam = sampleKalam().copyWithDefaults(playlistId = 1)
            every { kalamRepository.loadAllPlaylistKalam(any()) } returns MutableLiveData(
                listOf(kalam)
            )

            coEvery { kalamRepository.update(any()) } returns Unit
            coEvery { playlistRepository.delete(any()) } returns Unit

            //playlistViewModel.onEvent(PlaylistEvents.Delete(Playlist(1, "Karachi")))
            slot.invoke()

            assertEquals(0, kalam.playlistId)
            coVerify { kalamRepository.update(any()) }
            coVerify { playlistRepository.delete(any()) }
        }
    }

    @Ignore("will be fixed later")
    @Test(expected = UnhandledEventException::class)
    fun testUnknownEven_shouldReturn_unhandledEventException() {
        //playlistViewModel.onEvent(PlayerEvents.PlayPauseEvent)
    }

}