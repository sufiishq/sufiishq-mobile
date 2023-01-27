package pk.sufiishq.app.ui.components.player

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.LiveData
import pk.sufiishq.app.core.event.events.PlayerEvents
import pk.sufiishq.app.helpers.PopupMenuItem
import pk.sufiishq.app.models.Kalam
import pk.sufiishq.app.utils.dispatch
import pk.sufiishq.app.utils.optValue
import pk.sufiishq.aurora.models.DataMenuItem
import pk.sufiishq.aurora.widgets.SIPopupMenu

@Composable
fun PlayerOverflowMenu(
    menu: LiveData<List<DataMenuItem>>,
    showMenu: MutableState<Boolean>,
    kalam: Kalam
) {
    menu
        .observeAsState()
        .optValue(listOf())
        .apply {
            SIPopupMenu(
                isExpanded = showMenu,
                data = this,
                onClick = {
                    handleMenuItemClicked(it, kalam)
                }
            )
        }
}

private fun handleMenuItemClicked(popupMenuItem: DataMenuItem, kalam: Kalam) {
    when (popupMenuItem) {
        is PopupMenuItem.AddToPlaylist -> PlayerEvents.ShowPlaylistDialog(
            kalam
        ).dispatch()
        is PopupMenuItem.MarkAsFavorite -> PlayerEvents.MarkAsFavoriteKalam(
            kalam
        ).dispatch()
        is PopupMenuItem.MarkAsNotFavorite -> PlayerEvents.RemoveFavoriteKalam(
            kalam
        ).dispatch()
        is PopupMenuItem.Download -> PlayerEvents.StartDownload(kalam)
            .dispatch()
    }
}