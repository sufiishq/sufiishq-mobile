package pk.sufiishq.app.core.help.resolver

import kotlinx.coroutines.flow.Flow
import pk.sufiishq.app.core.help.model.HelpContent

interface HelpContentResolver {
    fun resolve(): Flow<List<HelpContent>>
}