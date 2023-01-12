package pk.sufiishq.app.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.BackpressureStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.exceptions.UndeliverableException
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.net.SocketException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.apache.commons.io.FilenameUtils
import pk.sufiishq.app.R
import pk.sufiishq.app.SufiIshqApp
import pk.sufiishq.app.core.downloader.FileDownloader
import pk.sufiishq.app.core.downloader.KalamDownloadState
import pk.sufiishq.app.core.event.dispatcher.EventDispatcher
import pk.sufiishq.app.core.event.events.Event
import pk.sufiishq.app.core.event.events.PlayerEvents
import pk.sufiishq.app.core.event.exception.UnhandledEventException
import pk.sufiishq.app.core.player.AudioPlayer
import pk.sufiishq.app.core.player.listener.PlayerStateListener
import pk.sufiishq.app.core.player.state.MediaState
import pk.sufiishq.app.data.providers.PlayerDataProvider
import pk.sufiishq.app.data.repository.KalamRepository
import pk.sufiishq.app.di.qualifier.AndroidMediaPlayer
import pk.sufiishq.app.helpers.PlayerState
import pk.sufiishq.app.helpers.TrackListType
import pk.sufiishq.app.models.Kalam
import pk.sufiishq.app.models.KalamInfo
import pk.sufiishq.app.utils.IS_SHUFFLE_ON
import pk.sufiishq.app.utils.KALAM_DIR
import pk.sufiishq.app.utils.asFlow
import pk.sufiishq.app.utils.canPlay
import pk.sufiishq.app.utils.getFromStorage
import pk.sufiishq.app.utils.isOfflineFileExists
import pk.sufiishq.app.utils.moveTo
import pk.sufiishq.app.utils.optValue
import pk.sufiishq.app.utils.putInStorage
import pk.sufiishq.app.utils.toast
import timber.log.Timber

