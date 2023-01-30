package pk.sufiishq.app.data.providers

import pk.sufiishq.app.models.NavigationItem

interface DashboardDataProvider {

    fun getMainNavigationItems(): List<NavigationItem>
}