package pk.sufiishq.app.ui.components.dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import pk.sufiishq.app.core.event.dispatcher.EventDispatcher
import pk.sufiishq.app.core.event.events.PlaylistEvents
import pk.sufiishq.app.models.Kalam
import pk.sufiishq.app.models.Playlist
import pk.sufiishq.app.ui.components.SufiIshqDialog
import pk.sufiishq.app.utils.toast

@Composable
fun PlaylistDialog(
    eventDispatcher: EventDispatcher,
    playlistState: State<List<Playlist>?>,
    showPlaylistDialog: State<Kalam?>
) {

    if (showPlaylistDialog.value != null) {

        val context = LocalContext.current
        val kalam = showPlaylistDialog.value!!

        if (playlistState.value?.isNotEmpty() == true) {

            val matColors = MaterialTheme.colors
            val playlistItems = playlistState.value!!

            SufiIshqDialog(onDismissRequest = {
                eventDispatcher.dispatch(PlaylistEvents.ShowPlaylistDialog(null))
            }) {

                Text(
                    text = "Playlist",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(12.dp)
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(0.dp, 300.dp)
                ) {

                    itemsIndexed(playlistItems) { index, item ->

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    eventDispatcher.dispatch(
                                        PlaylistEvents.AddToPlaylist(
                                            kalam,
                                            item
                                        )
                                    )
                                    eventDispatcher.dispatch(PlaylistEvents.ShowPlaylistDialog(null))
                                },
                        ) {

                            Text(
                                text = item.title,
                                modifier = Modifier.padding(12.dp)
                            )
                        }

                        if (index < playlistItems.lastIndex) {
                            Divider(color = matColors.secondaryVariant)
                        }
                    }
                }
            }
        } else {
            context.toast("No playlist found")
            eventDispatcher.dispatch(PlaylistEvents.ShowPlaylistDialog(null))
        }
    }
}