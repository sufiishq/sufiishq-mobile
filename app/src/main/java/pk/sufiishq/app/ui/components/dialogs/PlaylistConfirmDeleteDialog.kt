package pk.sufiishq.app.ui.components.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import pk.sufiishq.app.core.event.dispatcher.EventDispatcher
import pk.sufiishq.app.core.event.events.PlaylistEvents
import pk.sufiishq.app.models.Playlist

@Composable
fun PlaylistConfirmDeleteDialog(
    playlistState: State<Playlist?>
) {

    playlistState.value?.apply {

        val eventDispatcher = EventDispatcher.getInstance()

        SufiIshqDialog(
            onDismissRequest = {
                hideDialog()
            }
        ) {
            Text(
                buildAnnotatedString {
                    append("Are you sure you want to delete ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(title)
                    }
                    append(" playlist?")
                }
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = {
                    hideDialog()
                }) {
                    Text(text = "NO")
                }
                TextButton(onClick = {
                    eventDispatcher.dispatch(PlaylistEvents.Delete(this@apply))
                    hideDialog()

                }) {
                    Text(text = "YES")
                }
            }
        }
    }
}

private fun hideDialog() {
    EventDispatcher.getInstance().dispatch(PlaylistEvents.ShowConfirmDeletePlaylistDialog(null))
}