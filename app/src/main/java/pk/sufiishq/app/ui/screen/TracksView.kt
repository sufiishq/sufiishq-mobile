package pk.sufiishq.app.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import pk.sufiishq.app.R
import pk.sufiishq.app.annotations.ExcludeFromJacocoGeneratedReport
import pk.sufiishq.app.core.event.events.KalamEvents
import pk.sufiishq.app.data.providers.KalamDataProvider
import pk.sufiishq.app.helpers.ScreenType
import pk.sufiishq.app.helpers.TrackListType
import pk.sufiishq.app.models.Kalam
import pk.sufiishq.app.ui.components.KalamItem
import pk.sufiishq.app.ui.components.SearchTextField
import pk.sufiishq.app.ui.components.dialogs.KalamConfirmDeleteDialog
import pk.sufiishq.app.ui.components.dialogs.KalamItemSplitDialog
import pk.sufiishq.app.ui.components.dialogs.KalamRenameDialog
import pk.sufiishq.app.ui.components.dialogs.ShowCircularProgressDialog
import pk.sufiishq.app.ui.theme.SufiIshqTheme
import pk.sufiishq.app.utils.dispatch
import pk.sufiishq.app.utils.dummyKalamDataProvider
import pk.sufiishq.app.utils.rem
import pk.sufiishq.app.viewmodels.KalamViewModel

@Composable
fun TracksView(
    kalamDataProvider: KalamDataProvider = hiltViewModel<KalamViewModel>(),
    trackListType: TrackListType
) {

    // skip dispatch event in preview mode
    if (!LocalInspectionMode.current) {
        KalamEvents.SearchKalam("", trackListType).dispatch()
    }

    val lazyKalamItems: LazyPagingItems<Kalam> =
        kalamDataProvider.getKalamDataFlow().collectAsLazyPagingItems()
    val searchText = rem("")
    val matColors = MaterialTheme.colors

    val labelAddToPlaylist = stringResource(id = R.string.add_to_playlist)
    val labelMarkAsFavorite = stringResource(id = R.string.mark_as_favorite)
    val labelRemoveFavorite = stringResource(id = R.string.remove_favorite)
    val labelDownload = stringResource(id = R.string.download_label)
    val labelSplitKalam = stringResource(id = R.string.split_kalam)
    val labelDelete = stringResource(id = R.string.delete_label)
    val labelRename = stringResource(id = R.string.rename_label)
    val labelShare = stringResource(id = R.string.share_label)

    val kalamMenuItems = when (trackListType.type) {
        ScreenType.Tracks.ALL -> listOf(
            labelAddToPlaylist,
            labelMarkAsFavorite,
            labelDownload,
            labelRename,
            labelShare,
            labelDelete
        )
        ScreenType.Tracks.DOWNLOADS -> listOf(
            labelAddToPlaylist,
            labelMarkAsFavorite,
            labelRename,
            labelShare,
            labelSplitKalam,
            labelDelete
        )
        ScreenType.Tracks.FAVORITES -> listOf(
            labelAddToPlaylist,
            labelRemoveFavorite,
            labelDownload,
            labelRename,
            labelShare
        )
        ScreenType.Tracks.PLAYLIST -> listOf(
            labelMarkAsFavorite,
            labelRename,
            labelShare,
            labelDelete
        )
        else -> listOf("")
    }

    Column(
        modifier = Modifier
            .background(matColors.secondaryVariant)
            .fillMaxSize()
    ) {
        SearchTextField(
            searchText,
            matColors,
            lazyKalamItems,
            trackListType
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
                            kalam = track,
                            trackListType = trackListType,
                            kalamMenuItems = kalamMenuItems
                        )
                    }
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "No items found in ${trackListType.title}"
                )
            }
        }
    }

    // kalam confirm delete dialog
    KalamConfirmDeleteDialog(
        kalamDeleteItem = kalamDataProvider.getKalamDeleteConfirmDialog().observeAsState()
    )

    // kalam split dialog
    KalamItemSplitDialog(
        kalamSplitManager = kalamDataProvider.getKalamSplitManagerDialog().observeAsState()
    )

    // kalam rename dialog
    KalamRenameDialog(
        kalamState = kalamDataProvider.getKalamRenameDialog().observeAsState(),
    )

    // show circular progress indicator dialog for shapre kalam
    ShowCircularProgressDialog(
        showDialog = kalamDataProvider.getShowCircularProgressDialog().observeAsState()
    )
}

@ExcludeFromJacocoGeneratedReport
@Preview(showBackground = true)
@Composable
fun TracksPreviewLight() {
    SufiIshqTheme(darkTheme = false) {
        TracksView(
            kalamDataProvider = dummyKalamDataProvider(),
            trackListType = TrackListType.All()
        )
    }
}

@ExcludeFromJacocoGeneratedReport
@Preview(showBackground = true)
@Composable
fun TracksPreviewDark() {
    SufiIshqTheme(darkTheme = true) {
        TracksView(
            kalamDataProvider = dummyKalamDataProvider(),
            trackListType = TrackListType.All()
        )
    }
}