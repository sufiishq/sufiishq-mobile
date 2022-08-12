package pk.sufiishq.app.data.providers

import androidx.lifecycle.LiveData
import pk.sufiishq.app.helpers.TrackListType
import pk.sufiishq.app.models.Kalam
import pk.sufiishq.app.models.KalamInfo

interface PlayerDataProvider {

    fun updateSeekbarValue(value: Float)
    fun onSeekbarChanged(value: Int)
    fun doPlayOrPause()
    fun changeTrack(kalam: Kalam, trackListType: TrackListType)
    fun playNext()
    fun playPrevious()
    fun getDownloadProgress(): LiveData<Float>
    fun getDownloadError(): LiveData<String>
    fun setDownloadError(error: String)
    fun startDownload(kalam: Kalam)
    fun disposeDownload()
    fun getShuffleState(): LiveData<Boolean>
    fun setShuffleState(shuffle: Boolean)
    fun getMenuItems(): List<String>
    fun getKalamInfo(): LiveData<KalamInfo?>
}