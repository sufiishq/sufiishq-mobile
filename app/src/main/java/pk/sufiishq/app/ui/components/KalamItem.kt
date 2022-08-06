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
import androidx.compose.runtime.mutableStateOf
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
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.flowOf
import pk.sufiishq.app.R
import pk.sufiishq.app.helpers.Screen
import pk.sufiishq.app.models.Kalam
import pk.sufiishq.app.models.KalamItemParam
import pk.sufiishq.app.ui.theme.SufiIshqTheme
import pk.sufiishq.app.utils.*

@Composable
fun KalamItem(kalamItemParam: KalamItemParam, onMenuItemClicked: (kalam: Kalam, label: String) -> Unit) {

    val (kalam, _, playerDataProvider, _, _, _, _, _, trackType, playlistId) = kalamItemParam

    val matColors = MaterialTheme.colors

    val isExpanded = rem(false)

    Column(Modifier.clickable {
        playerDataProvider.changeTrack(kalam, trackType, playlistId)
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

                    KalamItemPopupMenu(isExpanded = isExpanded, kalamItemParam = kalamItemParam, onMenuItemClicked = onMenuItemClicked)
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
            KalamItemParam(
                kalam = dummyTrack(),
                listOf(""),
                dummyPlayerDataProvider(),
                dummyKalamDataProvider(),
                dummyPlaylistDataProvider(),
                flowOf<PagingData<Kalam>>().collectAsLazyPagingItems(),
                listOf(),
                remember { mutableStateOf("") },
                Screen.Tracks.ALL,
                0
            )
        ) { _, _ -> }
    }
}

@Preview(showBackground = true)
@Composable
fun KalamItemPreviewDark() {
    SufiIshqTheme(darkTheme = true) {
        KalamItem(
            KalamItemParam(
                kalam = dummyTrack(),
                listOf(""),
                dummyPlayerDataProvider(),
                dummyKalamDataProvider(),
                dummyPlaylistDataProvider(),
                flowOf<PagingData<Kalam>>().collectAsLazyPagingItems(),
                listOf(),
                remember { mutableStateOf("") },
                Screen.Tracks.ALL,
                0
            )
        ) { _, _ -> }
    }
}