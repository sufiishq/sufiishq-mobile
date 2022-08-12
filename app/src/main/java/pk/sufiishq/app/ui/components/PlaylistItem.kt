package pk.sufiishq.app.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import pk.sufiishq.app.R
import pk.sufiishq.app.data.providers.PlaylistDataProvider
import pk.sufiishq.app.helpers.ScreenType
import pk.sufiishq.app.helpers.TrackListType
import pk.sufiishq.app.models.Playlist
import pk.sufiishq.app.ui.theme.SufiIshqTheme
import pk.sufiishq.app.utils.app
import pk.sufiishq.app.utils.dummyPlaylist
import pk.sufiishq.app.utils.dummyPlaylistDataProvider

@Composable
fun PlaylistItem(
    matColors: Colors,
    playlist: Playlist,
    allPlaylist: SnapshotStateList<Playlist>,
    playlistDataProvider: PlaylistDataProvider,
    navController: NavController,
    onPlaylistRename: (playlist: Playlist) -> Unit
) {

    val appConfig = app.appConfig
    val isExpanded = remember { mutableStateOf(false) }
    val showPlaylistDeleteConfirmDialog = remember { mutableStateOf(false) }

    Column(Modifier.clickable {
        appConfig.trackListType = TrackListType.Playlist(playlist.title, playlist.id)
        navController.navigate(
            ScreenType.Tracks.withArgs(
                "playlist",
                "${playlist.title} Playlist",
                "${playlist.id}"
            )
        )
    }) {
        Row(
            modifier = Modifier
                .height(60.dp)
                .fillMaxWidth()
                .background(matColors.primaryVariant),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                modifier = Modifier
                    .width(40.dp)
                    .height(40.dp)
                    .padding(start = 12.dp),
                colorFilter = ColorFilter.tint(matColors.primary),
                painter = painterResource(id = R.drawable.ic_outline_playlist_play_24),
                contentDescription = null
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier
                        .width(280.dp)
                        .padding(start = 12.dp, top = 8.dp, end = 8.dp, bottom = 8.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {

                    // playlist title
                    Text(
                        color = matColors.primary,
                        fontSize = 18.sp,
                        text = playlist.title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    // playlist total kalam count
                    Text(
                        color = matColors.primary,
                        fontSize = 14.sp,
                        text = "Total ${playlist.totalKalam} kalams"
                    )
                }

                Box(modifier = Modifier.padding(end = 12.dp)) {
                    Image(
                        modifier = Modifier.clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {

                            isExpanded.value = !isExpanded.value
                        },
                        colorFilter = ColorFilter.tint(matColors.primary),
                        painter = painterResource(id = R.drawable.ic_baseline_more_vert_24),
                        contentDescription = null
                    )

                    DropdownMenu(
                        expanded = isExpanded.value,
                        onDismissRequest = { isExpanded.value = false }) {

                        listOf("Rename", "Delete").forEach { label ->
                            DropdownMenuItem(onClick = {
                                when (label) {
                                    "Rename" -> onPlaylistRename(playlist)
                                    "Delete" -> showPlaylistDeleteConfirmDialog.value = true
                                }
                                isExpanded.value = false
                            }) {
                                PopupMenuLabel(label = label)
                            }
                        }
                    }
                }
            }
        }
    }

    if (showPlaylistDeleteConfirmDialog.value) {
        Dialog(
            onDismissRequest = {
                showPlaylistDeleteConfirmDialog.value = false
            },
        ) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colors.surface,
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    Text(
                        buildAnnotatedString {
                            append("Are you sure you want to delete ")
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append(playlist.title)
                            }
                            append(" playlist?")
                        }
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = {
                            showPlaylistDeleteConfirmDialog.value = false
                        }) {
                            Text(text = "NO")
                        }
                        TextButton(onClick = {
                            playlistDataProvider.delete(playlist)
                            showPlaylistDeleteConfirmDialog.value = false
                            allPlaylist.remove(playlist)

                        }) {
                            Text(text = "YES")
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PlaylistItemPreviewLight() {
    SufiIshqTheme(darkTheme = false) {
        PlaylistItem(
            matColors = MaterialTheme.colors,
            playlist = dummyPlaylist(),
            listOf<Playlist>().toMutableStateList(),
            playlistDataProvider = dummyPlaylistDataProvider(),
            navController = rememberNavController(),
            onPlaylistRename = {})
    }
}

@Preview(showBackground = true)
@Composable
fun PlaylistItemPreviewDark() {
    SufiIshqTheme(darkTheme = true) {
        PlaylistItem(
            matColors = MaterialTheme.colors,
            playlist = dummyPlaylist(),
            listOf<Playlist>().toMutableStateList(),
            playlistDataProvider = dummyPlaylistDataProvider(),
            navController = rememberNavController(),
            onPlaylistRename = {})
    }
}