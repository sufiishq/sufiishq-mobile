package pk.sufiishq.app.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import pk.sufiishq.app.data.providers.HomeDataProvider
import pk.sufiishq.app.data.repository.KalamRepository
import pk.sufiishq.app.data.repository.PlaylistRepository
import pk.sufiishq.app.models.Kalam
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val kalamRepository: KalamRepository,
    private val playlistRepository: PlaylistRepository
) : ViewModel(), HomeDataProvider {

    private val showUpdateDialog = MutableLiveData(true)

    override fun setShowUpdateDialog(value: Boolean) {
        showUpdateDialog.postValue(value)
    }

    override fun getShowUpdateDialog(): LiveData<Boolean> {
        return showUpdateDialog
    }

    override fun getKalam(id: Int): LiveData<Kalam?> {
        return kalamRepository.getKalam(id)
    }

    override fun countAll(): LiveData<Int> {
        return kalamRepository.countAll()
    }

    override fun countFavorites(): LiveData<Int> {
        return kalamRepository.countFavorites()
    }

    override fun countDownloads(): LiveData<Int> {
        return kalamRepository.countDownloads()
    }

    override fun countPlaylist(): LiveData<Int> {
        return playlistRepository.countAll()
    }
}