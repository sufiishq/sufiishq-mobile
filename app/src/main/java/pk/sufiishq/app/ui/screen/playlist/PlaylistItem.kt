package pk.sufiishq.app.ui.screen.playlist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import io.github.esentsov.PackagePrivate
import pk.sufiishq.app.R
import pk.sufiishq.app.annotations.ExcludeFromJacocoGeneratedReport
import pk.sufiishq.app.helpers.ScreenType
import pk.sufiishq.app.helpers.popupmenu.PopupMenuItem
import pk.sufiishq.app.models.Playlist
import pk.sufiishq.app.utils.fakePlaylist
import pk.sufiishq.app.utils.rem
import pk.sufiishq.aurora.models.DataMenuItem
import pk.sufiishq.aurora.theme.AuroraDark
import pk.sufiishq.aurora.theme.AuroraLight
import pk.sufiishq.aurora.widgets.SIDataRow
import pk.sufiishq.aurora.widgets.SIPopupMenu

@PackagePrivate
@Composable
fun PlaylistItem(
    showAddUpdatePlaylistDialog: MutableState<Playlist?>,
    showDeletePlaylistDialog: MutableState<Playlist?>,
    playlist: Playlist,
    navController: NavController,
    popupMenuItems: List<DataMenuItem>
) {

    val isExpanded = rem(false)

    SIDataRow(
        leadingIcon = R.drawable.ic_outline_playlist_play_24,
        trailingIcon = R.drawable.ic_baseline_more_vert_24,
        onTrailingIconClick = { isExpanded.value = !isExpanded.value },
        trailingIconScope = {
            SIPopupMenu(
                isExpanded = isExpanded,
                data = popupMenuItems,
                onClick = {
                    handleClick(it, playlist, showAddUpdatePlaylistDialog, showDeletePlaylistDialog)
                }
            )
        },
        onClick = {
            navController.navigate(
                ScreenType.Tracks.buildRoute(
                    "playlist",
                    "${playlist.title} Playlist",
                    "${playlist.id}"
                )
            )
        },
        title = playlist.title,
        subTitle = "Total ${playlist.totalKalam} kalams"
    )
}

private fun handleClick(
    popupMenuItem: DataMenuItem,
    playlist: Playlist,
    showAddUpdatePlaylistDialog: MutableState<Playlist?>,
    showDeletePlaylistDialog: MutableState<Playlist?>
) {
    when (popupMenuItem) {
        is PopupMenuItem.Edit -> {
            showAddUpdatePlaylistDialog.value = playlist
        }
        is PopupMenuItem.Delete -> {
            showDeletePlaylistDialog.value = playlist
        }
    }
}

@ExcludeFromJacocoGeneratedReport
@Preview(showBackground = true)
@Composable
fun PlaylistItemPreviewLight() {
    AuroraLight {
        PlaylistItem(
            showAddUpdatePlaylistDialog = rem(null),
            showDeletePlaylistDialog = rem(null),
            playlist = fakePlaylist(),
            navController = rememberNavController(),
            popupMenuItems = listOf()
        )
    }
}

@ExcludeFromJacocoGeneratedReport
@Preview(showBackground = true)
@Composable
fun PlaylistItemPreviewDark() {
    AuroraDark {
        PlaylistItem(
            showAddUpdatePlaylistDialog = rem(null),
            showDeletePlaylistDialog = rem(null),
            playlist = fakePlaylist(),
            navController = rememberNavController(),
            popupMenuItems = listOf()
        )
    }
}