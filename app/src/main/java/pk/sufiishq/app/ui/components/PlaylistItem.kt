package pk.sufiishq.app.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Colors
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import pk.sufiishq.app.R
import pk.sufiishq.app.annotations.ExcludeFromJacocoGeneratedReport
import pk.sufiishq.app.core.event.events.PlaylistEvents
import pk.sufiishq.app.helpers.ScreenType
import pk.sufiishq.app.models.Playlist
import pk.sufiishq.app.ui.theme.SufiIshqTheme
import pk.sufiishq.app.utils.dispatch
import pk.sufiishq.app.utils.dummyPlaylist
import pk.sufiishq.app.utils.rem

@Composable
fun PlaylistItem(
    playlist: Playlist,
    navController: NavController,
) {

    val matColors = MaterialTheme.colors
    val isExpanded = rem(false)

    Column(
        Modifier
            .testTag("playlist_item")
            .clickable {
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
                isExpanded = isExpanded
            )
        }
    }
}

@Composable
private fun InfoRow(
    matColors: Colors,
    playlist: Playlist,
    isExpanded: MutableState<Boolean>
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
            matColors = matColors
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
            .padding(start = 12.dp)
            .testTag("leading_icon"),
        colorFilter = ColorFilter.tint(matColors.primary),
        painter = painterResource(id = R.drawable.ic_outline_playlist_play_24),
        contentDescription = null
    )
}

@Composable
private fun PopupMenu(
    playlist: Playlist,
    isExpanded: MutableState<Boolean>,
    matColors: Colors
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
            modifier = Modifier.testTag("dropdown_menu"),
            expanded = isExpanded.value,
            onDismissRequest = { isExpanded.value = false }) {

            listOf("Rename", "Delete").forEach { label ->
                DropdownMenuItem(onClick = {
                    when (label) {
                        "Rename" -> PlaylistEvents.ShowAddUpdatePlaylistDialog(playlist).dispatch()
                        "Delete" -> PlaylistEvents.ShowConfirmDeletePlaylistDialog(playlist)
                            .dispatch()
                    }
                    isExpanded.value = false
                }) {
                    PopupMenuLabel(label = label)
                }
            }
        }
    }
}

@ExcludeFromJacocoGeneratedReport
@Preview(showBackground = true)
@Composable
fun PlaylistItemPreviewLight() {
    SufiIshqTheme(darkTheme = false) {
        PlaylistItem(
            playlist = dummyPlaylist(),
            navController = rememberNavController()
        )
    }
}

@ExcludeFromJacocoGeneratedReport
@Preview(showBackground = true)
@Composable
fun PlaylistItemPreviewDark() {
    SufiIshqTheme(darkTheme = true) {
        PlaylistItem(
            playlist = dummyPlaylist(),
            navController = rememberNavController()
        )
    }
}