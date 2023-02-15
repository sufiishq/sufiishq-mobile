package pk.sufiishq.app.ui.screen.tracks

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import io.github.esentsov.PackagePrivate
import pk.sufiishq.app.R
import pk.sufiishq.app.annotations.ExcludeFromJacocoGeneratedReport
import pk.sufiishq.app.data.controller.KalamController
import pk.sufiishq.app.helpers.TrackListType
import pk.sufiishq.app.models.Kalam
import pk.sufiishq.app.utils.fakeKalam
import pk.sufiishq.app.utils.fakeKalamController
import pk.sufiishq.app.utils.formatDateAs
import pk.sufiishq.app.utils.optString
import pk.sufiishq.app.utils.rem
import pk.sufiishq.aurora.theme.AuroraDark
import pk.sufiishq.aurora.theme.AuroraLight
import pk.sufiishq.aurora.widgets.SIDataRow

@PackagePrivate
@Composable
fun KalamItem(
    kalam: Kalam,
    trackListType: TrackListType,
    kalamController: KalamController
) {

    val isExpanded = rem(false)

    SIDataRow(
        leadingIcon = R.drawable.ic_start_logo,
        trailingIcon = R.drawable.ic_baseline_more_vert_24,
        onTrailingIconClick = { isExpanded.value = !isExpanded.value },
        trailingIconScope = {

            KalamItemPopupMenu(
                isExpanded = isExpanded,
                kalamController = kalamController,
                kalam = kalam,
                trackListType = trackListType
            )
        },
        onClick = {
            kalamController.changeTrack(kalam, trackListType)
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
            kalam = fakeKalam(),
            trackListType = TrackListType.All(title = optString(R.string.title_all_kalam)),
            kalamController = fakeKalamController()
        )
    }
}

@ExcludeFromJacocoGeneratedReport
@Preview
@Composable
fun KalamItemPreviewDark() {
    AuroraDark {
        KalamItem(
            kalam = fakeKalam(),
            trackListType = TrackListType.All(title = optString(R.string.title_all_kalam)),
            kalamController = fakeKalamController()
        )
    }
}