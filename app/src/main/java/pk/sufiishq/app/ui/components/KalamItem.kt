package pk.sufiishq.app.ui.components

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.flowOf
import org.apache.commons.io.FilenameUtils
import pk.sufiishq.app.R
import pk.sufiishq.app.data.providers.KalamDataProvider
import pk.sufiishq.app.data.providers.PlayerDataProvider
import pk.sufiishq.app.data.providers.PlaylistDataProvider
import pk.sufiishq.app.helpers.KalamSplitManager
import pk.sufiishq.app.helpers.Screen
import pk.sufiishq.app.models.Kalam
import pk.sufiishq.app.ui.theme.SufiIshqTheme
import pk.sufiishq.app.utils.*

@Composable
fun KalamItem(
    matColors: Colors,
    kalam: Kalam,
    kalamMenuItems: List<String>,
    playerDataProvider: PlayerDataProvider,
    kalamDataProvider: KalamDataProvider,
    playlistDataProvider: PlaylistDataProvider,
    lazyKalamItems: LazyPagingItems<Kalam>,
    searchText: MutableState<String>,
    trackType: String,
    playlistId: Int
) {

    val context = LocalContext.current

    val isExpanded = remember { mutableStateOf(false) }
    val showDownloadDialog = remember { mutableStateOf(false) }
    val showDeleteKalamConfirmDialog = remember { mutableStateOf(false) }
    val showPlaylistDialog = remember { mutableStateOf(false) }
    val showSplitterDialog = remember { mutableStateOf(false) }
    val downloadError = playerDataProvider.getDownloadError().observeAsState()

    val kalamSplitManager = remember { mutableStateOf(KalamSplitManager(context)) }

    Column(Modifier.clickable {
        playerDataProvider.changeTrack(kalam)
    }) {
        Row(
            modifier = Modifier
                .height(60.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(6.dp))
                .background(matColors.primaryVariant),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                modifier = Modifier
                    .width(40.dp)
                    .height(40.dp)
                    .padding(start = 6.dp),
                colorFilter = ColorFilter.tint(matColors.primary),
                painter = painterResource(id = R.drawable.ic_start_logo), contentDescription = null
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(start = 12.dp, top = 8.dp, end = 8.dp, bottom = 8.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // kalam title
                    Text(
                        color = matColors.primary,
                        fontSize = 18.sp,
                        text = kalam.title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    // kalam meta info
                    Text(
                        color = matColors.primary,
                        fontSize = 14.sp,
                        text = "${kalam.location} ${kalam.year}"
                    )
                }

                Box(modifier = Modifier.padding(end = 12.dp)) {
                    Image(
                        modifier = Modifier.clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { isExpanded.value = !isExpanded.value },
                        colorFilter = ColorFilter.tint(matColors.primary),
                        painter = painterResource(id = R.drawable.ic_baseline_more_vert_24),
                        contentDescription = null
                    )

                    DropdownMenu(
                        expanded = isExpanded.value,
                        onDismissRequest = { isExpanded.value = false }) {

                        val labelAddToPlaylist = stringResource(id = R.string.add_to_playlist)
                        val labelMarkAsFavorite = stringResource(id = R.string.mark_as_favorite)
                        val labelRemoveFavorite = stringResource(id = R.string.remove_favorite)
                        val labelDownload = stringResource(id = R.string.download)
                        val labelSplitKalam = stringResource(id = R.string.split_kalam)
                        val labelDelete = stringResource(id = R.string.delete)

                        kalamMenuItems
                            .filter {
                                when (it) {
                                    labelDownload -> kalam.offlineSource.isEmpty() ?: true
                                    labelMarkAsFavorite -> kalam.isFavorite == 0
                                    labelDelete -> {
                                        if (trackType == Screen.Tracks.ALL) {
                                            kalam.onlineSource.isEmpty()
                                        } else true
                                    }
                                    else -> true
                                }
                            }
                            .forEach { label ->
                                DropdownMenuItem(onClick = {
                                    when (label) {
                                        labelAddToPlaylist -> {
                                            showPlaylistDialog.value = true
                                        }
                                        labelMarkAsFavorite -> {
                                            context.toast("${kalam.title} added in favorite")
                                            kalam.isFavorite = 1
                                            kalamDataProvider.update(kalam)
                                        }
                                        labelRemoveFavorite -> {
                                            context.toast("${kalam.title} removed in favorite")
                                            kalam.isFavorite = 0
                                            kalamDataProvider.update(kalam)
                                            kalamDataProvider.searchKalam(
                                                searchText.value,
                                                trackType,
                                                playlistId
                                            )
                                            lazyKalamItems.refresh()
                                        }
                                        labelDownload -> {
                                            showDownloadDialog.value = true
                                            playerDataProvider.startDownload(kalam)
                                        }
                                        labelDelete -> {
                                            showDeleteKalamConfirmDialog.value = true
                                        }
                                        labelSplitKalam -> {
                                            kalamSplitManager.value =
                                                kalamSplitManager.value.newInstance(context)
                                            kalamSplitManager.value.setKalam(kalam)
                                            showSplitterDialog.value = true
                                        }
                                    }
                                    isExpanded.value = false
                                }) {
                                    Text(text = label)
                                }
                            }
                    }
                }
            }
        }
    }

    if (showDownloadDialog.value) {
        KalamDownloadDialog(
            kalam = kalam,
            playerDataProvider = playerDataProvider,
            kalamDataProvider = kalamDataProvider,
            onCancel = {
                playerDataProvider.disposeDownload()
                showDownloadDialog.value = false
            }
        ) {
            showDownloadDialog.value = false
        }
    }

    downloadError.value?.let { error ->
        if (error.isNotEmpty()) {
            playerDataProvider.disposeDownload()
            showDownloadDialog.value = false
            AlertDialog(
                onDismissRequest = {
                    playerDataProvider.setDownloadError("")
                },
                title = {
                    Text(text = "Download Error", fontWeight = FontWeight.Bold)
                },
                text = {
                    Text(error)
                },
                confirmButton = {

                },
                dismissButton = {
                    Button(
                        onClick = {
                            playerDataProvider.setDownloadError("")
                        }
                    ) {
                        Text("OK")
                    }
                }
            )
        }
    }

    if (showDeleteKalamConfirmDialog.value) {
        Dialog(
            onDismissRequest = {
                showDeleteKalamConfirmDialog.value = false
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
                                append(kalam.title)
                            }
                        }
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = {
                            showDeleteKalamConfirmDialog.value = false
                        }) {
                            Text(text = "NO")
                        }
                        TextButton(onClick = {
                            showDeleteKalamConfirmDialog.value = false
                            kalamDataProvider.delete(kalam, trackType)
                            kalamDataProvider.searchKalam(searchText.value, trackType, playlistId)
                            lazyKalamItems.refresh()
                        }) {
                            Text(text = "YES")
                        }
                    }
                }
            }
        }
    }

    val playlistItems = playlistDataProvider.getAll().observeAsState().optValue(listOf())
    if (showPlaylistDialog.value) {

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

    if (showSplitterDialog.value) {

        SufiIshqDialog {
            KalamSplitter(
                showDialog = showSplitterDialog,
                kalamSplitManager = kalamSplitManager.value,
                kalamDataProvider = kalamDataProvider,
                searchValue = searchText.value,
                trackType = trackType,
                playlistId = playlistId,
                lazyKalamItems = lazyKalamItems
            )
        }
    }
}

