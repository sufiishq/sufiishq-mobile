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

package pk.sufiishq.app.ui.screen.playlist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import io.github.esentsov.PackagePrivate
import pk.sufiishq.app.R
import pk.sufiishq.app.core.playlist.controller.PlaylistController
import pk.sufiishq.app.core.playlist.model.Playlist
import pk.sufiishq.app.ui.components.dialogs.ConfirmDialogParam
import pk.sufiishq.app.ui.components.dialogs.ConfirmationDialog
import pk.sufiishq.app.utils.optString
import pk.sufiishq.app.utils.rem

@PackagePrivate
@Composable
fun PlaylistConfirmDeleteDialog(
    showDeletePlaylistDialog: MutableState<Playlist?>,
    playlistController: PlaylistController,
) {
    showDeletePlaylistDialog.value?.apply {
        val param = rem<ConfirmDialogParam?>(null)
        param.value =
            ConfirmDialogParam(
                message = optString(R.string.dynamic_confirm_delete_playlist, title),
                onConfirmed = {
                    playlistController.delete(this@apply)
                    showDeletePlaylistDialog.value = null
                },
                onDismissed = { showDeletePlaylistDialog.value = null },
            )
        ConfirmationDialog(state = param)
    }
}
