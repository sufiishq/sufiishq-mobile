package pk.sufiishq.app.ui.screen.playlist

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import io.github.esentsov.PackagePrivate
import pk.sufiishq.app.R
import pk.sufiishq.app.data.controller.PlaylistController
import pk.sufiishq.app.models.Playlist
import pk.sufiishq.app.ui.components.OutlinedTextField
import pk.sufiishq.app.utils.PLAYLIST_TITLE_LENGTH
import pk.sufiishq.app.utils.optString
import pk.sufiishq.app.utils.quickToast
import pk.sufiishq.app.utils.rem
import pk.sufiishq.aurora.layout.SIDialog

@PackagePrivate
@Composable
fun AddOrUpdatePlaylistDialog(
    playlistController: PlaylistController,
    showAddUpdatePlaylistDialog: MutableState<Playlist?>
) {

    showAddUpdatePlaylistDialog.value?.apply {

        val playlistTitle = rem(title)

        SIDialog(
            title = if (title.isEmpty()) optString(R.string.label_add_new_playlist) else optString(R.string.label_rename_playlist),
            onNoText = optString(R.string.label_cancel),
            onNoClick = {
                dismissDialog(showAddUpdatePlaylistDialog)
            },
            onDismissRequest = {
                dismissDialog(showAddUpdatePlaylistDialog)
            },
            onYesText = if (title.isEmpty()) optString(R.string.label_add) else optString(R.string.label_update),
            onYesClick = {

                addOrUpdatePlaylist(
                    showAddUpdatePlaylistDialog,
                    playlistController,
                    Playlist(id, playlistTitle.value.trim())
                )
            }
        ) {

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = playlistTitle.value,
                onValueChange = {
                    playlistTitle.value = it
                },
                label = optString(R.string.label_playlist_title),
                emptyFieldError = optString(R.string.msg_kalam_title_required),
                maxLength = PLAYLIST_TITLE_LENGTH
            )
        }
    }

}

private fun addOrUpdatePlaylist(
    showAddUpdatePlaylistDialog: MutableState<Playlist?>,
    playlistController: PlaylistController,
    playlist: Playlist
) {

    if (playlist.title.isEmpty()) {
        quickToast(R.string.msg_kalam_title_required)
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