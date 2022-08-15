package pk.sufiishq.app.ui.components.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import pk.sufiishq.app.core.event.dispatcher.EventDispatcher
import pk.sufiishq.app.data.providers.KalamDataProvider
import pk.sufiishq.app.data.providers.PlayerDataProvider
import pk.sufiishq.app.data.providers.PlaylistDataProvider
import pk.sufiishq.app.helpers.GlobalEventHandler

@Composable
fun DialogHolder(
    eventDispatcher: EventDispatcher,
    playerDataProvider: PlayerDataProvider,
    playlistDataProvider: PlaylistDataProvider,
    kalamDataProvider: KalamDataProvider,
    globalEventHandler: GlobalEventHandler
) {

    val kalamDownloadState = playerDataProvider.getKalamDownloadState().observeAsState()

    // kalam download start dialog
    KalamDownloadStartedDialog(
        eventDispatcher = eventDispatcher,
        kalamDownloadState = kalamDownloadState
    )

    // kalam download in-progress dialog
    KalamDownloadInProgressDialog(
        eventDispatcher = eventDispatcher,
        kalamDownloadState = kalamDownloadState
    )

    // kalam download completed dialog
    KalamDownloadCompletedDialog(
        eventDispatcher = eventDispatcher,
        kalamDownloadState = kalamDownloadState
    )

    // kalam download error dialog
    KalamDownloadErrorDialog(
        eventDispatcher = eventDispatcher,
        kalamDownloadState = kalamDownloadState
    )

    // playlist dialog
    PlaylistDialog(
        eventDispatcher = eventDispatcher,
        playlistState = playlistDataProvider.getAll().observeAsState(),
        showPlaylistDialog = playlistDataProvider.getShowPlaylistDialog().observeAsState()
    )

    // kalam confirm delete dialog
    KalamConfirmDeleteDialog(
        eventDispatcher = eventDispatcher,
        kalamDeleteItem = kalamDataProvider.getKalamDeleteConfirmDialog().observeAsState()
    )

    // kalam split dialog
    KalamItemSplitDialog(
        kalamSplitManager = kalamDataProvider.getKalamSplitManagerDialog().observeAsState(),
        eventDispatcher = eventDispatcher
    )

    // kalam rename dialog
    KalamRenameDialog(
        kalamState = kalamDataProvider.getKalamRenameDialog().observeAsState(),
        eventDispatcher = eventDispatcher,
    )

    // add or update playlist dialog
    AddOrUpdatePlaylistDialog(
        playlistState = playlistDataProvider.getShowPlaylistAddUpdateDialog().observeAsState(),
        eventDispatcher = eventDispatcher
    )

    // playlist confirm delete dialog
    PlaylistConfirmDeleteDialog(
        playlistState = playlistDataProvider.getShowConfirmPlaylistDeleteDialog().observeAsState(),
        eventDispatcher = eventDispatcher
    )

    // show circular progress indicator dialog
    ShowCircularProgressDialog(
        showDialog = globalEventHandler.getShowCircularProgressDialog().observeAsState()
    )
}