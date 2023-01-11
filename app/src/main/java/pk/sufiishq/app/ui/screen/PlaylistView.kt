package pk.sufiishq.app.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import pk.sufiishq.app.annotations.ExcludeFromJacocoGeneratedReport
import pk.sufiishq.app.core.event.dispatcher.EventDispatcher
import pk.sufiishq.app.core.event.events.PlaylistEvents
import pk.sufiishq.app.data.providers.PlaylistDataProvider
import pk.sufiishq.app.models.Playlist
import pk.sufiishq.app.ui.components.PlaylistItem
import pk.sufiishq.app.ui.theme.SufiIshqTheme
import pk.sufiishq.app.utils.dummyPlaylistDataProvider
import pk.sufiishq.app.utils.optValue
import pk.sufiishq.app.utils.rem

@Composable
fun PlaylistView(
    playlistDataProvider: PlaylistDataProvider,
    navController: NavController
) {

    val eventDispatcher = EventDispatcher.getInstance()
    val matColors = MaterialTheme.colors
    val playlist = rem(Playlist(0, ""))
    val allPlaylist = playlistDataProvider.getAll().observeAsState().optValue(listOf())

    Surface {
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    playlist.value = Playlist(0, "")
                    eventDispatcher.dispatch(PlaylistEvents.ShowAddUpdatePlaylistDialog(playlist.value))
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
                                playlist = pl,
                                navController = navController
                            )

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
}

@ExcludeFromJacocoGeneratedReport
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

@ExcludeFromJacocoGeneratedReport
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
