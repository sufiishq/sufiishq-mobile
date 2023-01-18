package pk.sufiishq.app.data.providers

import androidx.lifecycle.LiveData
import pk.sufiishq.app.models.HelpContent

interface HelpDataProvider {
    fun getHelpContent(): LiveData<List<HelpContent>>
}