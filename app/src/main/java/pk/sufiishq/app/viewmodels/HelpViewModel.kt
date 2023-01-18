package pk.sufiishq.app.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import pk.sufiishq.app.data.providers.HelpDataProvider
import pk.sufiishq.app.data.repository.HelpContentRepository
import pk.sufiishq.app.models.HelpContent

@HiltViewModel
class HelpViewModel @Inject constructor(
    private val helpContentRepository: HelpContentRepository
) : ViewModel(), HelpDataProvider {

    override fun getHelpContent(): LiveData<List<HelpContent>> {
        return helpContentRepository
            .loadContent()
            .asLiveData()
    }

}