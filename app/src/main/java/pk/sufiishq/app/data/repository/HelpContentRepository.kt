package pk.sufiishq.app.data.repository

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import pk.sufiishq.app.core.help.InAppHelpContentResolver
import pk.sufiishq.app.models.HelpContent

class HelpContentRepository @Inject constructor(private val helpContentResolver: InAppHelpContentResolver) {

    fun loadContent(): Flow<List<HelpContent>> = helpContentResolver.resolve()
}