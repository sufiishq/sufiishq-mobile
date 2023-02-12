package pk.sufiishq.app.ui.screen.playlist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import io.github.esentsov.PackagePrivate
import pk.sufiishq.app.R
import pk.sufiishq.app.data.providers.PlaylistDataProvider
import pk.sufiishq.app.models.Playlist
import pk.sufiishq.app.ui.components.dialogs.ConfirmDialogParam
import pk.sufiishq.app.ui.components.dialogs.ConfirmationDialog
import pk.sufiishq.app.utils.optString
import pk.sufiishq.app.utils.rem

@PackagePrivate
@Composable
fun PlaylistConfirmDeleteDialog(
    showDeletePlaylistDialog: MutableState<Playlist?>,
    playlistDataProvider: PlaylistDataProvider
) {

    showDeletePlaylistDialog.value?.apply {

        val param = rem<ConfirmDialogParam?>(null)
        param.value = ConfirmDialogParam(
            message = optString(R.string.dynamic_confirm_delete_playlist, title),
            onConfirmed = {
                playlistDataProvider.delete(this@apply)
                showDeletePlaylistDialog.value = null
            },
            onDismissed = {
                showDeletePlaylistDialog.value = null
            }
        )
        ConfirmationDialog(state = param)
    }
}