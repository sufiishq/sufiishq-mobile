package pk.sufiishq.app.data.providers

import androidx.lifecycle.LiveData
import pk.sufiishq.app.models.Kalam

interface HomeDataProvider {

    fun getKalam(id: Int): LiveData<Kalam?>
    fun countAll(): LiveData<Int>
    fun countFavorites(): LiveData<Int>
    fun countDownloads(): LiveData<Int>
    fun countPlaylist(): LiveData<Int>
}