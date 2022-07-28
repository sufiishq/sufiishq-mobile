package pk.sufiishq.app.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import pk.sufiishq.app.data.providers.PlaylistDataProvider
import pk.sufiishq.app.models.Playlist
import pk.sufiishq.app.ui.components.AddOrUpdatePlaylistDialog
import pk.sufiishq.app.ui.components.PlaylistItem
import pk.sufiishq.app.ui.theme.SufiIshqTheme
import pk.sufiishq.app.utils.*

@Composable
fun PlaylistView(playlistDataProvider: PlaylistDataProvider, navController: NavController) {

    val matColors = MaterialTheme.colors
    val showAddPlaylistDialog = rem(false)
    val playlist = rem(Playlist(0, ""))
    val allPlaylist = playlistDataProvider.getAll().observeAsState().optValue(listOf())

    Surface {
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    playlist.value = Playlist(0, "")
                    showAddPlaylistDialog.value = true
                }) {
                    Icon(
                        tint = Color.White,
                        imageVector = Icons.Filled.Add,
                        contentDescription = null
                    )
                }
            },
            floatingActionButtonPosition = FabPosition.End
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(matColors.secondaryVariant)
            ) {
                if (allPlaylist.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {

                        itemsIndexed(allPlaylist) { index, pl ->
                            PlaylistItem(
                                matColors,
                                pl,
                                allPlaylist.toMutableStateList(),
                                playlistDataProvider,
                                navController
                            ) {
                                playlist.value = it
                                showAddPlaylistDialog.value = true
                            }

                            if (index < allPlaylist.lastIndex) {
                                Divider(color = matColors.background)
                            }
                        }
                    }
                } else {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "No playlist found"
                        )
                    }
                }
            }
        }
    }

    // add or update playlist dialog
    AddOrUpdatePlaylistDialog(
        showAddPlaylistDialog = showAddPlaylistDialog,
        playlist = playlist.value,
        playlistDataProvider = playlistDataProvider
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewLightPlaylistView() {
    SufiIshqTheme(darkTheme = false) {
        PlaylistView(
            playlistDataProvider = dummyPlaylistDataProvider(),
            navController = rememberNavController()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDarkPlaylistView() {
    SufiIshqTheme(darkTheme = true) {
        PlaylistView(
            playlistDataProvider = dummyPlaylistDataProvider(),
            navController = rememberNavController()
        )
    }
}
