package pk.sufiishq.app.data.providers

import androidx.lifecycle.LiveData
import pk.sufiishq.app.models.KalamInfo

interface PlayerDataProvider {

    fun getKalamInfo(): LiveData<KalamInfo?>
    fun updateSeekbarValue(value: Float)
    fun onSeekbarChanged(value: Int)
    fun doPlayOrPause()
}