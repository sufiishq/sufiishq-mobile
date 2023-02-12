package pk.sufiishq.app.data.providers

import androidx.lifecycle.LiveData
import pk.sufiishq.app.models.Highlight
import pk.sufiishq.app.models.NavigationItem

interface DashboardDataProvider {

    fun getMainNavigationItems(): List<NavigationItem>
    fun countAll(): LiveData<Int>
    fun countFavorites(): LiveData<Int>
    fun countDownloads(): LiveData<Int>
    fun countPlaylist(): LiveData<Int>

    fun getHighlightAvailable(): LiveData<Highlight?>
}