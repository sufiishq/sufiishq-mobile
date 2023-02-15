package pk.sufiishq.app.data.controller

import kotlinx.coroutines.flow.Flow
import pk.sufiishq.app.models.HelpContent

interface HelpController {
    fun getHelpContent(): Flow<List<HelpContent>>
}