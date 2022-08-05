package pk.sufiishq.app.data.providers

import androidx.lifecycle.LiveData
import pk.sufiishq.app.helpers.PlayerState
import pk.sufiishq.app.models.Kalam

interface PlayerDataProvider {

    fun getSeekbarValue(): LiveData<Float>
    fun updateSeekbarValue(value: Float)
    fun getSeekbarAccess(): LiveData<Boolean>
    fun onSeekbarChanged(value: Float)
    fun getPlayerState(): LiveData<PlayerState>
    fun doPlayOrPause()
    fun getActiveKalam(): LiveData<Kalam?>
    fun changeTrack(kalam: Kalam, trackType: String, playlistId: Int)
    fun playNext()
    fun playPrevious()
    fun getDownloadProgress(): LiveData<Float>
    fun getDownloadError(): LiveData<String>
    fun setDownloadError(error: String)
    fun startDownload(kalam: Kalam)
    fun disposeDownload()
    fun getShuffleState(): LiveData<Boolean>
    fun setShuffleState(shuffle: Boolean)
    fun getCurrentPosition(): LiveData<Int>
    fun getTotalDuration(): LiveData<Int>
    fun getMenuItems(): List<String>
}