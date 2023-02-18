/*
 * Copyright 2022-2023 SufiIshq
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pk.sufiishq.app.core.playlist.popupmenu

import pk.sufiishq.app.R
import pk.sufiishq.app.helpers.popupmenu.PopupMenu
import pk.sufiishq.app.helpers.popupmenu.PopupMenuItem
import pk.sufiishq.app.utils.getString
import pk.sufiishq.aurora.models.DataMenuItem
import javax.inject.Inject

class PlaylistItemPopupMenu @Inject constructor() : PopupMenu {

    override fun getPopupMenuItems(): List<DataMenuItem> {
        return listOf(
            PopupMenuItem.Edit(getString(R.string.menu_item_rename)),
            PopupMenuItem.Delete(getString(R.string.menu_item_delete)),
        )
    }
}
