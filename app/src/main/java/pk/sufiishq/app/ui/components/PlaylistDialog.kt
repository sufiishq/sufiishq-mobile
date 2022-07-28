package pk.sufiishq.app.ui.components

import android.widget.Toast
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
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import pk.sufiishq.app.models.KalamItemParam

@Composable
fun PlaylistDialog(
    showPlaylistDialog: MutableState<Boolean>,
    kalamItemParam: KalamItemParam
) {

    if (showPlaylistDialog.value) {

        val (kalam, _, _, kalamDataProvider, _, _, playlistItems, _, _, _) = kalamItemParam

        val matColors = MaterialTheme.colors
        val context = LocalContext.current

        if (playlistItems.isNotEmpty()) {

            SufiIshqDialog(onDismissRequest = { showPlaylistDialog.value = false }) {

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
                                    kalam.playlistId = item.id
                                    kalamDataProvider.update(kalam)
                                    Toast
                                        .makeText(
                                            context,
                                            "${kalam.title} added in ${item.title} Playlist",
                                            Toast.LENGTH_LONG
                                        )
                                        .show()
                                    showPlaylistDialog.value = false
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
            Toast.makeText(context, "No playlist found", Toast.LENGTH_LONG).show()
            showPlaylistDialog.value = false
        }
    }
}