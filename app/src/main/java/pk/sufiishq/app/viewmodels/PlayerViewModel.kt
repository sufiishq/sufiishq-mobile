package pk.sufiishq.app.viewmodels

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import pk.sufiishq.app.core.player.AudioPlayer
import pk.sufiishq.app.core.player.listener.PlayerStateListener
import pk.sufiishq.app.core.player.state.MediaState
import pk.sufiishq.app.core.player.state.mapToIdleState
import pk.sufiishq.app.core.player.state.mapToLoadingState
import pk.sufiishq.app.core.player.state.mapToPauseState
import pk.sufiishq.app.core.player.state.mapToPlayingState
import pk.sufiishq.app.core.player.state.mapToResumeState
import pk.sufiishq.app.data.providers.PlayerDataProvider
import pk.sufiishq.app.di.qualifier.AndroidMediaPlayer
import pk.sufiishq.app.models.KalamInfo

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class PlayerViewModel @Inject constructor(
    @AndroidMediaPlayer private val player: AudioPlayer,
) : ViewModel(), PlayerDataProvider, PlayerStateListener {

    private val kalamInfo = MutableLiveData<KalamInfo?>(null)
    private var seekbarEnableOnPlaying = true

    init {
        player.registerListener(this)
    }

    override fun getKalamInfo(): LiveData<KalamInfo?> {
        return kalamInfo
    }

    override fun updateSeekbarValue(value: Float) {
        seekbarEnableOnPlaying = false

        kalamInfo.value = kalamInfo.value?.copy(
            currentProgress = value.toInt()
        )
    }

    override fun onSeekbarChanged(value: Int) {
        seekbarEnableOnPlaying = true
        player.seekTo(value)
    }

    override fun doPlayOrPause() {
        player.doPlayOrPause()
    }

    private fun updateKalamInfo(updatedKalamInfo: KalamInfo) {
        kalamInfo.postValue(updatedKalamInfo)
    }

    override fun onStateChange(mediaState: MediaState) {

        val newKalamInfo = when (mediaState) {

            is MediaState.Loading -> mediaState.mapToLoadingState(kalamInfo.value!!)
            is MediaState.Playing -> mediaState.mapToPlayingState(
                kalamInfo.value,
                seekbarEnableOnPlaying
            )
            is MediaState.Pause -> mediaState.mapToPauseState()
            is MediaState.Resume -> mediaState.mapToResumeState()
            is MediaState.Idle, is MediaState.Stop, is MediaState.Complete, is MediaState.Error -> mediaState.mapToIdleState()
        }

        updateKalamInfo(newKalamInfo)
    }
}