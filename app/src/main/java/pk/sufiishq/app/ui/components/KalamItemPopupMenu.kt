package pk.sufiishq.app.ui.components

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import pk.sufiishq.app.core.event.events.KalamEvents
import pk.sufiishq.app.core.event.events.PlayerEvents
import pk.sufiishq.app.data.providers.KalamDataProvider
import pk.sufiishq.app.helpers.PopupMenuItem
import pk.sufiishq.app.helpers.TrackListType
import pk.sufiishq.app.models.Kalam
import pk.sufiishq.app.models.KalamDeleteItem
import pk.sufiishq.app.utils.dispatch
import pk.sufiishq.aurora.components.SIDropdownMenuItem
import pk.sufiishq.aurora.layout.SIDropdownMenu
import pk.sufiishq.aurora.models.DataMenuItem

@Composable
fun KalamItemPopupMenu(
    isExpanded: MutableState<Boolean>,
    kalam: Kalam,
    kalamDataProvider: KalamDataProvider,
    trackListType: TrackListType,
    coroutineScope: CoroutineScope
) {
    val context = LocalContext.current

    SIDropdownMenu(
        isExpand = isExpanded,
        onHide = {
            isExpanded.value = false
        }
    ) { menuItemColor ->

        kalamDataProvider
            .popupMenuItems(kalam, trackListType.type)
            .forEach {
                SIDropdownMenuItem(
                    label = it.label,
                    labelColor = menuItemColor,
                    iconTint = null,
                    resId = it.resId,
                    onClick = {
                        isExpanded.value = false
                        coroutineScope.launch {
                            handleClick(it, kalam, trackListType, context)
                        }
                    }
                )
            }
    }
}

private fun handleClick(
    popupMenuItem: DataMenuItem,
    kalam: Kalam,
    trackListType: TrackListType,
    context: Context
) {
    when (popupMenuItem) {
        is PopupMenuItem.AddToPlaylist -> PlayerEvents.ShowPlaylistDialog(kalam).dispatch()
        is PopupMenuItem.MarkAsFavorite -> PlayerEvents.MarkAsFavoriteKalam(kalam).dispatch()
        is PopupMenuItem.MarkAsNotFavorite -> PlayerEvents.RemoveFavoriteKalam(kalam).dispatch()
        is PopupMenuItem.Download -> PlayerEvents.StartDownload(kalam).dispatch()
        is PopupMenuItem.Split -> KalamEvents.ShowKalamSplitManagerDialog(kalam)
            .dispatch()
        is PopupMenuItem.Edit -> KalamEvents.ShowKalamRenameDialog(kalam).dispatch()
        is PopupMenuItem.Share -> KalamEvents.ShareKalam(kalam, context).dispatch()
        is PopupMenuItem.Delete -> KalamEvents.ShowKalamConfirmDeleteDialog(
            KalamDeleteItem(
                kalam,
                trackListType
            )
        ).dispatch()
    }
}