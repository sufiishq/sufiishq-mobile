package pk.sufiishq.app.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pk.sufiishq.app.R
import pk.sufiishq.app.core.event.dispatcher.EventDispatcher
import pk.sufiishq.app.core.event.events.PlayerEvents
import pk.sufiishq.app.data.providers.KalamDataProvider
import pk.sufiishq.app.helpers.TrackListType
import pk.sufiishq.app.models.Kalam
import pk.sufiishq.app.ui.components.dialogs.KalamRenameDialog
import pk.sufiishq.app.ui.theme.SufiIshqTheme
import pk.sufiishq.app.utils.dummyKalam
import pk.sufiishq.app.utils.dummyKalamDataProvider
import pk.sufiishq.app.utils.formatDateAs
import pk.sufiishq.app.utils.rem

@Composable
fun KalamItem(
    kalam: Kalam,
    trackListType: TrackListType,
    kalamMenuItems: List<String>,
    eventDispatcher: EventDispatcher
) {

    val matColors = MaterialTheme.colors
    val isExpanded = rem(false)

    Column(Modifier.clickable {
        eventDispatcher.dispatch(PlayerEvents.ChangeTrack(kalam, trackListType))
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
                        text = "${kalam.location} ${kalam.recordeDate.formatDateAs(prefix = "- ")}"
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

                    KalamItemPopupMenu(
                        eventDispatcher = eventDispatcher,
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

@Preview(showBackground = true)
@Composable
fun KalamItemPreviewLight() {
    SufiIshqTheme(darkTheme = false) {
        KalamItem(
            kalam = dummyKalam(),
            trackListType = TrackListType.All(),
            kalamMenuItems = listOf(),
            eventDispatcher = EventDispatcher.getInstance()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun KalamItemPreviewDark() {
    SufiIshqTheme(darkTheme = true) {
        KalamItem(
            kalam = dummyKalam(),
            trackListType = TrackListType.All(),
            kalamMenuItems = listOf(),
            eventDispatcher = EventDispatcher.getInstance()
        )
    }
}