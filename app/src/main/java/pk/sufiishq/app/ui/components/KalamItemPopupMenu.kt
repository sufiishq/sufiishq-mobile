package pk.sufiishq.app.ui.components

import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import pk.sufiishq.app.R
import pk.sufiishq.app.core.event.dispatcher.EventDispatcher
import pk.sufiishq.app.core.event.events.GlobalEvents
import pk.sufiishq.app.core.event.events.KalamEvents
import pk.sufiishq.app.core.event.events.PlayerEvents
import pk.sufiishq.app.core.event.events.PlaylistEvents
import pk.sufiishq.app.helpers.ScreenType
import pk.sufiishq.app.helpers.TrackListType
import pk.sufiishq.app.models.Kalam
import pk.sufiishq.app.models.KalamDeleteItem

@Composable
fun KalamItemPopupMenu(
    isExpanded: MutableState<Boolean>,
    kalam: Kalam,
    kalamMenuItems: List<String>,
    trackListType: TrackListType
) {

    val eventDispatcher = EventDispatcher.getInstance()
    val context = LocalContext.current

    DropdownMenu(
        expanded = isExpanded.value,
        onDismissRequest = { isExpanded.value = false }) {

        val labelAddToPlaylist = stringResource(id = R.string.add_to_playlist)
        val labelMarkAsFavorite = stringResource(id = R.string.mark_as_favorite)
        val labelRemoveFavorite = stringResource(id = R.string.remove_favorite)
        val labelDownload = stringResource(id = R.string.download_label)
        val labelSplitKalam = stringResource(id = R.string.split_kalam)
        val labelRename = stringResource(id = R.string.rename_label)
        val labelShare = stringResource(id = R.string.share_label)
        val labelDelete = stringResource(id = R.string.delete_label)

        kalamMenuItems
            .filter(
                filterLabels(
                    labelDownload,
                    labelMarkAsFavorite,
                    labelDelete,
                    labelShare,
                    trackListType.type,
                    kalam
                )
            )
            .forEach { label ->
                DropdownMenuItem(onClick = {
                    when (label) {
                        labelAddToPlaylist -> eventDispatcher.dispatch(
                            PlaylistEvents.ShowPlaylistDialog(
                                kalam
                            )
                        )
                        labelMarkAsFavorite -> eventDispatcher.dispatch(
                            KalamEvents.MarkAsFavoriteKalam(
                                kalam
                            )
                        )
                        labelRemoveFavorite -> eventDispatcher.dispatch(
                            KalamEvents.RemoveFavoriteKalam(
                                kalam
                            )
                        )
                        labelDownload -> eventDispatcher.dispatch(PlayerEvents.StartDownload(kalam))
                        labelDelete -> eventDispatcher.dispatch(
                            KalamEvents.ShowKalamConfirmDeleteDialog(
                                KalamDeleteItem(kalam, trackListType)
                            )
                        )
                        labelSplitKalam -> {
                            eventDispatcher.dispatch(KalamEvents.ShowKalamSplitManagerDialog(kalam))
                        }
                        labelRename -> eventDispatcher.dispatch(
                            KalamEvents.ShowKalamRenameDialog(
                                kalam
                            )
                        )
                        labelShare -> eventDispatcher.dispatch(
                            GlobalEvents.ShareKalam(
                                kalam,
                                context
                            )
                        )
                    }
                    isExpanded.value = false
                }) {
                    PopupMenuLabel(label = label)
                }
            }
    }
}

private fun filterLabels(
    labelDownload: String,
    labelMarkAsFavorite: String,
    labelDelete: String,
    labelShare: String,
    trackType: String,
    kalam: Kalam
): (label: String) -> Boolean {
    return {
        when (it) {
            labelDownload -> kalam.offlineSource.isEmpty()
            labelMarkAsFavorite -> kalam.isFavorite == 0
            labelShare -> kalam.onlineSource.isNotEmpty()
            labelDelete -> {
                if (trackType == ScreenType.Tracks.ALL) {
                    kalam.onlineSource.isEmpty()
                } else true
            }
            else -> true
        }
    }
}