@Composable
fun KalamDownloadDialog(
    kalam: Kalam,
    playerDataProvider: PlayerDataProvider,
    kalamDataProvider: KalamDataProvider,
    onCancel: () -> Unit,
    onDismiss: () -> Unit
) {

    val progress = playerDataProvider.getDownloadProgress().observeAsState().value ?: 0f

    Dialog(
        onDismissRequest = {
            if (progress >= 100) onDismiss()
        },
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colors.surface,
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                if (progress < 100) {
                    Text(text = kalam.title)
                }

                if (progress > 0 && progress < 100) {
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        progress = playerDataProvider.getDownloadProgress().observeAsState().value
                            ?: 0f,
                    )
                } else if (progress >= 100) {
                    kalam.offlineSource = "$KALAM_DIR/${FilenameUtils.getName(kalam.onlineSource)}"
                    kalamDataProvider.update(kalam)

                    Text(
                        buildAnnotatedString {
                            append("Kalam ")
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append(kalam.title)
                            }
                            append(" successfully downloaded")
                        }
                    )
                } else {
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    )
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {

                    if (progress < 100) {
                        TextButton(onClick = onCancel) {
                            Text(text = "Cancel")
                        }
                    } else {
                        TextButton(onClick = onDismiss) {
                            Text(text = "OK")
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun KalamItemPreviewLight() {
    SufiIshqTheme(darkTheme = false) {
        KalamItem(
            matColors = MaterialTheme.colors,
            kalam = dummyTrack(),
            listOf(""),
            dummyPlayerDataProvider(),
            dummyKalamDataProvider(),
            dummyPlaylistDataProvider(),
            flowOf<PagingData<Kalam>>().collectAsLazyPagingItems(),
            remember { mutableStateOf("") },
            Screen.Tracks.ALL,
            0
        )
    }
}

@Preview(showBackground = true)
@Composable
fun KalamItemPreviewDark() {
    SufiIshqTheme(darkTheme = true) {
        KalamItem(
            matColors = MaterialTheme.colors,
            kalam = dummyTrack(),
            listOf(""),
            dummyPlayerDataProvider(),
            dummyKalamDataProvider(),
            dummyPlaylistDataProvider(),
            flowOf<PagingData<Kalam>>().collectAsLazyPagingItems(),
            remember { mutableStateOf("") },
            Screen.Tracks.ALL,
            0
        )
    }
}