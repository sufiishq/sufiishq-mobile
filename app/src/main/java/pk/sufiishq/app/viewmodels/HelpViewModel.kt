package pk.sufiishq.app.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import pk.sufiishq.app.data.providers.HelpDataProvider
import pk.sufiishq.app.data.repository.HelpContentRepository
import pk.sufiishq.app.models.HelpContent

@HiltViewModel
class HelpViewModel @Inject constructor(
    private val helpContentRepository: HelpContentRepository
) : ViewModel(), HelpDataProvider {

    override fun getHelpContent(): Flow<List<HelpContent>> {
        return helpContentRepository
            .loadContent()
    }

}