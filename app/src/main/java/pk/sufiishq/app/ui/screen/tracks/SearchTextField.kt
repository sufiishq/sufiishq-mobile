package pk.sufiishq.app.ui.screen.tracks

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import io.github.esentsov.PackagePrivate
import pk.sufiishq.app.R
import pk.sufiishq.app.annotations.ExcludeFromJacocoGeneratedReport
import pk.sufiishq.app.data.providers.KalamDataProvider
import pk.sufiishq.app.helpers.TrackListType
import pk.sufiishq.app.models.Kalam
import pk.sufiishq.app.utils.fakeKalamDataProvider
import pk.sufiishq.app.utils.optString
import pk.sufiishq.aurora.components.SITextField
import pk.sufiishq.aurora.theme.AuroraColor
import pk.sufiishq.aurora.theme.AuroraDark
import pk.sufiishq.aurora.theme.AuroraLight

@PackagePrivate
@Composable
fun SearchTextField(
    searchText: MutableState<String>,
    textColor: AuroraColor,
    lazyKalamItems: LazyPagingItems<Kalam>,
    trackListType: TrackListType,
    kalamDataProvider: KalamDataProvider
) {

    SITextField(
        searchText = searchText,
        textColor = textColor,
        placeholderText = trackListType.title,
        onValueChange = {
            searchText.value = it
            kalamDataProvider.searchKalam(it, trackListType)
            lazyKalamItems.refresh()
        }
    )

}

@ExcludeFromJacocoGeneratedReport
@Preview(showBackground = true)
@Composable
fun SearchTextFieldPreviewLight() {
    AuroraLight {
        val data = fakeKalamDataProvider()
        SearchTextField(
            remember { mutableStateOf("") },
            AuroraColor.OnBackground,
            data.getKalamDataFlow().collectAsLazyPagingItems(),
            TrackListType.All(optString(R.string.title_all_kalam)),
            fakeKalamDataProvider()
        )
    }
}

@ExcludeFromJacocoGeneratedReport
@Preview(showBackground = true)
@Composable
fun SearchTextFieldPreviewDark() {
    AuroraDark {
        val data = fakeKalamDataProvider()
        SearchTextField(
            remember { mutableStateOf("") },
            AuroraColor.OnBackground,
            data.getKalamDataFlow().collectAsLazyPagingItems(),
            TrackListType.All(optString(R.string.title_all_kalam)),
            fakeKalamDataProvider()
        )
    }
}