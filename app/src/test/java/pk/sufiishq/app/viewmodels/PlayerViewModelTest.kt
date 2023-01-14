package pk.sufiishq.app.viewmodels

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.test.core.app.ApplicationProvider
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.spyk
import io.mockk.verify
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.net.SocketException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.robolectric.util.ReflectionHelpers
import org.robolectric.util.ReflectionHelpers.callInstanceMethod
import org.robolectric.util.ReflectionHelpers.getField
import org.robolectric.util.ReflectionHelpers.setField
import pk.sufiishq.app.R
import pk.sufiishq.app.SufiIshqTest
import pk.sufiishq.app.configs.AppConfig
import pk.sufiishq.app.core.downloader.FileDownloader
import pk.sufiishq.app.core.downloader.KalamDownloadState
import pk.sufiishq.app.core.event.events.GlobalEvents
import pk.sufiishq.app.core.event.events.PlayerEvents
import pk.sufiishq.app.core.event.exception.UnhandledEventException
import pk.sufiishq.app.core.player.SufiishqMediaPlayer
import pk.sufiishq.app.core.player.state.MediaState
import pk.sufiishq.app.core.storage.SecureSharedPreferencesStorage
import pk.sufiishq.app.data.repository.KalamRepository
import pk.sufiishq.app.helpers.PlayerState
import pk.sufiishq.app.helpers.TrackListType
import pk.sufiishq.app.models.FileInfo
import pk.sufiishq.app.models.Kalam
import pk.sufiishq.app.models.KalamInfo
import pk.sufiishq.app.utils.IS_SHUFFLE_ON
import pk.sufiishq.app.utils.canPlay
import pk.sufiishq.app.utils.isOfflineFileExists
import pk.sufiishq.app.utils.toast

class PlayerViewModelTest : SufiIshqTest() {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val realApp = ApplicationProvider.getApplicationContext<Context>()
    private val app = mockApp()
    private val player = mockk<SufiishqMediaPlayer>()
    private val kalamRepository = mockk<KalamRepository>()
    private val fileDownloader = mockk<FileDownloader>()
    private val storage = mockk<SecureSharedPreferencesStorage>()
    private val appConfig = mockk<AppConfig>()

    private lateinit var playerViewModel: PlayerViewModel

    @Before
    fun setUp() {
        every { app.keyValueStorage } returns storage
        every { app.appConfig } returns appConfig
        every { storage.get(IS_SHUFFLE_ON, false) } returns false
        every { player.registerListener(any()) } returns true
        playerViewModel = PlayerViewModel(app, player, fileDownloader, kalamRepository)

        setIdleKalamInfo()
    }

    @Test
    fun test_getShuffleState_shouldReturn_false() {
        assertFalse(playerViewModel.getShuffleState().value!!)
    }

    @Test
    fun test_getMenuItems_shouldReturn_expectedMenuItems() {
        every { app.getString(any()) } answers {
            realApp.getString(firstArg())
        }

        listOf(
            R.string.mark_as_favorite,
            R.string.remove_favorite,
            R.string.download_label,
            R.string.add_to_playlist
        )
            .map { realApp.getString(it) }
            .sorted()
            .zip(playerViewModel.getMenuItems().sorted())
            .onEach {
                assertEquals(it.first, it.second)
            }
    }

    @Test
    fun test_getKalamDownloadState_shouldReturn_idleStateByDefault() {
        assertTrue(playerViewModel.getKalamDownloadState().value!! is KalamDownloadState.Idle)
    }

    @Test
    fun test_onStateChange_shouldUpdateToLoading_kalamInfo_withLoadingState() {
        playerViewModel.onStateChange(MediaState.Loading(mockk()))
        assertKalamInfoState(PlayerState.LOADING)
    }

    @Test
    fun test_onStateChange_shouldUpdateToPlaying_kalamInfo_withPlayingState_and_withSeekbarEnableOnPlaying() {
        updateSeekbarEnableOnPlaying(true)
        playerViewModel.onStateChange(MediaState.Playing(mockk(), 0, 0))
        assertKalamInfoState(PlayerState.PLAYING)
    }

