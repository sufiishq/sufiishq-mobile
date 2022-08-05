package pk.sufiishq.app.ui.components

import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.res.stringResource
import pk.sufiishq.app.R
import pk.sufiishq.app.helpers.Screen
import pk.sufiishq.app.models.Kalam
import pk.sufiishq.app.models.KalamItemParam
import pk.sufiishq.app.utils.rem

@Composable
fun KalamItemPopupMenu(
    isExpanded: MutableState<Boolean>,
    kalamItemParam: KalamItemParam
) {

    val (kalam, kalamMenuItems, playerDataProvider, kalamDataProvider, _, _, _, _, trackType, _) = kalamItemParam

    val showDownloadDialog = rem(false)
    val showDeleteKalamConfirmDialog = rem(false)
    val showPlaylistDialog = rem(false)
    val showSplitterDialog = rem(false)
    val kalamSplitManager = rem(kalamDataProvider.getKalamSplitManager())
    val downloadError = playerDataProvider.getDownloadError().observeAsState()

    DropdownMenu(
        expanded = isExpanded.value,
        onDismissRequest = { isExpanded.value = false }) {

        val labelAddToPlaylist = stringResource(id = R.string.add_to_playlist)
        val labelMarkAsFavorite = stringResource(id = R.string.mark_as_favorite)
        val labelRemoveFavorite = stringResource(id = R.string.remove_favorite)
        val labelDownload = stringResource(id = R.string.download_label)
        val labelSplitKalam = stringResource(id = R.string.split_kalam)
        val labelDelete = stringResource(id = R.string.delete_label)

        kalamMenuItems
            .filter(
                filterLabels(
                    labelDownload,
                    labelMarkAsFavorite,
                    labelDelete,
                    trackType,
                    kalam
                )
            )
            .forEach { label ->
                DropdownMenuItem(onClick = {
                    when (label) {
                        labelAddToPlaylist -> showPlaylistDialog.value = true
                        labelMarkAsFavorite -> kalamDataProvider.markAsFavorite(kalam)
                        labelRemoveFavorite -> kalamDataProvider.removeFavorite(kalamItemParam)
                        labelDownload -> {
                            showDownloadDialog.value = true
                            playerDataProvider.startDownload(kalam)
                        }
                        labelDelete -> showDeleteKalamConfirmDialog.value = true
                        labelSplitKalam -> {
                            kalamSplitManager.value.reset()
                            kalamSplitManager.value.setKalam(kalam)
                            showSplitterDialog.value = true
                        }
                    }
                    isExpanded.value = false
                }) {
                    PopupMenuLabel(label = label)
                }
            }
    }

    // kalam download dialog
    KalamItemDownloadDialog(
        showDownloadDialog = showDownloadDialog,
        kalamItemParam = kalamItemParam
    )

    // playlist dialog
    PlaylistDialog(
        showPlaylistDialog = showPlaylistDialog,
        kalamItemParam = kalamItemParam
    )

    // kalam confirm delete dialog
    KalamItemConfirmDeleteDialog(
        showDeleteKalamConfirmDialog = showDeleteKalamConfirmDialog,
        kalamItemParam = kalamItemParam
    )

    // kalam download error dialog
    KalamDownloadErrorDialog(
        downloadError = downloadError,
        showDownloadDialog = showDownloadDialog,
        kalamItemParam = kalamItemParam
    )

    // kalam split dialog
    KalamItemSplitDialog(
        showSplitterDialog = showSplitterDialog,
        kalamSplitManager = kalamSplitManager,
        kalamItemParam = kalamItemParam
    )
}

private fun filterLabels(
    labelDownload: String,
    labelMarkAsFavorite: String,
    labelDelete: String,
    trackType: String,
    kalam: Kalam
): (label: String) -> Boolean {
    return {
        when (it) {
            labelDownload -> kalam.offlineSource.isEmpty()
            labelMarkAsFavorite -> kalam.isFavorite == 0
            labelDelete -> {
                if (trackType == Screen.Tracks.ALL) {
                    kalam.onlineSource.isEmpty()
                } else true
            }
            else -> true
        }
    }
}