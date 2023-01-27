package pk.sufiishq.app.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
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
import pk.sufiishq.app.annotations.ExcludeFromJacocoGeneratedReport
import pk.sufiishq.app.core.event.events.PlaylistEvents
import pk.sufiishq.app.data.providers.PlaylistDataProvider
import pk.sufiishq.app.models.Playlist
import pk.sufiishq.app.ui.components.PlaylistItem
import pk.sufiishq.app.ui.components.dialogs.AddOrUpdatePlaylistDialog
import pk.sufiishq.app.ui.components.dialogs.PlaylistConfirmDeleteDialog
import pk.sufiishq.app.utils.dispatch
import pk.sufiishq.app.utils.dummyPlaylistDataProvider
import pk.sufiishq.app.utils.isScrollingUp
import pk.sufiishq.app.utils.optValue
import pk.sufiishq.app.utils.rem
import pk.sufiishq.app.viewmodels.PlaylistViewModel
import pk.sufiishq.aurora.layout.SILazyColumn
import pk.sufiishq.aurora.layout.SIScaffold
import pk.sufiishq.aurora.theme.AuroraDark
import pk.sufiishq.aurora.theme.AuroraLight

@Composable
fun PlaylistView(
    navController: NavController,
    playlistDataProvider: PlaylistDataProvider = hiltViewModel<PlaylistViewModel>()
) {

    val playlist = rem(Playlist(0, ""))
    val allPlaylist = playlistDataProvider.getAll().observeAsState().optValue(listOf())
    val popupMenuItems = playlistDataProvider.getPopupMenuItems()

    val listState = rememberLazyListState()

    SIScaffold(
        onFloatingButtonAction = {
            playlist.value = Playlist(0, "")
            PlaylistEvents.ShowAddUpdatePlaylistDialog(playlist.value).dispatch()
        },
        isVisibleFAB = listState.isScrollingUp()
    ) {
        SILazyColumn(
            modifier = Modifier.fillMaxSize(),
            hasItems = allPlaylist.isNotEmpty(),
            contentPadding = PaddingValues(8.dp, 8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            state = listState,
            noItemText = "No playlist found"
        ) {

            items(allPlaylist) { pl ->
                PlaylistItem(
                    playlist = pl,
                    navController = navController,
                    popupMenuItems = popupMenuItems
                )
            }
        }
    }

    // add or update playlist dialog
    AddOrUpdatePlaylistDialog(
        playlistState = playlistDataProvider.getShowPlaylistAddUpdateDialog().observeAsState()
    )

    // playlist confirm delete dialog
    PlaylistConfirmDeleteDialog(
        playlistState = playlistDataProvider.getShowConfirmPlaylistDeleteDialog().observeAsState()
    )
}

@ExcludeFromJacocoGeneratedReport
@Preview(showBackground = true)
@Composable
fun PreviewLightPlaylistView() {
    AuroraLight {
        PlaylistView(
            navController = rememberNavController(),
            playlistDataProvider = dummyPlaylistDataProvider()
        )
    }
}

@ExcludeFromJacocoGeneratedReport
@Preview(showBackground = true)
@Composable
fun PreviewDarkPlaylistView() {
    AuroraDark {
        PlaylistView(
            navController = rememberNavController(),
            playlistDataProvider = dummyPlaylistDataProvider()
        )
    }
}
