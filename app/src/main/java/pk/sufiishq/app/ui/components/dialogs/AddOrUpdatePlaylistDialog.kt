package pk.sufiishq.app.ui.components.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import pk.sufiishq.app.core.event.dispatcher.EventDispatcher
import pk.sufiishq.app.core.event.events.PlaylistEvents
import pk.sufiishq.app.models.Playlist
import pk.sufiishq.app.ui.components.OutlinedTextFieldValidation
import pk.sufiishq.app.utils.PLAYLIST_TITLE_LENGTH
import pk.sufiishq.app.utils.checkValue
import pk.sufiishq.app.utils.ifNotEmpty
import pk.sufiishq.app.utils.rem

@Composable
fun AddOrUpdatePlaylistDialog(
    playlistState: State<Playlist?>,
    eventDispatcher: EventDispatcher
) {

    playlistState.value?.apply {
        val playlistTitle = rem(title)
        val error = rem(title.checkValue("", "Title cannot be empty"))

        Dialog(onDismissRequest = {
            hideDialog(eventDispatcher)
        }) {
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
                                        Playlist(id, it),
                                        eventDispatcher
                                    )
                                }
                            }),
                            modifier = Modifier.padding(top = 8.dp),
                            label = {
                                Text(text = "Title")
                            },
                            error = error.value,
                            maxLength = PLAYLIST_TITLE_LENGTH
                        )
                    }

                    // BUTTONS
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { hideDialog(eventDispatcher) }) {
                            Text(text = "Cancel")
                        }
                        TextButton(onClick = {
                            playlistTitle.value.ifNotEmpty {
                                addOrUpdatePlaylist(
                                    Playlist(id, it),
                                    eventDispatcher
                                )
                            }
                        }) {
                            Text(text = if (title.isEmpty()) "Add" else "Update")
                        }
                    }
                }
            }
        }
    }

}

private fun addOrUpdatePlaylist(
    playlist: Playlist,
    eventDispatcher: EventDispatcher
) {
    if (playlist.id == 0) {
        eventDispatcher.dispatch(PlaylistEvents.Add(playlist))
    } else {
        eventDispatcher.dispatch(PlaylistEvents.Update(playlist))
    }

    hideDialog(eventDispatcher)
}

private fun hideDialog(
    eventDispatcher: EventDispatcher
) {
    eventDispatcher.dispatch(PlaylistEvents.ShowAddUpdatePlaylistDialog(null))
}