@HiltViewModel
class PlayerViewModel @Inject constructor(
    app: Application,
    @AndroidMediaPlayer private val player: AudioPlayer,
    private val fileDownloader: FileDownloader,
    private val kalamRepository: KalamRepository
) : BaseViewModel(app), PlayerDataProvider, PlayerStateListener {

    private var seekbarEnableOnPlaying = true
    private val kalamInfo = MutableLiveData<KalamInfo?>(null)
    private val shuffleState = MutableLiveData(IS_SHUFFLE_ON.getFromStorage(false))
    private val kalamDownloadState = MutableLiveData<KalamDownloadState>(KalamDownloadState.Idle)

    private var fileDownloaderDisposable = Disposables.disposed()
    private var fileMoveDisposables = Disposables.disposed()

    private val appContext = app as SufiIshqApp

    init {
        player.registerListener(this)
        EventDispatcher.getInstance().registerEventHandler(this)
        RxJavaPlugins.setErrorHandler { e ->
            if (e is UndeliverableException) {
                Timber.e(e)
            }
        }
    }

    override fun getShuffleState(): LiveData<Boolean> {
        return shuffleState
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

    private fun playNext() {
        viewModelScope.launch {
            kalamRepository.getNextKalam(
                player.getActiveTrack().id,
                player.getTrackListType(),
                appContext.appConfig.isShuffle()
            ).asFlow().collectLatest { nextKalam ->
                nextKalam?.let {
                    changeTrack(nextKalam, player.getTrackListType())
                }
            }
        }
    }

    private fun playPrevious() {
        viewModelScope.launch {
            kalamRepository.getPreviousKalam(
                player.getActiveTrack().id,
                player.getTrackListType(),
                appContext.appConfig.isShuffle()
            ).asFlow().collectLatest { previousKalam ->
                previousKalam?.let {
                    changeTrack(previousKalam, player.getTrackListType())
                }
            }
        }
    }

    private fun updateSeekbarValue(value: Float) {
        seekbarEnableOnPlaying = false

        kalamInfo.value?.let {
            kalamInfo.value = KalamInfo(
                it.playerState,
                it.kalam,
                value.toInt(),
                it.totalDuration,
                it.enableSeekbar
            )
        }
    }

    private fun onSeekbarChanged(value: Int) {
        seekbarEnableOnPlaying = true
        player.seekTo(value)
    }

    private fun setShuffleState(shuffle: Boolean) {
        IS_SHUFFLE_ON.putInStorage(shuffle)
        shuffleState.postValue(IS_SHUFFLE_ON.getFromStorage(false))
    }

    private fun doPlayOrPause() {
        player.doPlayOrPause()
    }

    private fun changeTrack(kalam: Kalam, trackListType: TrackListType) {
        if (kalam.canPlay(appContext)) {
            player.setSource(kalam, trackListType)
            player.doPlayOrPause()
        }
    }

    /*=======================================*/
    // HANDLE PLAYER EVENTS
    /*=======================================*/

    override fun onEvent(event: Event) {

        when (event) {
            is PlayerEvents.PlayPauseEvent -> doPlayOrPause()
            is PlayerEvents.PlayNext -> playNext()
            is PlayerEvents.PlayPrevious -> playPrevious()
            is PlayerEvents.ChangeShuffle -> setShuffleState(!getShuffleState().optValue(false))
            is PlayerEvents.SeekbarChanged -> onSeekbarChanged(event.value)
            is PlayerEvents.UpdateSeekbar -> updateSeekbarValue(event.value)
            is PlayerEvents.ChangeTrack -> changeTrack(event.kalam, event.trackListType)
            is PlayerEvents.StartDownload -> startDownload(event.kalam)
            is PlayerEvents.DisposeDownload -> disposeDownload()
            is PlayerEvents.ChangeDownloadState -> setKalamDownloadState(event.downloadState)
            else -> throw UnhandledEventException(event, this)
        }
    }

    /*=======================================*/
    // KALAM DOWNLOAD
    /*=======================================*/

    override fun getKalamDownloadState(): LiveData<KalamDownloadState> {
        return kalamDownloadState
    }

    private fun setKalamDownloadState(kalamDownloadState: KalamDownloadState) {
        this.kalamDownloadState.value = kalamDownloadState
    }

    private fun startDownload(kalam: Kalam) {

        setKalamDownloadState(KalamDownloadState.Started(kalam))

        val fileName = FilenameUtils.getName(kalam.onlineSource)
        fileDownloaderDisposable = fileDownloader.download(kalam.onlineSource, cacheDir(fileName))
            .throttleFirst(2, TimeUnit.SECONDS)
            .toFlowable(BackpressureStrategy.LATEST)
            .subscribeOn(Schedulers.io())
            .delay(2, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(

                // onNext ->
                {
                    Timber.d("kalam: ${kalam.title}, download progress: $it")
                    setKalamDownloadState(KalamDownloadState.InProgress(it, kalam))
                },

                // onError ->
                {
                    Timber.e(it)

                    val error = if (it is SocketException || it is UnknownHostException) {
                        "Internet connection failed"
                    } else {
                        it.localizedMessage ?: it.message ?: "Unknown error"
                    }

                    setKalamDownloadState(KalamDownloadState.Error(error, kalam))
                },

                // onComplete ->
                {
                    Timber.d("download completed")

                    val source = cacheDir(fileName)
                    val destination = kalamDir(fileName)

                    Timber.d("file moving from ${source.absolutePath} to ${destination.absolutePath}")

                    fileMoveDisposables = source.moveTo(destination)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {

                            kalam.offlineSource = KALAM_DIR + File.separator + fileName

                            if (kalam.isOfflineFileExists()) {
                                viewModelScope.launch {
                                    kalamRepository.update(kalam)
                                }
                            } else {
                                kalam.offlineSource = ""
                            }

                            setKalamDownloadState(KalamDownloadState.Completed(kalam))
                        }
                }
            )
    }

    private fun disposeDownload() {
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
    // HANDLE PLAYER STATES
    /*=======================================*/

    private fun setIdleState(mediaState: MediaState): KalamInfo {

        if (mediaState is MediaState.Error) {
            appContext.toast(mediaState.message)
        }

        return KalamInfo(
            PlayerState.IDLE,
            mediaState.kalam,
            0,
            0,
            false
        )
    }

    private fun setLoadingState(mediaState: MediaState.Loading): KalamInfo? {
        return kalamInfo.value?.let {
            KalamInfo(
                PlayerState.LOADING,
                mediaState.kalam,
                it.currentProgress,
                it.totalDuration,
                false
            )
        }
    }

    private fun setPlayingState(mediaState: MediaState.Playing): KalamInfo? {
        return if (seekbarEnableOnPlaying) {
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

    private fun setPauseState(mediaState: MediaState.Pause): KalamInfo {
        return KalamInfo(
            PlayerState.PAUSE,
            mediaState.kalam,
            mediaState.currentProgress,
            mediaState.totalDuration,
            false
        )
    }

    private fun setResumeState(mediaState: MediaState.Resume): KalamInfo {
        return KalamInfo(
            PlayerState.PLAYING,
            mediaState.kalam,
            mediaState.currentProgress,
            mediaState.totalDuration,
            true
        )
    }

    private fun updateKalamInfo(updatedKalamInfo: KalamInfo?) {
        kalamInfo.postValue(updatedKalamInfo)
    }

    override fun onStateChange(mediaState: MediaState) {

        val newKalamInfo = when (mediaState) {

            is MediaState.Loading -> setLoadingState(mediaState)
            is MediaState.Playing -> setPlayingState(mediaState)
            is MediaState.Pause -> setPauseState(mediaState)
            is MediaState.Resume -> setResumeState(mediaState)
            is MediaState.Idle, is MediaState.Complete, is MediaState.Error -> setIdleState(
                mediaState
            )
            else -> kalamInfo.value
        }

        updateKalamInfo(newKalamInfo)
    }
}