package pk.sufiishq.app.data.providers

import androidx.lifecycle.LiveData

interface GlobalDataProvider {

    fun getShowUpdateButton(): LiveData<Boolean>
}