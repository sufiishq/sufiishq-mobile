package pk.sufiishq.app.ui.screen.tracks

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.platform.LocalContext
import io.github.esentsov.PackagePrivate
import pk.sufiishq.app.data.providers.KalamDataProvider
import pk.sufiishq.app.helpers.TrackListType
import pk.sufiishq.app.helpers.popupmenu.PopupMenuItem
import pk.sufiishq.app.models.Kalam
import pk.sufiishq.app.models.KalamDeleteItem
import pk.sufiishq.aurora.components.SIDropdownMenuItem
import pk.sufiishq.aurora.layout.SIDropdownMenu
import pk.sufiishq.aurora.models.DataMenuItem

@PackagePrivate
@Composable
fun KalamItemPopupMenu(
    isExpanded: MutableState<Boolean>,
    kalam: Kalam,
    kalamDataProvider: KalamDataProvider,
    trackListType: TrackListType,
) {
    val context = LocalContext.current

    SIDropdownMenu(isExpand = isExpanded, onHide = {
        isExpanded.value = false
    }) { menuItemColor ->

        kalamDataProvider.popupMenuItems(kalam, trackListType.type).forEach {
            SIDropdownMenuItem(label = it.label,
                labelColor = menuItemColor,
                iconTint = null,
                resId = it.resId,
                onClick = {
                    isExpanded.value = false
                    handleClick(it, kalam, trackListType, context, kalamDataProvider)
                })
        }
    }
}

private fun handleClick(
    popupMenuItem: DataMenuItem,
    kalam: Kalam,
    trackListType: TrackListType,
    context: Context,
    kalamDataProvider: KalamDataProvider
) {
    when (popupMenuItem) {
        is PopupMenuItem.AddToPlaylist -> kalamDataProvider.showPlaylistDialog(kalam)
        is PopupMenuItem.MarkAsFavorite -> kalamDataProvider.markAsFavorite(kalam)
        is PopupMenuItem.MarkAsNotFavorite -> kalamDataProvider.removeFavorite(kalam)
        is PopupMenuItem.Download -> kalamDataProvider.startDownload(kalam)
        is PopupMenuItem.Split -> kalamDataProvider.showKalamSplitDialog(kalam)
        is PopupMenuItem.Share -> kalamDataProvider.shareKalam(kalam, context as ComponentActivity)
        is PopupMenuItem.Delete -> kalamDataProvider.showKalamConfirmDeleteDialog(
            KalamDeleteItem(
                kalam, trackListType
            )
        )
    }
}