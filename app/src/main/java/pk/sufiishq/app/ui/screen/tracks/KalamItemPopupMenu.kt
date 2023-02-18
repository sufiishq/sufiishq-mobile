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

package pk.sufiishq.app.ui.screen.tracks

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.platform.LocalContext
import io.github.esentsov.PackagePrivate
import pk.sufiishq.app.feature.kalam.controller.KalamController
import pk.sufiishq.app.feature.kalam.helper.TrackListType
import pk.sufiishq.app.feature.kalam.model.Kalam
import pk.sufiishq.app.feature.kalam.model.KalamDeleteItem
import pk.sufiishq.app.helpers.popupmenu.PopupMenuItem
import pk.sufiishq.aurora.components.SIDropdownMenuItem
import pk.sufiishq.aurora.layout.SIDropdownMenu
import pk.sufiishq.aurora.models.DataMenuItem

@PackagePrivate
@Composable
fun KalamItemPopupMenu(
    isExpanded: MutableState<Boolean>,
    kalam: Kalam,
    kalamController: pk.sufiishq.app.feature.kalam.controller.KalamController,
    trackListType: TrackListType,
) {
    val context = LocalContext.current

    SIDropdownMenu(isExpand = isExpanded, onHide = { isExpanded.value = false }) { menuItemColor ->
        kalamController.popupMenuItems(kalam, trackListType.type).forEach {
            SIDropdownMenuItem(
                label = it.label,
                labelColor = menuItemColor,
                iconTint = null,
                resId = it.resId,
                onClick = {
                    isExpanded.value = false
                    handleClick(it, kalam, trackListType, context, kalamController)
                },
            )
        }
    }
}

private fun handleClick(
    popupMenuItem: DataMenuItem,
    kalam: Kalam,
    trackListType: TrackListType,
    context: Context,
    kalamController: pk.sufiishq.app.feature.kalam.controller.KalamController,
) {
    when (popupMenuItem) {
        is PopupMenuItem.AddToPlaylist -> kalamController.showPlaylistDialog(kalam)
        is PopupMenuItem.MarkAsFavorite -> kalamController.markAsFavorite(kalam)
        is PopupMenuItem.MarkAsNotFavorite -> kalamController.removeFavorite(kalam)
        is PopupMenuItem.Download -> kalamController.startDownload(kalam)
        is PopupMenuItem.Split -> kalamController.showKalamSplitDialog(kalam)
        is PopupMenuItem.Share -> kalamController.shareKalam(kalam, context as ComponentActivity)
        is PopupMenuItem.Delete ->
            kalamController.showKalamConfirmDeleteDialog(
                KalamDeleteItem(
                    kalam,
                    trackListType,
                ),
            )
    }
}
