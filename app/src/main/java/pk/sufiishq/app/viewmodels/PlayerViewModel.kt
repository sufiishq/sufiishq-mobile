package pk.sufiishq.app.viewmodels

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.BackpressureStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.exceptions.UndeliverableException
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.apache.commons.io.FilenameUtils
import pk.sufiishq.app.R
import pk.sufiishq.app.SufiIshqApp
import pk.sufiishq.app.data.providers.PlayerDataProvider
import pk.sufiishq.app.helpers.FileDownloader
import pk.sufiishq.app.helpers.PlayerState
import pk.sufiishq.app.models.Kalam
import pk.sufiishq.app.services.AudioPlayerService
import pk.sufiishq.app.services.PlayerController
import pk.sufiishq.app.utils.*
import timber.log.Timber
import java.io.File
import java.net.SocketException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.math.min

@HiltViewModel
class PlayerViewModel @Inject constructor(
    @ApplicationContext val appContext: Context,
    private val fileDownloader: FileDownloader
) : ViewModel(), PlayerDataProvider, AudioPlayerService.Listener {

    private val seekbarValue = MutableLiveData(0f)
    private val seekbarAccess = MutableLiveData(false)
    private val currentPosition = MutableLiveData(0)
    private val totalDuration = MutableLiveData(0)
    private val activeKalam = MutableLiveData<Kalam?>()
    private val playerState = MutableLiveData(PlayerState.IDLE)
    private val shuffleState = MutableLiveData(IS_SHUFFLE_ON.getFromStorage(false))
    private var playerController: PlayerController? = null

    private val downloadProgress = MutableLiveData(0f)
    private val downloadError = MutableLiveData("")
    private var fileDownloaderDisposable = Disposables.disposed()
    private var fileMoveDisposables = Disposables.disposed()

    init {
        RxJavaPlugins.setErrorHandler { e ->
            if (e is UndeliverableException) {
                Timber.e(e)
            }
        }
    }

    fun setPlayerService(playerService: PlayerController?) {
        this.playerController = playerService
    }

    override fun getSeekbarValue(): LiveData<Float> {
        return seekbarValue
    }

    override fun updateSeekbarValue(value: Float) {
        seekbarValue.value = value
        totalDuration.value?.let {
            currentPosition.value = (value / 100f * it.toFloat()).toInt()
        }
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
            } else {
                it.doPlay()
            }
        }
    }

    override fun getActiveKalam(): LiveData<Kalam?> {
        return activeKalam
    }

    override fun changeTrack(kalam: Kalam, trackType: String, playlistId: Int) {
        if (kalam.canPlay(appContext)) {
            currentPosition.value = 0
            playerController?.setActiveTrack(kalam, trackType, playlistId)
            playerController?.doPlay()
        }
    }

    override fun playNext() {
        getPlayerState().value?.let {
            if (it != PlayerState.LOADING) {
                currentPosition.value = 0
                playerController?.playNext()
            }
        }

    }

    override fun playPrevious() {
        getPlayerState().value?.let {
            if (it != PlayerState.LOADING) {
                currentPosition.value = 0
                playerController?.playPrevious()
            }
        }
    }

    private fun playStart() {
        playerState.value = PlayerState.PLAYING
        seekbarAccess.value = true
    }

    override fun getShuffleState(): LiveData<Boolean> {
        return shuffleState
    }

    override fun setShuffleState(shuffle: Boolean) {
        IS_SHUFFLE_ON.putInStorage(shuffle)
        shuffleState.postValue(IS_SHUFFLE_ON.getFromStorage(false))
    }

    override fun getCurrentPosition(): LiveData<Int> {
        return currentPosition
    }

    override fun getTotalDuration(): LiveData<Int> {
        return totalDuration
    }

    override fun getMenuItems(): List<String> {
        return listOf(
            appContext.getString(R.string.mark_as_favorite),
            appContext.getString(R.string.remove_favorite),
            appContext.getString(R.string.download),
            appContext.getString(R.string.add_to_playlist),
        )
    }

    /*=======================================*/
    // KALAM DOWNLOAD
    /*=======================================*/

    override fun getDownloadProgress(): LiveData<Float> {
        return downloadProgress
    }

    override fun getDownloadError(): LiveData<String> {
        return downloadError
    }

    override fun setDownloadError(error: String) {
        downloadError.postValue(error)
    }

    override fun startDownload(kalam: Kalam) {

        setDownloadError("")
        downloadProgress.postValue(0f)

        val fileName = FilenameUtils.getName(kalam.onlineSource)
        fileDownloaderDisposable = fileDownloader.download(kalam.onlineSource, cacheDir(fileName))
            .throttleFirst(2, TimeUnit.SECONDS)
            .toFlowable(BackpressureStrategy.LATEST)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Timber.d("kalam: ${kalam.title}, download progress: $it")
                downloadProgress.postValue(min(it, 99).toFloat() / 100f * 1f)
            }, {
                Timber.e(it)
                if (it is SocketException || it is UnknownHostException) {
                    setDownloadError("Internet connection failed")
                } else {
                    setDownloadError(it.localizedMessage ?: it.message ?: "Unknown error")
                }
            }, {
                Timber.d("download completed")

                val source = cacheDir(fileName)
                val destination = kalamDir(fileName)

                Timber.d("file moving from ${source.absolutePath} to ${destination.absolutePath}")

                fileMoveDisposables = source.moveTo(destination)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        downloadProgress.postValue(100f)
                    }
            })
    }

    override fun disposeDownload() {
        fileDownloaderDisposable.dispose()
        fileMoveDisposables.dispose()
    }

    private fun kalamDir(fileName: String): File {
        val kalamDir = File("${appContext.filesDir.absolutePath}/$KALAM_DIR")
        if (!kalamDir.exists()) kalamDir.mkdir()
        return File(kalamDir, fileName)
    }

    private fun cacheDir(fileName: String): File {
        return File(appContext.cacheDir, fileName)
    }

    /*=======================================*/
    // PLAYER LISTENER
    /*=======================================*/

    override fun initService(kalam: Kalam) {
        activeKalam.value = kalam
        playerController?.let {
            seekbarValue.value = it.getCurrentProgress()
            if (it.isPlaying()) {
                playerState.value = PlayerState.PLAYING
                seekbarAccess.value = true
            }
        }
    }

    override fun onTrackUpdated(kalam: Kalam) {
        activeKalam.value = kalam
        seekbarValue.value = 0f
    }

    override fun onTrackLoading() {
        playerState.value = PlayerState.LOADING
        seekbarAccess.value = false
    }

    override fun onPlayStart() {
        playStart()
    }

    override fun onPause() {
        playerState.value = PlayerState.PAUSE
    }

    override fun onResume() {
        playStart()
    }

    override fun onError(ex: Exception) {
        Toast.makeText(SufiIshqApp.getInstance(), ex.message, Toast.LENGTH_LONG).show()
        playerState.value = PlayerState.IDLE
        seekbarValue.value = 0f
        seekbarAccess.value = false
    }

    override fun onCompleted(kalam: Kalam) {
        super.onCompleted(kalam)
        playerState.value = PlayerState.IDLE
        seekbarValue.value = 0f
        seekbarAccess.value = false
    }

    override fun onProgressChanged(currentPosition: Int, progress: Float) {
        seekbarValue.value = progress
        this.currentPosition.value = currentPosition
    }

    override fun onTrackLoaded(totalDuration: Int) {
        this.totalDuration.value = totalDuration
        playerState.value = PlayerState.IDLE
    }
}