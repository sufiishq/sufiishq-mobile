package pk.sufiishq.app.utils

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import pk.sufiishq.app.SufiIshqApp
import pk.sufiishq.app.annotations.ExcludeFromJacocoGeneratedReport
import pk.sufiishq.app.core.player.SufiishqMediaPlayer
import pk.sufiishq.app.core.player.helpers.AppMediaPlayer
import pk.sufiishq.app.data.providers.HomeDataProvider
import pk.sufiishq.app.data.providers.KalamDataProvider
import pk.sufiishq.app.data.providers.PlayerDataProvider
import pk.sufiishq.app.data.providers.PlaylistDataProvider
import pk.sufiishq.app.helpers.KalamSplitManager
import pk.sufiishq.app.helpers.PreviewAudioPlayer
import pk.sufiishq.app.helpers.TrackListType
import pk.sufiishq.app.models.Kalam
import pk.sufiishq.app.models.KalamInfo
import pk.sufiishq.app.models.Playlist
import java.io.File

// ----------------------------------------- //
// PLAYER DATA PROVIDER
// ----------------------------------------- //

@ExcludeFromJacocoGeneratedReport
fun dummyPlayerDataProvider() = object : PlayerDataProvider {

    override fun updateSeekbarValue(value: Float) { /* no comment */
    }

    override fun doPlayOrPause() { /* no comment */
    }

    override fun changeTrack(kalam: Kalam, trackListType: TrackListType) {
        /* no comment */
    }

    override fun getDownloadProgress(): LiveData<Float> {
        return MutableLiveData(0f)
    }

    override fun getDownloadError(): LiveData<String> {
        return MutableLiveData("")
    }

    override fun setDownloadError(error: String) { /* no comment */
    }

    override fun startDownload(kalam: Kalam) { /* no comment */
    }

    override fun disposeDownload() { /* no comment */
    }

    override fun playNext() {
        /* no comment */
    }

    override fun playPrevious() {
        /* no comment */
    }

    override fun getShuffleState(): LiveData<Boolean> {
        return MutableLiveData(false)
    }

    override fun setShuffleState(shuffle: Boolean) {
        /* no comment */
    }

    override fun getMenuItems(): List<String> {
        return listOf()
    }

    override fun getKalamInfo(): LiveData<KalamInfo?> {
        return MutableLiveData(null)
    }

    override fun onSeekbarChanged(value: Int) {
        /* no comment */
    }
}

// ----------------------------------------- //
// KALAM DATA PROVIDER
// ----------------------------------------- //

@ExcludeFromJacocoGeneratedReport
fun dummyKalamDataProvider() = object : KalamDataProvider {

    override fun init(trackListType: TrackListType) { /* no comment */
    }

    override fun getKalamDataFlow(): Flow<PagingData<Kalam>> {
        return emptyFlow()
    }

    override fun searchKalam(keyword: String, trackListType: TrackListType) { /* no comment */
    }

    override fun update(kalam: Kalam) { /* no comment */
    }

    override fun delete(kalam: Kalam, trackType: String) { /* no comment */
    }

    override fun saveSplitKalam(
        sourceKalam: Kalam,
        splitFile: File,
        kalamTitle: String
    ) { /* no comment */
    }

    override fun markAsFavorite(kalam: Kalam) { /* no comment */
    }

    override fun removeFavorite(kalam: Kalam) {
        /* no comment */
    }

    override fun getKalamSplitManager(): KalamSplitManager {
        return KalamSplitManager(
            SufiIshqApp.getInstance(),
            PreviewAudioPlayer(Handler(Looper.getMainLooper()), MediaPlayer()),
            SufiishqMediaPlayer(
                app,
                AppMediaPlayer(Handler(Looper.getMainLooper()))
            )
        )
    }

    override fun getActiveSearchKeyword(): String {
        return ""
    }
}

@ExcludeFromJacocoGeneratedReport
fun dummyTrack() = Kalam(1, "Kalam Title", 2, "1993", "Karachi", "", "", 0, 0)

// ----------------------------------------- //
// PLAYLIST DATA PROVIDER
// ----------------------------------------- //

@ExcludeFromJacocoGeneratedReport
fun dummyPlaylistDataProvider() = object : PlaylistDataProvider {

    override fun getAll(): LiveData<List<Playlist>> = MutableLiveData(
        listOf(
            Playlist(1, "Karachi"),
            Playlist(2, "Lahore")
        )
    )

    override fun get(id: Int) = MutableLiveData(Playlist(1, "Karachi"))

    override fun add(playlist: Playlist) { /* no comment */
    }

    override fun update(playlist: Playlist) { /* no comment */
    }

    override fun delete(playlist: Playlist) { /* no comment */
    }
}

@ExcludeFromJacocoGeneratedReport
fun dummyPlaylist() = Playlist(1, "Karachi")

// ----------------------------------------- //
// HOME DATA PROVIDER
// ----------------------------------------- //

fun dummyHomeDataProvider() = object : HomeDataProvider {
    override fun setShowUpdateDialog(value: Boolean) {
        /* no comment */
    }

    override fun getShowUpdateDialog(): LiveData<Boolean> {
        return MutableLiveData(true)
    }

    override fun handleUpdate() {
        /* no comment */
    }

    override fun getKalam(id: Int): LiveData<Kalam?> {
        return MutableLiveData(null)
    }

    override fun countAll(): LiveData<Int> {
        return MutableLiveData(150)
    }

    override fun countFavorites(): LiveData<Int> {
        return MutableLiveData(15)
    }

    override fun countDownloads(): LiveData<Int> {
        return MutableLiveData(35)
    }

    override fun countPlaylist(): LiveData<Int> {
        return MutableLiveData(5)
    }
}