    @Test
    fun test_onStateChange_shouldUpdateToPlaying_kalamInfo_withPlayingState_and_withoutSeekbarEnableOnPlaying() {
        updateSeekbarEnableOnPlaying(false)
        setIdleKalamInfo()

        playerViewModel.onStateChange(MediaState.Playing(mockk(), 0, 0))
        assertKalamInfoState(PlayerState.IDLE)
    }

    @Test
    fun test_onStateChange_shouldUpdateToPause_kalamInfo_withPauseState() {
        playerViewModel.onStateChange(MediaState.Pause(mockk(), 0, 0))
        assertKalamInfoState(PlayerState.PAUSE)
    }

    @Test
    fun test_onStateChange_shouldUpdateToPlaying_kalamInfo_withResumeState() {
        playerViewModel.onStateChange(MediaState.Resume(mockk(), 0, 0))
        assertKalamInfoState(PlayerState.PLAYING)
    }

    @Test
    fun test_onStateChange_shouldUpdateToIdle_kalamInfo_withOtherStates() {
        mockkStatic(Context::toast)
        every { app.toast(any()) } returns Unit

        listOf(
            MediaState.Idle(mockk()),
            MediaState.Complete(mockk()),
            MediaState.Error(mockk(), 0, 0, "something went wrong")
        ).onEach {
            playerViewModel.onStateChange(it)
            assertKalamInfoState(PlayerState.IDLE)

            if (it is MediaState.Error) {
                verify { app.toast(it.message) }
            }
        }
    }

    @Test
    fun test_onStateChange_shouldNotUpdate_kalamInfo_whenStateIsUnknown() {

        val prevKalamInfo = playerViewModel.getKalamInfo().value
        playerViewModel.onStateChange(MediaState.Prepared(getKalam()))

        assertKalamInfoState(prevKalamInfo!!.playerState)
    }

    @Test
    fun test_onEven_shouldCall_doPlayOrPause_method_onPlayer() {
        every { player.doPlayOrPause() } returns Unit
        playerViewModel.onEvent(PlayerEvents.PlayPauseEvent)
        verify { player.doPlayOrPause() }
    }

    @Test
    fun test_onEvent_shouldCall_getNextKalam_method_onKalamRepo() {
        mockkStatic(Kalam::canPlay)

        val nextKalam = getKalam().copy(
            id = 10,
        )
        val trackListType = TrackListType.All()
        val isShuffle = false

        every { nextKalam.canPlay(any()) } returns true
        every { player.getActiveTrack() } returns getKalam()
        every { player.getTrackListType() } returns trackListType
        every { appConfig.isShuffle() } returns isShuffle
        every { player.setSource(any(), any()) } returns Unit
        every { player.doPlayOrPause() } returns Unit
        every { kalamRepository.getNextKalam(any(), any(), any()) } returns MutableLiveData(
            nextKalam
        )

        playerViewModel.onEvent(PlayerEvents.PlayNext)

        verify { kalamRepository.getNextKalam(getKalam().id, trackListType, isShuffle) }
        verify { player.setSource(nextKalam, trackListType) }
        verify { player.doPlayOrPause() }
    }

    @Test
    fun test_onEvent_shouldCall_getPrevKalam_method_onKalamRepo() {
        mockkStatic(Kalam::canPlay)

        val prevKalam = getKalam().copy(
            id = 10,
        )
        val trackListType = TrackListType.All()
        val isShuffle = false

        every { prevKalam.canPlay(any()) } returns true
        every { player.getActiveTrack() } returns getKalam()
        every { player.getTrackListType() } returns trackListType
        every { appConfig.isShuffle() } returns isShuffle
        every { player.setSource(any(), any()) } returns Unit
        every { player.doPlayOrPause() } returns Unit
        every { kalamRepository.getPreviousKalam(any(), any(), any()) } returns MutableLiveData(
            prevKalam
        )

        playerViewModel.onEvent(PlayerEvents.PlayPrevious)

        verify { kalamRepository.getPreviousKalam(getKalam().id, trackListType, isShuffle) }
        verify { player.setSource(prevKalam, trackListType) }
        verify { player.doPlayOrPause() }
    }

