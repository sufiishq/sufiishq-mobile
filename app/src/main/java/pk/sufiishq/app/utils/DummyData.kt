package pk.sufiishq.app.utils

import androidx.compose.ui.text.buildAnnotatedString
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import pk.sufiishq.app.annotations.ExcludeFromJacocoGeneratedReport
import pk.sufiishq.app.core.downloader.KalamDownloadState
import pk.sufiishq.app.core.help.HelpData
import pk.sufiishq.app.core.splitter.KalamSplitManager
import pk.sufiishq.app.data.providers.GlobalDataProvider
import pk.sufiishq.app.data.providers.HelpDataProvider
import pk.sufiishq.app.data.providers.HomeDataProvider
import pk.sufiishq.app.data.providers.KalamDataProvider
import pk.sufiishq.app.data.providers.PlayerDataProvider
import pk.sufiishq.app.data.providers.PlaylistDataProvider
import pk.sufiishq.app.models.HelpContent
import pk.sufiishq.app.models.Kalam
import pk.sufiishq.app.models.KalamDeleteItem
import pk.sufiishq.app.models.KalamInfo
import pk.sufiishq.app.models.Playlist
import pk.sufiishq.aurora.models.DataMenuItem

// ----------------------------------------- //
// PLAYER DATA PROVIDER
// ----------------------------------------- //

@ExcludeFromJacocoGeneratedReport
fun dummyPlayerDataProvider() = object : PlayerDataProvider {

    override fun getKalamDownloadState(): LiveData<KalamDownloadState> {
        return MutableLiveData(KalamDownloadState.Idle)
    }

    override fun getShuffleState(): LiveData<Boolean> {
        return MutableLiveData(false)
    }

    override fun getPopupMenuItems(kalam: Kalam): LiveData<List<DataMenuItem>> {
        return MutableLiveData()
    }

    override fun getKalamInfo(): LiveData<KalamInfo?> {
        return MutableLiveData(null)
    }

    override fun getShowPlaylistDialog(): LiveData<Kalam?> {
        return MutableLiveData(null)
    }

    override fun getAllPlaylist(): LiveData<List<Playlist>> {
        return MutableLiveData(listOf())
    }
}

// ----------------------------------------- //
// KALAM DATA PROVIDER
// ----------------------------------------- //

@ExcludeFromJacocoGeneratedReport
fun dummyKalamDataProvider() = object : KalamDataProvider {

    override fun getKalamRenameDialog(): LiveData<Kalam?> {
        return MutableLiveData(null)
    }

    override fun getKalamDataFlow(): Flow<PagingData<Kalam>> {
        return emptyFlow()
    }

    override fun getKalamDeleteConfirmDialog(): LiveData<KalamDeleteItem?> {
        return MutableLiveData(null)
    }

    override fun getKalamSplitManagerDialog(): LiveData<KalamSplitManager?> {
        return MutableLiveData(null)
    }

    override fun getShowCircularProgressDialog() = MutableLiveData(false)

    override fun popupMenuItems(kalam: Kalam, trackType: String): List<DataMenuItem> {
        return listOf()
    }
}

@ExcludeFromJacocoGeneratedReport
fun dummyKalam() = Kalam(1, "Kalam Title", 2, "1993", "Karachi", "", "", 0, 0)

// ----------------------------------------- //
// PLAYLIST DATA PROVIDER
// ----------------------------------------- //

@ExcludeFromJacocoGeneratedReport
fun dummyPlaylistDataProvider() = object : PlaylistDataProvider {

    override fun getPopupMenuItems(): List<DataMenuItem> {
        return listOf()
    }

    override fun getShowPlaylistAddUpdateDialog(): LiveData<Playlist?> {
        return MutableLiveData(null)
    }

    override fun getShowConfirmPlaylistDeleteDialog(): LiveData<Playlist?> {
        return MutableLiveData(null)
    }

    override fun getAll(): LiveData<List<Playlist>> = MutableLiveData(
        listOf(
            Playlist(1, "Karachi"), Playlist(2, "Lahore")
        )
    )

    override fun get(id: Int) = MutableLiveData(Playlist(1, "Karachi"))
}

@ExcludeFromJacocoGeneratedReport
fun dummyPlaylist() = Playlist(1, "Karachi")

// ----------------------------------------- //
// HOME DATA PROVIDER
// ----------------------------------------- //

@ExcludeFromJacocoGeneratedReport
fun dummyHomeDataProvider() = object : HomeDataProvider {

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

// ----------------------------------------- //
// GLOBAL DATA PROVIDER
// ----------------------------------------- //

@ExcludeFromJacocoGeneratedReport
fun dummyGlobalDataProvider() = object : GlobalDataProvider {

    override fun getShowUpdateButton() = MutableLiveData(true)

    override fun popupMenuItems(): List<DataMenuItem> {
        return listOf()
    }
}

// ----------------------------------------- //
// HELP DATA PROVIDER
// ----------------------------------------- //

@ExcludeFromJacocoGeneratedReport
fun dummyHelpDataProvider() = object : HelpDataProvider {

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