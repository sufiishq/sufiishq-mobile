package pk.sufiishq.app.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import pk.sufiishq.app.data.repository.KalamRepository
import pk.sufiishq.app.core.player.PlayerState
import pk.sufiishq.app.helpers.TrackListType
import pk.sufiishq.app.models.Kalam
import pk.sufiishq.app.models.KalamInfo
import pk.sufiishq.app.utils.LAST_PLAY_KALAM
import pk.sufiishq.app.utils.getFromStorage
import pk.sufiishq.app.utils.putInStorage

@HiltViewModel
class AssetKalamLoaderViewModel @Inject constructor(
    private val app: Application,
    private val gson: Gson,
    private val kalamRepository: KalamRepository
) : ViewModel() {

    fun countAll(): LiveData<Int> = kalamRepository.countAll()

    fun loadAllKalam() {
        viewModelScope.launch {
            kalamRepository.countAll().asFlow().collectLatest { count ->
                if (count <= 0) {

                    val allKalam = kalamRepository.loadAllFromAssets(app)
                    kalamRepository.insertAll(allKalam)
                    initDefaultKalam(allKalam[allKalam.size.minus(1)].copy(id = allKalam.size))

                } else if (LAST_PLAY_KALAM.getFromStorage("").isEmpty()) {
                    kalamRepository.getDefaultKalam().asFlow().collectLatest {
                        initDefaultKalam(it)
                    }
                }
            }
        }
    }

    private fun initDefaultKalam(kalam: Kalam) {

        LAST_PLAY_KALAM.putInStorage(
            gson.toJson(
                KalamInfo(
                    playerState = PlayerState.IDLE,
                    kalam = kalam,
                    currentProgress = 0,
                    totalDuration = 0,
                    enableSeekbar = false,
                    TrackListType.All()
                )
            )
        )
    }
}

