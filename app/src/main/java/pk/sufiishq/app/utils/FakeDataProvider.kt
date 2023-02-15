package pk.sufiishq.app.utils

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.ui.text.buildAnnotatedString
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import pk.sufiishq.app.annotations.ExcludeFromJacocoGeneratedReport
import pk.sufiishq.app.core.help.HelpData
import pk.sufiishq.app.core.kalam.downloader.KalamDownloadState
import pk.sufiishq.app.core.kalam.splitter.SplitKalamInfo
import pk.sufiishq.app.core.kalam.splitter.SplitStatus
import pk.sufiishq.app.data.providers.DashboardDataProvider
import pk.sufiishq.app.data.providers.HelpDataProvider
import pk.sufiishq.app.data.providers.KalamDataProvider
import pk.sufiishq.app.data.providers.MainDataProvider
import pk.sufiishq.app.data.providers.PlayerDataProvider
import pk.sufiishq.app.data.providers.PlaylistDataProvider
import pk.sufiishq.app.helpers.TrackListType
import pk.sufiishq.app.models.HelpContent
import pk.sufiishq.app.models.Highlight
import pk.sufiishq.app.models.HijriDate
import pk.sufiishq.app.models.Kalam
import pk.sufiishq.app.models.KalamDeleteItem
import pk.sufiishq.app.models.KalamInfo
import pk.sufiishq.app.models.NavigationItem
import pk.sufiishq.app.models.Playlist
import pk.sufiishq.aurora.models.DataMenuItem

// ---------------------------------------------------------------------------------- //
// DASHBOARD DATA PROVIDER
// ---------------------------------------------------------------------------------- //

@ExcludeFromJacocoGeneratedReport
fun fakeDashboardDataProvider() = object : DashboardDataProvider {
    override fun getMainNavigationItems(): List<NavigationItem> = listOf()
    override fun countAll(): LiveData<Int> = MutableLiveData(150)
    override fun countFavorites(): LiveData<Int> = MutableLiveData(15)
    override fun countDownloads(): LiveData<Int> = MutableLiveData(35)
    override fun countPlaylist(): LiveData<Int> = MutableLiveData(5)
    override fun getHighlightAvailable(): LiveData<Highlight?> = MutableLiveData(null)
}

// ---------------------------------------------------------------------------------- //
// PLAYER DATA PROVIDER
// ---------------------------------------------------------------------------------- //

@ExcludeFromJacocoGeneratedReport
fun fakePlayerDataProvider() = object : PlayerDataProvider {

    override fun getKalamInfo(): LiveData<KalamInfo?> = MutableLiveData(null)
    override fun updateSeekbarValue(value: Float) = Unit
    override fun onSeekbarChanged(value: Int) = Unit
    override fun doPlayOrPause() = Unit
}

// ---------------------------------------------------------------------------------- //
// KALAM DATA PROVIDER
// ---------------------------------------------------------------------------------- //

@ExcludeFromJacocoGeneratedReport
fun fakeKalamDataProvider() = object : KalamDataProvider {

    override fun getKalamDataFlow(): Flow<PagingData<Kalam>> = flow {
        PagingData.from(
            listOf(
                fakeKalam()
            )
        )
    }

    override fun searchKalam(keyword: String, trackListType: TrackListType) = Unit
    override fun popupMenuItems(kalam: Kalam, trackType: String): List<DataMenuItem> = listOf()

    override fun changeTrack(kalam: Kalam, trackListType: TrackListType) = Unit

    override fun shareKalam(kalam: Kalam, componentActivity: ComponentActivity) = Unit

    override fun markAsFavorite(kalam: Kalam) = Unit
    override fun removeFavorite(kalam: Kalam) = Unit

    override fun delete(kalamDeleteItem: KalamDeleteItem) = Unit
    override fun showKalamConfirmDeleteDialog(): LiveData<KalamDeleteItem?> = MutableLiveData(null)
    override fun showKalamConfirmDeleteDialog(kalamDeleteItem: KalamDeleteItem?) = Unit

    override fun addToPlaylist(kalam: Kalam, playlist: Playlist) = Unit
    override fun showPlaylistDialog(): LiveData<Pair<Kalam, List<Playlist>>?> =
        MutableLiveData(null)

    override fun showPlaylistDialog(kalam: Kalam) = Unit
    override fun dismissPlaylistDialog() = Unit

    override fun getKalamDownloadState(): LiveData<KalamDownloadState> =
        MutableLiveData(KalamDownloadState.Idle)

    override fun startDownload(kalam: Kalam) = Unit
    override fun dismissDownload() = Unit

    override fun showKalamSplitDialog(kalam: Kalam) = Unit
    override fun showKalamSplitDialog(): LiveData<SplitKalamInfo?> = MutableLiveData(null)
    override fun startSplitting() = Unit
    override fun playSplitKalamPreview() = Unit
    override fun setSplitStart(start: Int) = Unit
    override fun setSplitEnd(end: Int) = Unit
    override fun setSplitStatus(status: SplitStatus) = Unit
    override fun updateSplitSeekbarValue(value: Float) = Unit
    override fun saveSplitKalam(sourceKalam: Kalam, kalamTitle: String) = Unit
    override fun dismissKalamSplitDialog() = Unit
}

