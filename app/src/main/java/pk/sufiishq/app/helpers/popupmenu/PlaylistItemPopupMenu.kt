package pk.sufiishq.app.helpers.popupmenu

import javax.inject.Inject
import pk.sufiishq.app.R
import pk.sufiishq.app.utils.getString
import pk.sufiishq.aurora.models.DataMenuItem

class PlaylistItemPopupMenu @Inject constructor() : PopupMenu {

    override fun getPopupMenuItems(): List<DataMenuItem> {
        return listOf(
            PopupMenuItem.Edit(getString(R.string.menu_item_rename)),
            PopupMenuItem.Delete(getString(R.string.menu_item_delete))
        )
    }
}