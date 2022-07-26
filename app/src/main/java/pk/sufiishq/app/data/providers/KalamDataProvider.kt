package pk.sufiishq.app.data.providers

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import pk.sufiishq.app.models.Kalam
import java.io.File

interface KalamDataProvider {

    fun init(trackType: String, playlistId: Int = 0)
    fun getKalamDataFlow(): Flow<PagingData<Kalam>>
    fun searchKalam(keyword: String, trackType: String, playlistId: Int)
    fun update(kalam: Kalam)
    fun delete(kalam: Kalam, trackType: String)
    fun save(sourceKalam: Kalam, splitFile: File, kalamTitle: String)
    fun countAll(): LiveData<Int>
    fun countFavorites(): LiveData<Int>
    fun countDownloads(): LiveData<Int>
}