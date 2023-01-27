package pk.sufiishq.app.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.CoroutineScope
import pk.sufiishq.app.R
import pk.sufiishq.app.annotations.ExcludeFromJacocoGeneratedReport
import pk.sufiishq.app.core.event.events.PlayerEvents
import pk.sufiishq.app.data.providers.KalamDataProvider
import pk.sufiishq.app.helpers.TrackListType
import pk.sufiishq.app.models.Kalam
import pk.sufiishq.app.utils.dispatch
import pk.sufiishq.app.utils.dummyKalam
import pk.sufiishq.app.utils.dummyKalamDataProvider
import pk.sufiishq.app.utils.formatDateAs
import pk.sufiishq.app.utils.rem
import pk.sufiishq.aurora.theme.AuroraDark
import pk.sufiishq.aurora.theme.AuroraLight
import pk.sufiishq.aurora.widgets.SIDataRow

@Composable
fun KalamItem(
    kalam: Kalam,
    trackListType: TrackListType,
    kalamDataProvider: KalamDataProvider,
    coroutineScope: CoroutineScope
) {

    val isExpanded = rem(false)

    SIDataRow(
        leadingIcon = R.drawable.ic_start_logo,
        trailingIcon = R.drawable.ic_baseline_more_vert_24,
        onTrailingIconClick = { isExpanded.value = !isExpanded.value },
        trailingIconScope = {

            KalamItemPopupMenu(
                isExpanded = isExpanded,
                kalamDataProvider = kalamDataProvider,
                kalam = kalam,
                trackListType = trackListType,
                coroutineScope = coroutineScope
            )
        },
        onClick = {
            PlayerEvents.ChangeTrack(kalam, trackListType).dispatch()
        },
        title = kalam.title,
        subTitle = "${kalam.location} ${kalam.recordeDate.formatDateAs(prefix = "- ")}"
    )
}

@ExcludeFromJacocoGeneratedReport
@Preview
@Composable
fun KalamItemPreviewLight() {
    AuroraLight {
        KalamItem(
            kalam = dummyKalam(),
            trackListType = TrackListType.All(),
            kalamDataProvider = dummyKalamDataProvider(),
            coroutineScope = rememberCoroutineScope()
        )
    }
}

@ExcludeFromJacocoGeneratedReport
@Preview
@Composable
fun KalamItemPreviewDark() {
    AuroraDark {
        KalamItem(
            kalam = dummyKalam(),
            trackListType = TrackListType.All(),
            kalamDataProvider = dummyKalamDataProvider(),
            coroutineScope = rememberCoroutineScope()
        )
    }
}