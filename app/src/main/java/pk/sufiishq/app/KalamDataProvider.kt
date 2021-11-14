package pk.sufiishq.app

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface KalamDataProvider {

    fun getKalamDataFlow(): Flow<PagingData<Track>>
    fun searchKalam(keyword: String)
    fun getSeekbarValue(): LiveData<Float>
    fun updateSeekbarValue(value: Float)
    fun getSeekbarAccess(): LiveData<Boolean>
    fun onSeekbarChanged(value: Float)
    fun getPlayerState(): LiveData<PlayerState>
    fun doPlayOrPause()
    fun getActiveKalam(): LiveData<Track>
    fun changeTrack(track: Track)
}