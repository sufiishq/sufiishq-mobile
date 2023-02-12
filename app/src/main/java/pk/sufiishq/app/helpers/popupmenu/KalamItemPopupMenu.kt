package pk.sufiishq.app.helpers.popupmenu

import javax.inject.Inject
import pk.sufiishq.app.R
import pk.sufiishq.app.utils.getString
import pk.sufiishq.aurora.models.DataMenuItem

class KalamItemPopupMenu @Inject constructor() : PopupMenu {

    override fun getPopupMenuItems(): List<DataMenuItem> {
        return listOf(
            PopupMenuItem.MarkAsFavorite(getString(R.string.menu_item_mark_as_favorite)),
            PopupMenuItem.MarkAsNotFavorite(getString(R.string.menu_item_remove_favorite)),
            PopupMenuItem.Download(getString(R.string.menu_item_download)),
            PopupMenuItem.AddToPlaylist(getString(R.string.menu_item_add_to_playlist)),
            PopupMenuItem.Share(getString(R.string.menu_item_share)),
            PopupMenuItem.Split(getString(R.string.menu_item_split_kalam)),
            PopupMenuItem.Delete(getString(R.string.menu_item_delete))
        )
    }
}