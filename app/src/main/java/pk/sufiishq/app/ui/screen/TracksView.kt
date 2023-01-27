package pk.sufiishq.app.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import pk.sufiishq.app.annotations.ExcludeFromJacocoGeneratedReport
import pk.sufiishq.app.core.event.events.KalamEvents
import pk.sufiishq.app.data.providers.KalamDataProvider
import pk.sufiishq.app.helpers.TrackListType
import pk.sufiishq.app.models.Kalam
import pk.sufiishq.app.ui.components.KalamItem
import pk.sufiishq.app.ui.components.SearchTextField
import pk.sufiishq.app.ui.components.dialogs.KalamConfirmDeleteDialog
import pk.sufiishq.app.ui.components.dialogs.KalamItemSplitDialog
import pk.sufiishq.app.ui.components.dialogs.KalamRenameDialog
import pk.sufiishq.app.ui.components.dialogs.ShowCircularProgressDialog
import pk.sufiishq.app.utils.dispatch
import pk.sufiishq.app.utils.dummyKalamDataProvider
import pk.sufiishq.app.utils.rem
import pk.sufiishq.app.viewmodels.KalamViewModel
import pk.sufiishq.aurora.layout.SIColumn
import pk.sufiishq.aurora.layout.SILazyColumn
import pk.sufiishq.aurora.theme.AuroraDark
import pk.sufiishq.aurora.theme.AuroraLight

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
    val lifecycleScope = rememberCoroutineScope()
    val searchText = rem("")

    SIColumn { textColor ->

        SearchTextField(
            searchText,
            textColor,
            lazyKalamItems,
            trackListType
        )

        SILazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(8.dp, 8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            hasItems = lazyKalamItems.itemCount > 0,
            noItemText = "No items found in ${trackListType.title}"
        ) {
            items(lazyKalamItems) { track ->
                track?.run {

                    KalamItem(
                        kalam = track,
                        trackListType = trackListType,
                        kalamDataProvider = kalamDataProvider,
                        coroutineScope = lifecycleScope
                    )
                }
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
    AuroraLight {
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
    AuroraDark {
        TracksView(
            kalamDataProvider = dummyKalamDataProvider(),
            trackListType = TrackListType.All()
        )
    }
}