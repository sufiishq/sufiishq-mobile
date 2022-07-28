package pk.sufiishq.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import pk.sufiishq.app.data.providers.PlaylistDataProvider
import pk.sufiishq.app.models.Playlist
import pk.sufiishq.app.utils.checkValue
import pk.sufiishq.app.utils.ifNotEmpty
import pk.sufiishq.app.utils.rem

@Composable
fun AddOrUpdatePlaylistDialog(
    showAddPlaylistDialog: MutableState<Boolean>,
    playlist: Playlist,
    playlistDataProvider: PlaylistDataProvider
) {

    if (showAddPlaylistDialog.value) {
        val playlistTitle = rem(playlist.title)
        val error = rem(playlist.title.checkValue("", "Title cannot be empty"))

        Dialog(onDismissRequest = { showAddPlaylistDialog.value = false }) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colors.surface,
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    Text(text = "Playlist", style = MaterialTheme.typography.subtitle1)

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(weight = 1f, fill = false)
                            .padding(vertical = 16.dp)
                    ) {
                        OutlinedTextFieldValidation(
                            value = playlistTitle.value,
                            onValueChange = {
                                playlistTitle.value = it
                                error.value =
                                    playlistTitle.value.checkValue("", "Title cannot be empty")
                            },
                            keyboardActions = KeyboardActions(onDone = {
                                playlistTitle.value.ifNotEmpty {
                                    addOrUpdatePlaylist(
                                        showAddPlaylistDialog,
                                        Playlist(playlist.id, it),
                                        playlistDataProvider
                                    )
                                }
                            }),
                            modifier = Modifier.padding(top = 8.dp),
                            label = {
                                Text(text = "Title")
                            },
                            error = error.value
                        )
                    }

                    // BUTTONS
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { showAddPlaylistDialog.value = false }) {
                            Text(text = "Cancel")
                        }
                        TextButton(onClick = {
                            playlistTitle.value.ifNotEmpty {
                                addOrUpdatePlaylist(
                                    showAddPlaylistDialog,
                                    Playlist(playlist.id, it),
                                    playlistDataProvider
                                )
                            }
                        }) {
                            Text(text = if (playlist.title.isEmpty()) "Add" else "Update")
                        }
                    }
                }
            }
        }
    }

}

private fun addOrUpdatePlaylist(
    showAddPlaylistDialog: MutableState<Boolean>,
    playlist: Playlist,
    playlistDataProvider: PlaylistDataProvider
) {
    if (playlist.id == 0) {
        playlistDataProvider.add(playlist)
    } else {
        playlistDataProvider.update(playlist)
    }
    showAddPlaylistDialog.value = false
}