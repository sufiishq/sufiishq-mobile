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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pk.sufiishq.app.R
import pk.sufiishq.app.annotations.ExcludeFromJacocoGeneratedReport
import pk.sufiishq.app.core.event.events.PlayerEvents
import pk.sufiishq.app.helpers.TrackListType
import pk.sufiishq.app.models.Kalam
import pk.sufiishq.app.ui.theme.SufiIshqTheme
import pk.sufiishq.app.utils.dispatch
import pk.sufiishq.app.utils.dummyKalam
import pk.sufiishq.app.utils.formatDateAs
import pk.sufiishq.app.utils.rem

@Composable
fun KalamItem(
    kalam: Kalam,
    trackListType: TrackListType,
    kalamMenuItems: List<String>
) {

    val matColors = MaterialTheme.colors
    val isExpanded = rem(false)

    Column(
        modifier = Modifier
            .testTag("kalam_item")
            .clickable {
                PlayerEvents
                    .ChangeTrack(kalam, trackListType)
                    .dispatch()
            }
    ) {
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
                    .padding(start = 6.dp)
                    .testTag("leading_icon"),
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
                        text = "${kalam.location} ${kalam.recordeDate.formatDateAs(prefix = "- ")}"
                    )
                }

                Box(modifier = Modifier.padding(end = 12.dp)) {
                    Image(
                        modifier = Modifier
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                isExpanded.value = !isExpanded.value
                            }
                            .testTag("overflow_menu_button"),
                        colorFilter = ColorFilter.tint(matColors.primary),
                        painter = painterResource(id = R.drawable.ic_baseline_more_vert_24),
                        contentDescription = null
                    )

                    KalamItemPopupMenu(
                        isExpanded = isExpanded,
                        kalamMenuItems = kalamMenuItems,
                        kalam = kalam,
                        trackListType = trackListType
                    )
                }
            }
        }
    }
}

@ExcludeFromJacocoGeneratedReport
@Preview
@Composable
fun KalamItemPreviewLight() {
    SufiIshqTheme(darkTheme = false) {
        KalamItem(
            kalam = dummyKalam(),
            trackListType = TrackListType.All(),
            kalamMenuItems = listOf()
        )
    }
}

@ExcludeFromJacocoGeneratedReport
@Preview
@Composable
fun KalamItemPreviewDark() {
    SufiIshqTheme(darkTheme = true) {
        KalamItem(
            kalam = dummyKalam(),
            trackListType = TrackListType.All(),
            kalamMenuItems = listOf()
        )
    }
}