package pk.sufiishq.app.ui.components.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import pk.sufiishq.app.core.event.events.PlaylistEvents
import pk.sufiishq.app.models.Playlist
import pk.sufiishq.app.utils.dispatch
import pk.sufiishq.aurora.components.SIText
import pk.sufiishq.aurora.layout.SIDialog

@Composable
fun PlaylistConfirmDeleteDialog(
    playlistState: State<Playlist?>
) {

    playlistState.value?.apply {

        SIDialog(
            onNoText = "NO",
            onNoClick = ::hideDialog,
            onYesText = "YES",
            onYesClick = {
                PlaylistEvents.Delete(this@apply).dispatch()
                hideDialog()
            },
            onDismissRequest = ::hideDialog
        ) { textColor ->
            SIText(
                text = buildAnnotatedString {
                    append("Are you sure you want to delete ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(title)
                    }
                    append(" playlist?")
                },
                textColor = textColor
            )
        }
    }
}

private fun hideDialog() {
    PlaylistEvents.ShowConfirmDeletePlaylistDialog(null).dispatch()
}