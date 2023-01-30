package pk.sufiishq.app.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import pk.sufiishq.app.data.providers.DashboardDataProvider
import pk.sufiishq.app.models.NavigationItem

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val mainNavigationItems: List<NavigationItem>
) : ViewModel(), DashboardDataProvider {

    override fun getMainNavigationItems(): List<NavigationItem> {
        return mainNavigationItems
    }
}