package pk.sufiishq.app

import android.widget.Toast
import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import java.lang.Exception

class KalamViewModel(private val kalamRepository: KalamRepository): ViewModel(), KalamDataProvider, AudioPlayerService.Listener {

    private var nextPage = 1
    private var searchKeyword = ""
    private var kalamSource = KalamSource(kalamRepository)

    private val seekbarValue = MutableLiveData(0f)
    private val seekbarAccess = MutableLiveData(false)
    private val activeKalam = MutableLiveData(kalamRepository.getDefaultKalam())
    private val playerState = MutableLiveData(PlayerState.IDLE)
    private var playerController: PlayerController? = null

    private var kalams: Flow<PagingData<Track>> = Pager(PagingConfig(pageSize = 10)) {
        kalamSource
    }.flow

    fun init() {
        nextPage = 1
        searchKeyword = ""
        kalamSource = KalamSource(kalamRepository)
        kalams = Pager(PagingConfig(pageSize = 10)) {
            kalamSource
        }.flow
    }

    fun setPlayerService(playerService: PlayerController?) {
        this.playerController = playerService
    }

    override fun getKalamDataFlow(): Flow<PagingData<Track>> {
        return kalams
    }

    override fun searchKalam(keyword: String) {
        searchKeyword = keyword
        nextPage = 1
        kalamSource = KalamSource(kalamRepository, searchKeyword)
    }

    override fun getSeekbarValue(): LiveData<Float> {
        return seekbarValue
    }

    override fun updateSeekbarValue(value: Float) {
        seekbarValue.value = value
    }

    override fun getSeekbarAccess(): LiveData<Boolean> {
        return seekbarAccess
    }

    override fun onSeekbarChanged(value: Float) {
        playerController?.seekTo(value)
    }

    override fun getPlayerState(): LiveData<PlayerState> {
        return playerState
    }

    override fun doPlayOrPause() {
        playerController?.let {
            if (it.isPlaying()) {
                it.doPause()
            }
            else {
                it.doPlay()
            }
        }
    }

    override fun getActiveKalam(): LiveData<Track> {
        return activeKalam
    }

    override fun changeTrack(track: Track) {
        activeKalam.value = track
        playerController?.setActiveTrack(track)
        playerController?.doPlay()
    }
    /*=======================================*/
    // PLAYER LISTENER
    /*=======================================*/

    override fun initService(track: Track) {
        activeKalam.value = track
        playerController?.let {
            seekbarValue.value = it.getCurrentProgress()
            if (it.isPlaying()) {
                playerState.value = PlayerState.PLAYING
                seekbarAccess.value = true
            }
        }
    }

    override fun onTrackUpdated(track: Track) {
        activeKalam.value = track
    }

    override fun onTrackLoading() {
        playerState.value = PlayerState.LOADING
        seekbarAccess.value = false
    }

    override fun onPlayStart() {
        playerState.value = PlayerState.PLAYING
        seekbarAccess.value = true
    }

    override fun onPause() {
        playerState.value = PlayerState.PAUSE
    }

    override fun onResume() {
        playerState.value = PlayerState.PLAYING
        seekbarAccess.value = true
    }

    override fun onError(ex: Exception) {
        Toast.makeText(SufiIshqApp.getInstance(), ex.message, Toast.LENGTH_LONG).show()
        playerState.value = PlayerState.IDLE
        seekbarValue.value = 0f
        seekbarAccess.value = false
    }

    override fun onCompleted(track: Track) {
        super.onCompleted(track)
        playerState.value = PlayerState.IDLE
        seekbarValue.value = 0f
        seekbarAccess.value = false
    }

    override fun onProgressChanged(progress: Float) {
        super.onProgressChanged(progress)
        seekbarValue.value  = progress
    }

    companion object {
        fun get(owner: ViewModelStoreOwner, kalamRepository: KalamRepository): KalamViewModel {
            return ViewModelProvider(owner, object: ViewModelProvider.Factory{
                override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                    return KalamViewModel(kalamRepository) as T
                }
            }).get(KalamViewModel::class.java)
        }
    }
}