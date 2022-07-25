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
    fun getActiveKalam(): LiveData<Kalam>
    fun changeTrack(kalam: Kalam)

    fun getDownloadProgress(): LiveData<Float>
    fun getDownloadError(): LiveData<String>
    fun setDownloadError(error: String)
    fun startDownload(kalam: Kalam)
    fun disposeDownload()
}