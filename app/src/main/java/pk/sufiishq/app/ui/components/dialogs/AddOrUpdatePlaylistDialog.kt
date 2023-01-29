package pk.sufiishq.app.ui.components.dialogs

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import pk.sufiishq.app.core.event.events.PlaylistEvents
import pk.sufiishq.app.models.Playlist
import pk.sufiishq.app.ui.components.SingleOutlinedTextField
import pk.sufiishq.app.utils.PLAYLIST_TITLE_LENGTH
import pk.sufiishq.app.utils.checkValue
import pk.sufiishq.app.utils.dispatch
import pk.sufiishq.app.utils.ifNotEmpty
import pk.sufiishq.app.utils.rem
import pk.sufiishq.aurora.layout.SIDialog

@Composable
fun AddOrUpdatePlaylistDialog(
    playlistState: State<Playlist?>
) {

    playlistState.value?.apply {

        val playlistTitle = rem(title)
        val errorText = rem(title.checkValue("", "Title cannot be empty"))
        val isError = rem(false)

        SIDialog(
            onNoText = "Cancel",
            onNoClick = ::hideDialog,
            onDismissRequest = ::hideDialog,
            onYesText = if (title.isEmpty()) "Add" else "Update",
            onYesClick = {
                isError.value = errorText.value.isNotEmpty()
                playlistTitle.value.ifNotEmpty {
                    addOrUpdatePlaylist(
                        Playlist(id, it)
                    )
                }
            }
        ) {

            SingleOutlinedTextField(
                value = playlistTitle.value,
                onValueChange = {
                    playlistTitle.value = it
                    errorText.value =
                        playlistTitle.value.trim().checkValue("", "Title cannot be empty")
                    isError.value = errorText.value.isNotEmpty()
                },
                keyboardActions = KeyboardActions(onDone = {
                    isError.value = errorText.value.isNotEmpty()
                    playlistTitle.value.ifNotEmpty {
                        addOrUpdatePlaylist(
                            Playlist(id, it)
                        )
                    }
                }),
                label = "Playlist Title",
                errorText = errorText.value,
                isError = isError,
                maxLength = PLAYLIST_TITLE_LENGTH
            )
        }
    }

}

private fun addOrUpdatePlaylist(
    playlist: Playlist
) {

    if (playlist.id == 0) {
        PlaylistEvents.Add(playlist).dispatch()
    } else {
        PlaylistEvents.Update(playlist).dispatch()
    }

    hideDialog()
}

private fun hideDialog() {
    PlaylistEvents.ShowAddUpdatePlaylistDialog(null).dispatch()
}