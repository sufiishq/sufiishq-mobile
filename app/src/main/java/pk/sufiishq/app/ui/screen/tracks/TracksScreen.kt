package pk.sufiishq.app.ui.screen.tracks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import pk.sufiishq.app.R
import pk.sufiishq.app.annotations.ExcludeFromJacocoGeneratedReport
import pk.sufiishq.app.data.providers.KalamDataProvider
import pk.sufiishq.app.helpers.TrackListType
import pk.sufiishq.app.models.Kalam
import pk.sufiishq.app.utils.fakeKalamDataProvider
import pk.sufiishq.app.utils.optString
import pk.sufiishq.app.viewmodels.KalamViewModel
import pk.sufiishq.aurora.layout.SIColumn
import pk.sufiishq.aurora.layout.SILazyColumn
import pk.sufiishq.aurora.theme.AuroraDark
import pk.sufiishq.aurora.theme.AuroraLight

@Composable
fun TracksScreen(
    kalamDataProvider: KalamDataProvider = hiltViewModel<KalamViewModel>(),
    trackListType: TrackListType
) {

    kalamDataProvider.searchKalam("", trackListType)

    val lazyKalamItems: LazyPagingItems<Kalam> =
        kalamDataProvider.getKalamDataFlow().collectAsLazyPagingItems()

    SIColumn { textColor ->

        SearchTextField(
            textColor, lazyKalamItems, trackListType, kalamDataProvider
        )

        SILazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            hasItems = lazyKalamItems.itemCount > 0,
            noItemText = optString(R.string.dynamic_no_kalam_found, trackListType.title)
        ) {
            items(lazyKalamItems) { track ->
                track?.run {

                    KalamItem(
                        kalam = track,
                        trackListType = trackListType,
                        kalamDataProvider = kalamDataProvider
                    )
                }
            }
        }
    }

    // kalam confirm delete dialog
    KalamConfirmDeleteDialog(
        kalamDataProvider = kalamDataProvider
    )

    // kalam split dialog
    KalamSplitDialog(
        kalamDataProvider = kalamDataProvider
    )

    // kalam download dialog
    KalamDownloadDialog(
        kalamDataProvider = kalamDataProvider
    )

    // playlist dialog
    PlaylistDialog(
        kalamDataProvider = kalamDataProvider,
    )
}

@ExcludeFromJacocoGeneratedReport
@Preview(showBackground = true)
@Composable
fun TracksPreviewLight() {
    AuroraLight {
        TracksScreen(
            kalamDataProvider = fakeKalamDataProvider(),
            trackListType = TrackListType.All(optString(R.string.title_all_kalam))
        )
    }
}

@ExcludeFromJacocoGeneratedReport
@Preview(showBackground = true)
@Composable
fun TracksPreviewDark() {
    AuroraDark {
        TracksScreen(
            kalamDataProvider = fakeKalamDataProvider(),
            trackListType = TrackListType.All(optString(R.string.title_all_kalam))
        )
    }
}

