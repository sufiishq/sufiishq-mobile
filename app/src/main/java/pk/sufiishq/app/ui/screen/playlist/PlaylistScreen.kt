package pk.sufiishq.app.ui.screen.playlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import pk.sufiishq.app.R
import pk.sufiishq.app.annotations.ExcludeFromJacocoGeneratedReport
import pk.sufiishq.app.data.providers.PlaylistDataProvider
import pk.sufiishq.app.models.Playlist
import pk.sufiishq.app.utils.fakePlaylistDataProvider
import pk.sufiishq.app.utils.isScrollingUp
import pk.sufiishq.app.utils.optString
import pk.sufiishq.app.utils.optValue
import pk.sufiishq.app.utils.rem
import pk.sufiishq.app.viewmodels.PlaylistViewModel
import pk.sufiishq.aurora.layout.SILazyColumn
import pk.sufiishq.aurora.layout.SIScaffold
import pk.sufiishq.aurora.theme.AuroraDark
import pk.sufiishq.aurora.theme.AuroraLight

@Composable
fun PlaylistScreen(
    navController: NavController,
    playlistDataProvider: PlaylistDataProvider = hiltViewModel<PlaylistViewModel>()
) {

    val showAddUpdatePlaylistDialog = rem<Playlist?>(null)
    val showDeletePlaylistDialog = rem<Playlist?>(null)
    val playlist = rem(Playlist(0, ""))
    val allPlaylist = playlistDataProvider.getAll().observeAsState().optValue(listOf())
    val popupMenuItems = playlistDataProvider.getPopupMenuItems()

    val listState = rememberLazyListState()

    SIScaffold(
        onFloatingButtonAction = {
            showAddUpdatePlaylistDialog.value = playlist.value
        },
        isVisibleFAB = listState.isScrollingUp()
    ) {
        SILazyColumn(
            modifier = Modifier.fillMaxSize(),
            hasItems = allPlaylist.isNotEmpty(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            state = listState,
            noItemText = optString(R.string.msg_no_playlist)
        ) {

            items(allPlaylist) { playlistItem ->
                PlaylistItem(
                    showAddUpdatePlaylistDialog = showAddUpdatePlaylistDialog,
                    showDeletePlaylistDialog = showDeletePlaylistDialog,
                    playlist = playlistItem,
                    navController = navController,
                    popupMenuItems = popupMenuItems
                )
            }
        }
    }

    // add or update playlist dialog
    AddOrUpdatePlaylistDialog(
        showAddUpdatePlaylistDialog = showAddUpdatePlaylistDialog,
        playlistDataProvider = playlistDataProvider
    )

    // playlist confirm delete dialog
    PlaylistConfirmDeleteDialog(
        showDeletePlaylistDialog = showDeletePlaylistDialog,
        playlistDataProvider = playlistDataProvider
    )
}

@ExcludeFromJacocoGeneratedReport
@Preview(showBackground = true)
@Composable
fun PreviewLightPlaylistView() {
    AuroraLight {
        PlaylistScreen(
            navController = rememberNavController(),
            playlistDataProvider = fakePlaylistDataProvider()
        )
    }
}

@ExcludeFromJacocoGeneratedReport
@Preview(showBackground = true)
@Composable
fun PreviewDarkPlaylistView() {
    AuroraDark {
        PlaylistScreen(
            navController = rememberNavController(),
            playlistDataProvider = fakePlaylistDataProvider()
        )
    }
}
