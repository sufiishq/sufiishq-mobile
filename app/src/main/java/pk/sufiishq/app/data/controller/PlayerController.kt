package pk.sufiishq.app.data.controller

import androidx.lifecycle.LiveData
import pk.sufiishq.app.models.KalamInfo

interface PlayerController {

    fun getKalamInfo(): LiveData<KalamInfo?>
    fun updateSeekbarValue(value: Float)
    fun onSeekbarChanged(value: Int)
    fun doPlayOrPause()
}