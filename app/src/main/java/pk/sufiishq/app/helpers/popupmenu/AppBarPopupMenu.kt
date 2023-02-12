package pk.sufiishq.app.helpers.popupmenu

import javax.inject.Inject
import pk.sufiishq.app.R
import pk.sufiishq.app.utils.getString
import pk.sufiishq.aurora.models.DataMenuItem

class AppBarPopupMenu @Inject constructor() : PopupMenu {

    override fun getPopupMenuItems(): List<DataMenuItem> {
        return listOf(
            PopupMenuItem.Share(getString(R.string.menu_item_share)),
            PopupMenuItem.Facebook(getString(R.string.menu_item_facebook)),
            PopupMenuItem.Help(getString(R.string.menu_item_help)),
            PopupMenuItem.Theme(getString(R.string.menu_item_theme))
        )
    }
}