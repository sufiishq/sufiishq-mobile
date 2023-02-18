/*
 * Copyright 2022-2023 SufiIshq
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pk.sufiishq.app.viewmodels

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import pk.sufiishq.app.SufiIshqTest
import pk.sufiishq.app.core.kalam.data.repository.KalamRepository
import pk.sufiishq.app.core.kalam.downloader.FileDownloader
import pk.sufiishq.app.core.player.controller.PlayerViewModel
import pk.sufiishq.app.core.player.controller.SufiishqMediaPlayer
import pk.sufiishq.app.core.playlist.data.repository.PlaylistRepository
import pk.sufiishq.app.core.storage.SecureSharedPreferencesStorage

@Ignore("will be fixed later")
class PlayerViewModelTest : SufiIshqTest() {

    @get:Rule val rule = InstantTaskExecutorRule()

    private val realApp = ApplicationProvider.getApplicationContext<Context>()
    private val app = mockApp()
    private val player = mockk<SufiishqMediaPlayer>()
    private val kalamRepository = mockk<KalamRepository>()
    private val playlistRepository = mockk<PlaylistRepository>()
    private val fileDownloader = mockk<FileDownloader>()
    private val storage = mockk<SecureSharedPreferencesStorage>()

    private lateinit var playerViewModel: PlayerViewModel

    @Before
    fun setUp() {
        every { app.keyValueStorage } returns storage

        every { player.registerListener(any()) } returns true
        /*playerViewModel =
            PlayerViewModel(
                app,
                player,
                fileDownloader,
                kalamRepository,
                playlistRepository,
                favoriteChangeFactory,
                mockk(),
                mockk(),
            )*/

        // setIdleKalamInfo()
    }

  /*@Test
  fun test_getShuffleState_shouldReturn_false() {
      assertFalse(playerViewModel.getShuffleState().value!!)
  }

  @Ignore("will be fixed later")
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
      //.zip(playerViewModel.getMenuItems().sorted())
      //.onEach {
      //    assertEquals(it.first, it.second)
      //}
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

  */
  /*@Test(expected = UnhandledEventException::class)
  fun test_onEvent_shouldThrow_UnhandledEventException_whenUnknownEventReceived() {
      playerViewModel.onEvent(GlobalEvents.StartUpdateFlow)
  }*/
  /*

  @Test
  fun testShouldPlaylistDialog_shouldVerify_isShow() {
      playerViewModel.onEvent(PlayerEvents.ShowPlaylistDialog(sampleKalam()))

      Shadows.shadowOf(Looper.getMainLooper()).idle()
      playerViewModel.getShowPlaylistDialog().observe(mockLifecycleOwner()) {
          Assert.assertNotNull(it)
          assertEquals(sampleKalam().id, it?.id)
      }
  }

  @Test
  fun testAddToPlaylist_shouldAdd_playlistInDatabase() {
      mockkStatic(Context::toast)
      every { app.toast(any()) } returns Unit

      launchViewModelScope(playerViewModel) { slot ->
          coEvery { kalamRepository.update(any()) } returns Unit

          val kalam = sampleKalam()
          val playlist = Playlist(2, "Faisalabad")

          playerViewModel.onEvent(PlayerEvents.AddKalamInPlaylist(kalam, playlist))
          slot.invoke()

          assertEquals(kalam.playlistId, playlist.id)
          verify { app.toast("${kalam.title} added in ${playlist.title} Playlist") }
      }
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
  }*/
}
