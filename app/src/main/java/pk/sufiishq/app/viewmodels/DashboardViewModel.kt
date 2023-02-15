package pk.sufiishq.app.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import pk.sufiishq.app.core.firebase.HighlightManager
import pk.sufiishq.app.data.controller.DashboardController
import pk.sufiishq.app.data.repository.KalamRepository
import pk.sufiishq.app.data.repository.PlaylistRepository
import pk.sufiishq.app.models.Highlight
import pk.sufiishq.app.models.NavigationItem

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val kalamRepository: KalamRepository,
    private val highlightManager: HighlightManager,
    private val playlistRepository: PlaylistRepository,
    private val mainNavigationItems: List<NavigationItem>
) : ViewModel(), DashboardController {

    override fun getMainNavigationItems(): List<NavigationItem> = mainNavigationItems
    override fun countAll(): LiveData<Int> = kalamRepository.countAll()
    override fun countFavorites(): LiveData<Int> = kalamRepository.countFavorites()
    override fun countDownloads(): LiveData<Int> = kalamRepository.countDownloads()
    override fun countPlaylist(): LiveData<Int> = playlistRepository.countAll()

    // -------------------------------------------------------------------- //
    // highlight available check functionality
    // -------------------------------------------------------------------- //

    override fun getHighlightAvailable(): LiveData<Highlight?> {
        return highlightManager.getHighlightAvailable()
    }
}