    @Test
    fun test_onEvent_shouldUpdate_shuffleStateTrue() {

        every { storage.put(any(), any<Boolean>()) } returns Unit
        every { storage.get(any(), false) } returns true

        playerViewModel.onEvent(PlayerEvents.ChangeShuffle)

        val shuffleState = playerViewModel.getShuffleState().value
        verify { storage.put(IS_SHUFFLE_ON, shuffleState) }
    }

    @Test
    fun test_onEvent_shouldUpdate_seekbarEnableOnPlay_toTrue_and_playerSeek() {
        updateSeekbarEnableOnPlaying(false)
        every { player.seekTo(any()) } returns Unit

        playerViewModel.onEvent(PlayerEvents.SeekbarChanged(100))

        assertTrue(getSeekbarEnableOnPlaying())
        verify { player.seekTo(100) }
    }

    @Test
    fun test_onEvent_shouldUpdate_seekbarEnableOnPlay_toFalse_and_updateKalamInfo() {
        updateSeekbarEnableOnPlaying(true)
        setIdleKalamInfo()

        playerViewModel.onEvent(PlayerEvents.UpdateSeekbar(50f))
        assertFalse(getSeekbarEnableOnPlaying())
        assertEquals(50, playerViewModel.getKalamInfo().value!!.currentProgress)
    }

    @Test
    fun test_onEvent_shouldUpdate_playerSource_andCall_doPlayOrPause() {
        mockkStatic(Kalam::canPlay)
        val kalam = mockk<Kalam>()
        val trackListType = TrackListType.All()

        every { kalam.canPlay(any()) } returns true
        every { player.setSource(any(), any()) } returns Unit
        every { player.doPlayOrPause() } returns Unit

        playerViewModel.onEvent(PlayerEvents.ChangeTrack(kalam, trackListType))

        verify { player.setSource(kalam, trackListType) }
        verify { player.doPlayOrPause() }
    }

    @Test
    fun test_onEvent_shouldVerify_allScenarios_ofKalamDownloading() {

        mockkStatic(Completable::fromAction)
        mockkStatic(Kalam::isOfflineFileExists)

        val onNextSlot = slot<Consumer<FileInfo>>()
        val onErrorSlot = slot<Consumer<in Throwable>>()
        val onCompleteSlot = slot<Action>()
        val completableOnCompleteSlot = slot<Action>()

        val flowable = mockk<Flowable<FileInfo>> {
            every { subscribeOn(any()) } returns this
            every { delay(any(), any()) } returns this
            every { observeOn(any()) } returns this
            every {
                subscribe(
                    capture(onNextSlot),
                    capture(onErrorSlot),
                    capture(onCompleteSlot)
                )
            } returns mockk()
        }

        val observable = mockk<Observable<FileInfo>> {
            every { throttleFirst(any(), any()) } returns this
            every { toFlowable(any()) } returns flowable
        }

        val completable = mockk<Completable> {
            every { subscribeOn(any()) } returns this
            every { observeOn(any()) } returns this
            every { subscribe(capture(completableOnCompleteSlot)) } returns mockk()
        }

        every { Completable.fromAction(any()) } returns completable

        val kalam = spyk(getKalam())
        val cacheDir = File("cache_dir")
        val filesDir = File("kalam_dir")

        every { app.cacheDir } returns cacheDir
        every { app.filesDir } returns filesDir
        every { fileDownloader.download(any(), any()) } returns observable

        playerViewModel.onEvent(PlayerEvents.StartDownload(kalam))

        assertTrue(playerViewModel.getKalamDownloadState().value is KalamDownloadState.Started)
        verify { fileDownloader.download(any(), any()) }

        // verify download progress case by trigger onNext
        onNextSlot.captured.accept(FileInfo(File("downloaded_file"), 100.0, 50))

        with(playerViewModel.getKalamDownloadState().value as KalamDownloadState.InProgress) {
            assertEquals(
                File("downloaded_file").absolutePath,
                fileInfo.downloadingFile.absolutePath
            )
            assertEquals(100, fileInfo.totalSize.toInt())
            assertEquals(50, fileInfo.progress)
        }

        verify { observable.throttleFirst(2, TimeUnit.SECONDS) }
        verify { observable.toFlowable(BackpressureStrategy.LATEST) }
        verify { flowable.subscribeOn(Schedulers.io()) }
        verify { flowable.delay(2, TimeUnit.SECONDS) }
        verify { flowable.observeOn(AndroidSchedulers.mainThread()) }

        // verify download error case by trigger onError with SocketException
        onErrorSlot.captured.accept(SocketException())

        with(playerViewModel.getKalamDownloadState().value as KalamDownloadState.Error) {
            assertEquals("Internet connection failed", error)
        }

        // verify download error case by trigger onError with UnknownHostException
        onErrorSlot.captured.accept(UnknownHostException())

        with(playerViewModel.getKalamDownloadState().value as KalamDownloadState.Error) {
            assertEquals("Internet connection failed", error)
        }

        // verify download error case by trigger onError with RuntimeException and message
        onErrorSlot.captured.accept(RuntimeException("something went wrong"))

        with(playerViewModel.getKalamDownloadState().value as KalamDownloadState.Error) {
            assertEquals("something went wrong", error)
        }

        // verify download error case by trigger onError with RuntimeException without message
        onErrorSlot.captured.accept(RuntimeException())

        with(playerViewModel.getKalamDownloadState().value as KalamDownloadState.Error) {
            assertEquals("Unknown error", error)
        }

        // verify complete case by trigger onComplete without offline-file-exists
        every { kalam.isOfflineFileExists() } returns false
        onCompleteSlot.captured.run()

        verify { completable.subscribeOn(Schedulers.io()) }
        verify { completable.observeOn(AndroidSchedulers.mainThread()) }

        completableOnCompleteSlot.captured.run()

        assertTrue(playerViewModel.getKalamDownloadState().value is KalamDownloadState.Completed)

        // verify complete case by trigger onComplete with offline-file-exists
        every { kalam.isOfflineFileExists() } returns true
        coEvery { kalamRepository.update(any()) } returns Unit

        onCompleteSlot.captured.run()
        completableOnCompleteSlot.captured.run()

        assertTrue(playerViewModel.getKalamDownloadState().value is KalamDownloadState.Completed)
    }

