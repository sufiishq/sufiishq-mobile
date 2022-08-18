package pk.sufiishq.app.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import pk.sufiishq.app.R
import pk.sufiishq.app.core.event.dispatcher.EventDispatcher
import pk.sufiishq.app.core.event.events.PlaylistEvents
import pk.sufiishq.app.helpers.ScreenType
import pk.sufiishq.app.helpers.TrackListType
import pk.sufiishq.app.models.Playlist
import pk.sufiishq.app.ui.theme.SufiIshqTheme
import pk.sufiishq.app.utils.app
import pk.sufiishq.app.utils.dummyPlaylist
import pk.sufiishq.app.utils.rem

@Composable
fun PlaylistItem(
    playlist: Playlist,
    eventDispatcher: EventDispatcher,
    navController: NavController,
) {

    val matColors = MaterialTheme.colors
    val appConfig = app().appConfig
    val isExpanded = rem(false)

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

            LogoIcon(matColors = matColors)

            InfoRow(
                matColors = matColors,
                playlist = playlist,
                isExpanded = isExpanded,
                eventDispatcher = eventDispatcher
            )
        }
    }
}

@Composable
private fun InfoRow(
    matColors: Colors,
    playlist: Playlist,
    isExpanded: MutableState<Boolean>,
    eventDispatcher: EventDispatcher
) {
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

        PopupMenu(
            playlist = playlist,
            isExpanded = isExpanded,
            matColors = matColors,
            eventDispatcher = eventDispatcher
        )
    }
}

@Composable
private fun LogoIcon(
    matColors: Colors
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
}

@Composable
private fun PopupMenu(
    playlist: Playlist,
    isExpanded: MutableState<Boolean>,
    matColors: Colors,
    eventDispatcher: EventDispatcher
) {
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
                        "Rename" -> eventDispatcher.dispatch(
                            PlaylistEvents.ShowAddUpdatePlaylistDialog(
                                playlist
                            )
                        )
                        "Delete" -> eventDispatcher.dispatch(
                            PlaylistEvents.ShowConfirmDeletePlaylistDialog(
                                playlist
                            )
                        )
                    }
                    isExpanded.value = false
                }) {
                    PopupMenuLabel(label = label)
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
            playlist = dummyPlaylist(),
            eventDispatcher = EventDispatcher.getInstance(),
            navController = rememberNavController()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PlaylistItemPreviewDark() {
    SufiIshqTheme(darkTheme = true) {
        PlaylistItem(
            playlist = dummyPlaylist(),
            eventDispatcher = EventDispatcher.getInstance(),
            navController = rememberNavController()
        )
    }
}