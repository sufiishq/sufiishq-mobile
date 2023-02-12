package pk.sufiishq.app.ui.screen.tracks

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.esentsov.PackagePrivate
import pk.sufiishq.app.R
import pk.sufiishq.app.data.providers.KalamDataProvider
import pk.sufiishq.app.utils.optString
import pk.sufiishq.aurora.components.SIDivider
import pk.sufiishq.aurora.layout.SIDialog
import pk.sufiishq.aurora.layout.SILazyColumn
import pk.sufiishq.aurora.widgets.SIDataRow

@PackagePrivate
@Composable
fun PlaylistDialog(
    kalamDataProvider: KalamDataProvider
) {

    kalamDataProvider
        .showPlaylistDialog()
        .observeAsState()
        .value
        ?.apply {

            val kalam = first
            val playlistItems = second

            SIDialog(
                title = optString(R.string.title_playlist),
                onDismissRequest = {
                    kalamDataProvider.dismissPlaylistDialog()
                }
            ) {

                SILazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(0.dp, 300.dp),
                    contentPadding = PaddingValues(0.dp),
                    content = {
                        itemsIndexed(playlistItems) { index, item ->

                            SIDataRow(
                                title = item.title,
                                rowHeight = 50,
                                onClick = {
                                    kalamDataProvider.addToPlaylist(kalam, item)
                                }
                            )

                            if (index < playlistItems.lastIndex) {
                                SIDivider()
                            }
                        }
                    }
                )
            }
        }
}