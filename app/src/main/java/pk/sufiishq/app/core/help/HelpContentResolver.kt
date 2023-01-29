package pk.sufiishq.app.core.help

import kotlinx.coroutines.flow.Flow
import pk.sufiishq.app.models.HelpContent

interface HelpContentResolver {
    fun resolve(): Flow<List<HelpContent>>
}