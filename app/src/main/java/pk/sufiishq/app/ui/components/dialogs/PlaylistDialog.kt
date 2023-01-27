package pk.sufiishq.app.ui.components.dialogs

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import pk.sufiishq.app.core.event.events.PlayerEvents
import pk.sufiishq.app.models.Kalam
import pk.sufiishq.app.models.Playlist
import pk.sufiishq.app.utils.dispatch
import pk.sufiishq.app.utils.toast
import pk.sufiishq.aurora.components.SIDivider
import pk.sufiishq.aurora.components.SIHeightSpace
import pk.sufiishq.aurora.components.SIText
import pk.sufiishq.aurora.components.TextSize
import pk.sufiishq.aurora.layout.SIDialog
import pk.sufiishq.aurora.layout.SILazyColumn
import pk.sufiishq.aurora.theme.AuroraColor

@Composable
fun PlaylistDialog(
    playlistState: State<List<Playlist>?>,
    showPlaylistDialog: State<Kalam?>
) {

    if (showPlaylistDialog.value != null) {

        val context = LocalContext.current
        val kalam = showPlaylistDialog.value!!

        if (playlistState.value?.isNotEmpty() == true) {

            val playlistItems = playlistState.value!!

            SIDialog(
                onDismissRequest = {
                    PlayerEvents.ShowPlaylistDialog(null).dispatch()
                }
            ) { textColor ->

                SIText(
                    text = "Playlist",
                    textColor = textColor,
                    textSize = TextSize.Regular,
                    fontWeight = FontWeight.Bold
                )
                
                SIHeightSpace(value = 12)
                SIDivider(color = AuroraColor.Secondary)

                SILazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(0.dp, 300.dp),
                    content = {
                        itemsIndexed(playlistItems) { index, item ->

                            SIText(
                                modifier = Modifier.fillMaxWidth(),
                                text = item.title,
                                textColor = textColor,
                                onClick = {
                                    PlayerEvents
                                        .AddKalamInPlaylist(
                                            kalam,
                                            item
                                        )
                                        .dispatch(
                                            PlayerEvents.ShowPlaylistDialog(null)
                                        )
                                }
                            )

                            if (index < playlistItems.lastIndex) {
                                SIDivider(color = AuroraColor.Primary)
                            }
                        }
                    }
                )
            }
        } else {
            context.toast("No playlist found")
            PlayerEvents.ShowPlaylistDialog(null).dispatch()
        }
    }
}