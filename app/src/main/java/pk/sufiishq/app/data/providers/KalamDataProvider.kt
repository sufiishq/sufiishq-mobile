package pk.sufiishq.app.data.providers

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import pk.sufiishq.app.helpers.KalamSplitManager
import pk.sufiishq.app.helpers.TrackListType
import pk.sufiishq.app.models.Kalam
import java.io.File

interface KalamDataProvider {

    fun init(trackListType: TrackListType)
    fun getKalamDataFlow(): Flow<PagingData<Kalam>>
    fun searchKalam(keyword: String, trackListType: TrackListType)
    fun update(kalam: Kalam)
    fun delete(kalam: Kalam, trackType: String)
    fun saveSplitKalam(sourceKalam: Kalam, splitFile: File, kalamTitle: String)
    fun markAsFavorite(kalam: Kalam)
    fun removeFavorite(kalam: Kalam)
    fun getKalamSplitManager(): KalamSplitManager
    fun getActiveSearchKeyword(): String
}