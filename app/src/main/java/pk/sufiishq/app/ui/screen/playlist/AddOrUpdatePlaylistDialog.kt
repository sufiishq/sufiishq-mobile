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

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import io.github.esentsov.PackagePrivate
import pk.sufiishq.app.feature.playlist.controller.PlaylistController
import pk.sufiishq.app.feature.playlist.model.Playlist
import pk.sufiishq.app.ui.components.OutlinedTextField
import pk.sufiishq.app.utils.Constants.PLAYLIST_TITLE_LENGTH
import pk.sufiishq.app.utils.TextRes
import pk.sufiishq.app.utils.extention.optString
import pk.sufiishq.app.utils.quickToast
import pk.sufiishq.app.utils.rem
import pk.sufiishq.aurora.layout.SIDialog

@PackagePrivate
@Composable
fun AddOrUpdatePlaylistDialog(
    playlistController: PlaylistController,
    showAddUpdatePlaylistDialog: MutableState<Playlist?>,
) {
    showAddUpdatePlaylistDialog.value?.apply {
        val playlistTitle = rem(title)

        SIDialog(
            title =
            if (title.isEmpty()) {
                optString(TextRes.label_add_new_playlist)
            } else {
                optString(TextRes.label_rename_playlist)
            },
            onNoText = optString(TextRes.label_cancel),
            onNoClick = { dismissDialog(showAddUpdatePlaylistDialog) },
            onDismissRequest = { dismissDialog(showAddUpdatePlaylistDialog) },
            onYesText =
            if (title.isEmpty()) {
                optString(TextRes.label_add)
            } else {
                optString(TextRes.label_update)
            },
            onYesClick = {
                addOrUpdatePlaylist(
                    showAddUpdatePlaylistDialog,
                    playlistController,
                    Playlist(id, playlistTitle.value.trim()),
                )
            },
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = playlistTitle.value,
                onValueChange = { playlistTitle.value = it },
                label = optString(TextRes.label_playlist_title),
                emptyFieldError = optString(TextRes.msg_kalam_title_required),
                maxLength = PLAYLIST_TITLE_LENGTH,
            )
        }
    }
}

private fun addOrUpdatePlaylist(
    showAddUpdatePlaylistDialog: MutableState<Playlist?>,
    playlistController: PlaylistController,
    playlist: Playlist,
) {
    if (playlist.title.isEmpty()) {
        quickToast(TextRes.msg_kalam_title_required)
    } else {
        if (playlist.id == 0) {
            playlistController.add(playlist)
        } else {
            playlistController.update(playlist)
        }
        dismissDialog(showAddUpdatePlaylistDialog)
    }
}

private fun dismissDialog(
    showAddUpdatePlaylistDialog: MutableState<Playlist?>,
) {
    showAddUpdatePlaylistDialog.value = null
}
