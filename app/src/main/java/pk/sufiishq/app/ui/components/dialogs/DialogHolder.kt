package pk.sufiishq.app.ui.components.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import pk.sufiishq.app.data.providers.KalamDataProvider
import pk.sufiishq.app.data.providers.PlayerDataProvider
import pk.sufiishq.app.data.providers.PlaylistDataProvider
import pk.sufiishq.app.helpers.GlobalEventHandler

@Composable
fun DialogHolder(
    playerDataProvider: PlayerDataProvider,
    playlistDataProvider: PlaylistDataProvider,
    kalamDataProvider: KalamDataProvider,
    globalEventHandler: GlobalEventHandler
) {

    val kalamDownloadState = playerDataProvider.getKalamDownloadState().observeAsState()

    // kalam download start dialog
    KalamDownloadStartedDialog(
        kalamDownloadState = kalamDownloadState
    )

    // kalam download in-progress dialog
    KalamDownloadInProgressDialog(
        kalamDownloadState = kalamDownloadState
    )

    // kalam download completed dialog
    KalamDownloadCompletedDialog(
        kalamDownloadState = kalamDownloadState
    )

    // kalam download error dialog
    KalamDownloadErrorDialog(
        kalamDownloadState = kalamDownloadState
    )

    // playlist dialog
    PlaylistDialog(
        playlistState = playlistDataProvider.getAll().observeAsState(),
        showPlaylistDialog = playlistDataProvider.getShowPlaylistDialog().observeAsState()
    )

    // kalam confirm delete dialog
    KalamConfirmDeleteDialog(
        kalamDeleteItem = kalamDataProvider.getKalamDeleteConfirmDialog().observeAsState()
    )

    // kalam split dialog
    KalamItemSplitDialog(
        kalamSplitManager = kalamDataProvider.getKalamSplitManagerDialog().observeAsState()
    )

    // kalam rename dialog
    KalamRenameDialog(
        kalamState = kalamDataProvider.getKalamRenameDialog().observeAsState(),
    )

    // add or update playlist dialog
    AddOrUpdatePlaylistDialog(
        playlistState = playlistDataProvider.getShowPlaylistAddUpdateDialog().observeAsState()
    )

    // playlist confirm delete dialog
    PlaylistConfirmDeleteDialog(
        playlistState = playlistDataProvider.getShowConfirmPlaylistDeleteDialog().observeAsState()
    )

    // show circular progress indicator dialog
    ShowCircularProgressDialog(
        showDialog = globalEventHandler.getShowCircularProgressDialog().observeAsState()
    )
}