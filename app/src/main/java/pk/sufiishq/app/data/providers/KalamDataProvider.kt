package pk.sufiishq.app.data.providers

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import pk.sufiishq.app.helpers.KalamSplitManager
import pk.sufiishq.app.models.Kalam
import pk.sufiishq.app.models.KalamDeleteItem

interface KalamDataProvider {

    fun getKalamDeleteConfirmDialog(): LiveData<KalamDeleteItem?>
    fun getKalamDataFlow(): Flow<PagingData<Kalam>>
    fun getKalamSplitManagerDialog(): LiveData<KalamSplitManager?>
    fun getKalamRenameDialog(): LiveData<Kalam?>
    fun getShowCircularProgressDialog(): LiveData<Boolean>
}