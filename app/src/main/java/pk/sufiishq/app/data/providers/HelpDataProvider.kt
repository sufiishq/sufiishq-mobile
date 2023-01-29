package pk.sufiishq.app.data.providers

import kotlinx.coroutines.flow.Flow
import pk.sufiishq.app.models.HelpContent

interface HelpDataProvider {
    fun getHelpContent(): Flow<List<HelpContent>>
}