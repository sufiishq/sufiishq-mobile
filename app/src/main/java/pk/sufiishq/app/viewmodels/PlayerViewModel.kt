package pk.sufiishq.app.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.BackpressureStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.exceptions.UndeliverableException
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.apache.commons.io.FilenameUtils
import pk.sufiishq.app.R
import pk.sufiishq.app.configs.AppConfig
import pk.sufiishq.app.core.player.AudioPlayer
import pk.sufiishq.app.core.player.listener.PlayerStateListener
import pk.sufiishq.app.core.player.state.MediaState
import pk.sufiishq.app.data.providers.PlayerDataProvider
import pk.sufiishq.app.data.repository.KalamRepository
import pk.sufiishq.app.di.qualifier.AndroidMediaPlayer
import pk.sufiishq.app.helpers.FileDownloader
import pk.sufiishq.app.helpers.PlayerState
import pk.sufiishq.app.helpers.TrackListType
import pk.sufiishq.app.models.Kalam
import pk.sufiishq.app.models.KalamInfo
import pk.sufiishq.app.utils.*
import timber.log.Timber
import java.io.File
import java.net.SocketException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.math.min

@HiltViewModel
@SuppressLint("StaticFieldLeak")
class PlayerViewModel @Inject constructor(
    @ApplicationContext val appContext: Context,
    @AndroidMediaPlayer private val player: AudioPlayer,
    private val fileDownloader: FileDownloader,
    private val kalamRepository: KalamRepository,
    private val appConfig: AppConfig
) : ViewModel(), PlayerDataProvider, PlayerStateListener {

    private val kalamInfo = MutableLiveData<KalamInfo?>(null)
    private var seekbarEnableOnPlaying = true
    private val shuffleState = MutableLiveData(IS_SHUFFLE_ON.getFromStorage(false))

    private val downloadProgress = MutableLiveData(0f)
    private val downloadError = MutableLiveData("")
    private var fileDownloaderDisposable = Disposables.disposed()
    private var fileMoveDisposables = Disposables.disposed()

    init {
        player.registerListener(this)
        RxJavaPlugins.setErrorHandler { e ->
            if (e is UndeliverableException) {
                Timber.e(e)
            }
        }
    }

    override fun updateSeekbarValue(value: Float) {
        seekbarEnableOnPlaying = false
        kalamInfo.value?.let { info ->
            info.currentProgress = value.toInt()
        }
    }

    override fun onSeekbarChanged(value: Int) {
        seekbarEnableOnPlaying = true
        player.seekTo(value)
    }

    override fun doPlayOrPause() {
        player.doPlayOrPause()
    }

    override fun changeTrack(kalam: Kalam, trackListType: TrackListType) {
        if (kalam.canPlay(appContext)) {
            player.setSource(kalam, trackListType)
            player.doPlayOrPause()
        }
    }

    override fun playNext() {
        viewModelScope.launch {
            kalamRepository.getNextKalam(
                player.getActiveTrack().id,
                player.getTrackListType(),
                appConfig.isShuffle()
            ).asFlow().collectLatest { nextKalam ->
                nextKalam?.let {
                    changeTrack(nextKalam, player.getTrackListType())
                }
            }
        }
    }

    override fun playPrevious() {
        viewModelScope.launch {
            kalamRepository.getPreviousKalam(
                player.getActiveTrack().id,
                player.getTrackListType(),
                appConfig.isShuffle()
            ).asFlow().collectLatest { previousKalam ->
                previousKalam?.let {
                    changeTrack(previousKalam, player.getTrackListType())
                }
            }
        }
    }

    override fun getShuffleState(): LiveData<Boolean> {
        return shuffleState
    }

    override fun setShuffleState(shuffle: Boolean) {
        IS_SHUFFLE_ON.putInStorage(shuffle)
        shuffleState.postValue(IS_SHUFFLE_ON.getFromStorage(false))
    }

    override fun getMenuItems(): List<String> {
        return listOf(
            appContext.getString(R.string.mark_as_favorite),
            appContext.getString(R.string.remove_favorite),
            appContext.getString(R.string.download_label),
            appContext.getString(R.string.add_to_playlist),
        )
    }

    override fun getKalamInfo(): LiveData<KalamInfo?> {
        return kalamInfo
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

    override fun onStateChange(mediaState: MediaState) {

        val newKalamInfo = when (mediaState) {

            is MediaState.Idle, is MediaState.Complete, is MediaState.Error -> {

                if (mediaState is MediaState.Error) {
                    appContext.toast(mediaState.message)
                }

                KalamInfo(PlayerState.IDLE, mediaState.kalam, 0, 0, false)
            }

            is MediaState.Loading -> {
                kalamInfo.value?.let {
                    KalamInfo(
                        PlayerState.LOADING,
                        mediaState.kalam,
                        it.currentProgress,
                        it.totalDuration,
                        false
                    )
                } ?: run {
                    kalamInfo.value
                }
            }

            is MediaState.Playing -> {
                if (seekbarEnableOnPlaying) {
                    KalamInfo(
                        PlayerState.PLAYING,
                        mediaState.kalam,
                        mediaState.currentProgress,
                        mediaState.totalDuration,
                        true
                    )
                } else {
                    kalamInfo.value
                }
            }

            is MediaState.Pause -> {
                KalamInfo(
                    PlayerState.PAUSE,
                    mediaState.kalam,
                    mediaState.currentProgress,
                    mediaState.totalDuration,
                    false
                )
            }

            is MediaState.Resume -> {
                KalamInfo(
                    PlayerState.PLAYING,
                    mediaState.kalam,
                    mediaState.currentProgress,
                    mediaState.totalDuration,
                    true
                )
            }

            else -> {
                kalamInfo.value
            }
        }

        kalamInfo.postValue(newKalamInfo)
    }
}