    @Test
    fun test_onEvent_shouldDispose_allDisposable() {
        val fileDownloaderDisposable = mockk<Disposable>()
        val fileMoveDisposables = mockk<Disposable>()

        every { fileDownloaderDisposable.dispose() } returns Unit
        every { fileMoveDisposables.dispose() } returns Unit

        setField(playerViewModel, "fileDownloaderDisposable", fileDownloaderDisposable)
        setField(playerViewModel, "fileMoveDisposables", fileMoveDisposables)

        playerViewModel.onEvent(PlayerEvents.DisposeDownload)

        verify { fileDownloaderDisposable.dispose() }
        verify { fileMoveDisposables.dispose() }
    }

    @Test
    fun test_onEvent_shouldUpdate_downloadState_toIdle() {
        playerViewModel.onEvent(PlayerEvents.ChangeDownloadState(KalamDownloadState.Idle))
        assertTrue(playerViewModel.getKalamDownloadState().value is KalamDownloadState.Idle)
    }

    @Test(expected = UnhandledEventException::class)
    fun test_onEvent_shouldThrow_UnhandledEventException_whenUnknownEventReceived() {
        playerViewModel.onEvent(GlobalEvents.StartUpdateFlow)
    }

    private fun setIdleKalamInfo() {
        val kalamInfo = KalamInfo(PlayerState.IDLE, getKalam(), 0, 0, false)
        callInstanceMethod<Unit>(
            playerViewModel, "updateKalamInfo",
            ReflectionHelpers.ClassParameter(KalamInfo::class.java, kalamInfo)
        )
    }

    private fun assertKalamInfoState(playerState: PlayerState) {
        assertEquals(playerState.name, playerViewModel.getKalamInfo().value!!.playerState.name)
    }

    private fun updateSeekbarEnableOnPlaying(isEnable: Boolean) {
        setField(playerViewModel, "seekbarEnableOnPlaying", isEnable)
    }

    private fun getSeekbarEnableOnPlaying(): Boolean {
        return getField(playerViewModel, "seekbarEnableOnPlaying")
    }
}