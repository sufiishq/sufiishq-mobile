package pk.sufiishq.app.data.providers

import androidx.lifecycle.LiveData
import pk.sufiishq.app.core.downloader.KalamDownloadState
import pk.sufiishq.app.models.KalamInfo

interface PlayerDataProvider {

    fun getKalamDownloadState(): LiveData<KalamDownloadState>
    fun getShuffleState(): LiveData<Boolean>
    fun getMenuItems(): List<String>
    fun getKalamInfo(): LiveData<KalamInfo?>
}