package pk.sufiishq.app.data.providers

import androidx.lifecycle.LiveData
import pk.sufiishq.aurora.models.DataMenuItem

interface GlobalDataProvider {

    fun getShowUpdateButton(): LiveData<Boolean>
    fun popupMenuItems(): List<DataMenuItem>
}