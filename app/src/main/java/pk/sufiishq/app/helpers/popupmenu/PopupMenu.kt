package pk.sufiishq.app.helpers.popupmenu

import pk.sufiishq.aurora.models.DataMenuItem

interface PopupMenu {
    fun getPopupMenuItems(): List<DataMenuItem>
}