@ExcludeFromJacocoGeneratedReport
fun fakeKalam() = Kalam(1, "Kalam Title", 2, "1993", "Karachi", "", "", 0, 0)

// ---------------------------------------------------------------------------------- //
// PLAYLIST DATA PROVIDER
// ---------------------------------------------------------------------------------- //

@ExcludeFromJacocoGeneratedReport
fun fakePlaylistDataProvider() = object : PlaylistDataProvider {

    override fun getPopupMenuItems(): List<DataMenuItem> = listOf()
    override fun showAddUpdatePlaylistDialog(): LiveData<Playlist?> = MutableLiveData(null)
    override fun showConfirmDeletePlaylistDialog(): LiveData<Playlist?> = MutableLiveData(null)
    override fun getAll(): LiveData<List<Playlist>> = MutableLiveData(
        listOf(
            Playlist(1, "Karachi"), Playlist(2, "Lahore")
        )
    )

    override fun get(id: Int) = MutableLiveData(Playlist(1, "Karachi"))
    override fun add(playlist: Playlist) = Unit
    override fun showAddUpdatePlaylistDialog(playlist: Playlist?) = Unit
    override fun showConfirmDeletePlaylistDialog(playlist: Playlist?) = Unit
    override fun update(playlist: Playlist) = Unit
    override fun delete(playlist: Playlist) = Unit
}

@ExcludeFromJacocoGeneratedReport
fun fakePlaylist() = Playlist(1, "Karachi")

// ---------------------------------------------------------------------------------- //
// GLOBAL DATA PROVIDER
// ---------------------------------------------------------------------------------- //

@ExcludeFromJacocoGeneratedReport
fun fakeMainDataProvider() = object : MainDataProvider {

    override fun popupMenuItems(): List<DataMenuItem> = listOf()
    override fun showUpdateButton(): LiveData<Boolean> = MutableLiveData(false)
    override fun handleUpdate() = Unit
    override fun openFacebookGroup(context: Context, groupUrl: String) = Unit
    override fun shareApp(activity: ComponentActivity) = Unit
    override fun checkUpdate(activity: ComponentActivity) = Unit
    override fun showUpdateButton(value: Boolean) = Unit
    override fun unregisterListener(activity: ComponentActivity) = Unit
    override fun getHijriDate(): LiveData<HijriDate?> = MutableLiveData(null)
}

// ---------------------------------------------------------------------------------- //
// HELP DATA PROVIDER
// ---------------------------------------------------------------------------------- //

@ExcludeFromJacocoGeneratedReport
fun fakeHelpDataProvider() = object : HelpDataProvider {

    override fun getHelpContent(): Flow<List<HelpContent>> = MutableStateFlow(
        listOf(
            HelpContent(
                "First Title", listOf(
                    HelpData.Paragraph(buildAnnotatedString { append("some paragraph") }),
                    HelpData.Photo("Path"),
                    HelpData.Paragraph(buildAnnotatedString { append("another paragraph") }),
                    HelpData.Divider(2),
                    HelpData.Paragraph(buildAnnotatedString { append("another paragraph") }),
                    HelpData.Spacer(10),
                    HelpData.Paragraph(buildAnnotatedString { append("another paragraph with some **bold** and __italic__ words") })
                )
            ), HelpContent(
                "Second Title",
                listOf(HelpData.Paragraph(buildAnnotatedString { append("Test 2") }))
            )
        )
    )
}