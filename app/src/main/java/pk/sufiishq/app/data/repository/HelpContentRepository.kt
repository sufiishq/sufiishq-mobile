package pk.sufiishq.app.data.repository

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import pk.sufiishq.app.core.help.HelpContentResolver
import pk.sufiishq.app.di.qualifier.OnlineResolver
import pk.sufiishq.app.models.HelpContent

class HelpContentRepository @Inject constructor(
    @OnlineResolver private val helpContentResolver: HelpContentResolver
) {

    fun loadContent(): Flow<List<HelpContent>> = helpContentResolver.resolve()
}