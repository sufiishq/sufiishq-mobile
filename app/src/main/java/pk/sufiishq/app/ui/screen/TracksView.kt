package pk.sufiishq.app.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import pk.sufiishq.app.R
import pk.sufiishq.app.data.providers.KalamDataProvider
import pk.sufiishq.app.data.providers.PlayerDataProvider
import pk.sufiishq.app.data.providers.PlaylistDataProvider
import pk.sufiishq.app.helpers.Screen
import pk.sufiishq.app.models.Kalam
import pk.sufiishq.app.models.KalamItemParam
import pk.sufiishq.app.ui.components.KalamItem
import pk.sufiishq.app.ui.components.KalamRenameDialog
import pk.sufiishq.app.ui.components.SearchTextField
import pk.sufiishq.app.ui.components.SufiIshqDialog
import pk.sufiishq.app.ui.theme.SufiIshqTheme
import pk.sufiishq.app.utils.*

@Composable
fun TracksView(
    playerDataProvider: PlayerDataProvider,
    kalamDataProvider: KalamDataProvider,
    playlistDataProvider: PlaylistDataProvider,
    trackType: String,
    title: String,
    playlistId: Int
) {

    val context = LocalContext.current
    kalamDataProvider.init(trackType, playlistId)

    val lazyKalamItems: LazyPagingItems<Kalam> =
        kalamDataProvider.getKalamDataFlow().collectAsLazyPagingItems()
    val searchText = rem("")
    val matColors = MaterialTheme.colors
    val playlistItems = playlistDataProvider.getAll().observeAsState().optValue(listOf())
    val showKalamRenameDialog = rem(false)
    val selectedKalam = rem<Kalam?>(null)
    val kalamShareLoadingDialog = rem(false)

    val labelAddToPlaylist = stringResource(id = R.string.add_to_playlist)
    val labelMarkAsFavorite = stringResource(id = R.string.mark_as_favorite)
    val labelRemoveFavorite = stringResource(id = R.string.remove_favorite)
    val labelDownload = stringResource(id = R.string.download_label)
    val labelSplitKalam = stringResource(id = R.string.split_kalam)
    val labelDelete = stringResource(id = R.string.delete_label)
    val labelRename = stringResource(id = R.string.rename_label)
    val labelShare = stringResource(id = R.string.share_label)

    val kalamMenuItems = when (trackType) {
        Screen.Tracks.ALL -> listOf(
            labelAddToPlaylist,
            labelMarkAsFavorite,
            labelDownload,
            labelRename,
            labelShare,
            labelDelete
        )
        Screen.Tracks.DOWNLOADS -> listOf(
            labelAddToPlaylist,
            labelMarkAsFavorite,
            labelRename,
            labelShare,
            labelSplitKalam,
            labelDelete
        )
        Screen.Tracks.FAVORITES -> listOf(
            labelAddToPlaylist,
            labelRemoveFavorite,
            labelDownload,
            labelRename,
            labelShare
        )
        Screen.Tracks.PLAYLIST -> listOf(labelMarkAsFavorite, labelRename, labelShare, labelDelete)
        else -> listOf("")
    }

    Column(
        modifier = Modifier
            .background(matColors.secondaryVariant)
            .fillMaxSize()
    ) {
        SearchTextField(
            searchText,
            kalamDataProvider,
            matColors,
            lazyKalamItems,
            trackType,
            title,
            playlistId
        )
        Divider(color = matColors.secondary)

        if (lazyKalamItems.itemCount > 0) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = PaddingValues(8.dp, 8.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(lazyKalamItems) { track ->
                    track?.run {
                        KalamItem(
                            KalamItemParam(
                                kalam = track,
                                kalamMenuItems,
                                playerDataProvider,
                                kalamDataProvider,
                                playlistDataProvider,
                                lazyKalamItems,
                                playlistItems,
                                searchText,
                                trackType,
                                playlistId
                            )
                        ) { kalam, label ->
                            when (label) {
                                labelRename -> {
                                    selectedKalam.value = kalam
                                    showKalamRenameDialog.value = true
                                }
                                labelShare -> {
                                    kalamShareLoadingDialog.value = true
                                    kalam.share(context) {
                                        kalamShareLoadingDialog.value = false
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "No items found in $title"
                )
            }
        }
    }

    // kalam rename dialog
    selectedKalam.value?.let { kalam ->
        KalamRenameDialog(
            showKalamRenameDialog = showKalamRenameDialog,
            kalam = kalam,
        ) {
            kalam.title = it
            kalamDataProvider.update(kalam)
            kalamDataProvider.searchKalam("*", trackType, playlistId)
            lazyKalamItems.refresh()
            100.runWithDelay {
                kalamDataProvider.searchKalam(searchText.value, trackType, playlistId)
                lazyKalamItems.refresh()
            }
        }
    }

    if (kalamShareLoadingDialog.value) {
        SufiIshqDialog {
            CircularProgressIndicator()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TracksPreviewLight() {
    SufiIshqTheme(darkTheme = false) {
        TracksView(
            playerDataProvider = dummyPlayerDataProvider(),
            kalamDataProvider = dummyKalamDataProvider(),
            playlistDataProvider = dummyPlaylistDataProvider(),
            trackType = "all",
            "All",
            0
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TracksPreviewDark() {
    SufiIshqTheme(darkTheme = true) {
        TracksView(
            playerDataProvider = dummyPlayerDataProvider(),
            kalamDataProvider = dummyKalamDataProvider(),
            playlistDataProvider = dummyPlaylistDataProvider(),
            trackType = "all",
            "All",
            0
        )
    }
}