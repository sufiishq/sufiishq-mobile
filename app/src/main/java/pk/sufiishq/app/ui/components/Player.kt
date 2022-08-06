package pk.sufiishq.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.paging.compose.collectAsLazyPagingItems
import pk.sufiishq.app.R
import pk.sufiishq.app.data.providers.KalamDataProvider
import pk.sufiishq.app.data.providers.PlayerDataProvider
import pk.sufiishq.app.data.providers.PlaylistDataProvider
import pk.sufiishq.app.helpers.PlayerState
import pk.sufiishq.app.models.KalamItemParam
import pk.sufiishq.app.ui.theme.SufiIshqTheme
import pk.sufiishq.app.utils.*

@Composable
fun Player(
    matColors: Colors,
    playerDataProvider: PlayerDataProvider,
    kalamDataProvider: KalamDataProvider,
    playlistDataProvider: PlaylistDataProvider
) {

    var backgroundColor = Color(219, 219, 219, 255)
    var contentColor = Color(43, 43, 43, 255)

    if (isDarkThem()) {
        backgroundColor = Color(34, 34, 34, 255)
        contentColor = Color.White
    }

    val sliderValue = playerDataProvider.getSeekbarValue().observeAsState()
    val playerState = playerDataProvider.getPlayerState().observeAsState().value
    val activeKalam = playerDataProvider.getActiveKalam().observeAsState()
    val shuffleState = playerDataProvider.getShuffleState().observeAsState()
    val currentPosition = playerDataProvider.getCurrentPosition().observeAsState()
    val totalDuration = playerDataProvider.getTotalDuration().observeAsState()
    val playlistItems = playlistDataProvider.getAll().observeAsState().optValue(listOf())
    val menu = rem(playerDataProvider.getMenuItems())
    val showMenu = rem(false)

    val showDownloadDialog = rem(false)
    val showPlaylistDialog = rem(false)
    val downloadError = playerDataProvider.getDownloadError().observeAsState()

    var kalamItemParam = rem<KalamItemParam?>(null)
    activeKalam.value?.let {
        kalamItemParam.value = KalamItemParam(
            kalam = it,
            kalamMenuItems = menu.value,
            playerDataProvider = playerDataProvider,
            kalamDataProvider = kalamDataProvider,
            playlistDataProvider = playlistDataProvider,
            lazyKalamItems = kalamDataProvider.getKalamDataFlow().collectAsLazyPagingItems(),
            playlistItems = playlistItems,
            searchText = mutableStateOf(kalamDataProvider.getActiveSearchKeyword()),
            trackType = kalamDataProvider.getActiveTrackType(),
            playlistId = kalamDataProvider.getActivePlaylistId()
        )
    }

    Box(
        Modifier
            .background(backgroundColor)
            .height(90.dp)
    ) {

        Slider(
            modifier = Modifier
                .fillMaxWidth()
                .height(0.dp),
            colors = SliderDefaults.colors(
                thumbColor = matColors.secondary
            ),
            value = sliderValue.optValue(0f),
            valueRange = 0f..100f,
            enabled = playerDataProvider.getSeekbarAccess().observeAsState().value!!,
            onValueChange = { playerDataProvider.updateSeekbarValue(it) },
            onValueChangeFinished = {
                playerDataProvider.onSeekbarChanged(sliderValue.optValue(0f))
            })

        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {


            ConstraintLayout(
                modifier = Modifier.fillMaxWidth(),
            ) {

                val (titleRef, metaInfoRef) = createRefs()

                MarqueeText(
                    modifier = Modifier.constrainAs(titleRef) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        end.linkTo(metaInfoRef.start, 12.dp)
                        width = Dimension.fillToConstraints
                    },
                    color = contentColor,
                    fontSize = 18.sp,
                    text = (activeKalam.value?.title ?: ""),
                    gradientEdgeColor = backgroundColor
                )

                Text(
                    modifier = Modifier.constrainAs(metaInfoRef) {
                        top.linkTo(titleRef.top)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    },
                    text = (activeKalam.value?.location ?: "") + (activeKalam.value?.recordeDate?.formatDateAs(prefix = " - ")
                        ?: ""),
                    fontSize = 13.sp,
                    color = contentColor
                )

            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = currentPosition.optValue(0).plus(999).formatTime,
                    color = contentColor,
                    fontSize = 13.sp
                )

                IconButton(onClick = {
                    playerDataProvider.setShuffleState(!shuffleState.optValue(false))
                }) {

                    val drawable = if (shuffleState.optValue(false)) {
                        R.drawable.ic_baseline_shuffle_on_24
                    } else {
                        R.drawable.ic_round_shuffle_24
                    }

                    Icon(
                        painter = painterResource(id = drawable),
                        contentDescription = null,
                        tint = contentColor
                    )
                }

                IconButton(onClick = {
                    playerDataProvider.playPrevious()
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_round_keyboard_double_arrow_left_24),
                        contentDescription = null,
                        tint = contentColor
                    )
                }

                Box(
                    modifier = Modifier
                        .width(45.dp)
                        .height(25.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (playerState == PlayerState.LOADING) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .height(25.dp)
                                .width(25.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        IconButton(onClick = { playerDataProvider.doPlayOrPause() }) {
                            Icon(
                                modifier = Modifier.size(25.dp),
                                painter = painterResource(id = if (playerState == PlayerState.PAUSE || playerState == PlayerState.IDLE) R.drawable.ic_play else R.drawable.ic_pause),
                                tint = contentColor,
                                contentDescription = null
                            )
                        }
                    }
                }

                IconButton(onClick = {
                    playerDataProvider.playNext()
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_round_keyboard_double_arrow_right_24),
                        contentDescription = null,
                        tint = contentColor
                    )
                }

                Box {

                    IconButton(onClick = {
                        showMenu.value = !showMenu.value
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_more_vert_24),
                            contentDescription = null,
                            tint = contentColor
                        )
                    }

                    DropdownMenu(expanded = showMenu.value, onDismissRequest = {
                        showMenu.value = false
                    }) {

                        val labelMarkAsFavorite = stringResource(id = R.string.mark_as_favorite)
                        val labelRemoveFavorite = stringResource(id = R.string.remove_favorite)
                        val labelDownload = stringResource(id = R.string.download_label)
                        val labelAddToPlaylist = stringResource(id = R.string.add_to_playlist)

                        menu.value
                            .filter { label ->

                                when (label) {
                                    labelMarkAsFavorite -> {
                                        activeKalam.value!!.isFavorite == 0
                                    }
                                    labelRemoveFavorite -> {
                                        activeKalam.value!!.isFavorite == 1
                                    }
                                    labelDownload -> {
                                        activeKalam.value!!.offlineSource.isEmpty()
                                    }
                                    else -> true
                                }
                            }
                            .forEach { label ->
                                DropdownMenuItem(onClick = {
                                    showMenu.value = false
                                    when (label) {
                                        labelAddToPlaylist -> showPlaylistDialog.value = true
                                        labelMarkAsFavorite -> kalamDataProvider.markAsFavorite(
                                            activeKalam.value!!
                                        )
                                        labelRemoveFavorite -> kalamDataProvider.removeFavorite(
                                            kalamItemParam.value!!
                                        )
                                        labelDownload -> {
                                            showDownloadDialog.value = true
                                            playerDataProvider.startDownload(activeKalam.value!!)
                                        }
                                    }
                                }) {
                                    PopupMenuLabel(label = label)
                                }
                            }
                    }
                }

                Text(
                    text = totalDuration.optValue(0).plus(999).formatTime,
                    color = contentColor,
                    fontSize = 13.sp
                )
            }

        }
    }

    kalamItemParam.value?.let {
        // kalam download dialog
        KalamItemDownloadDialog(
            showDownloadDialog = showDownloadDialog,
            kalamItemParam = it
        )

        // kalam download error dialog
        KalamDownloadErrorDialog(
            downloadError = downloadError,
            showDownloadDialog = showDownloadDialog,
            kalamItemParam = it
        )

        // playlist dialog
        PlaylistDialog(
            showPlaylistDialog = showPlaylistDialog,
            kalamItemParam = it
        )
    }

}

@Preview(showBackground = true)
@Composable
fun PlayerPreviewLight() {
    SufiIshqTheme(darkTheme = false) {
        Player(
            matColors = MaterialTheme.colors,
            playerDataProvider = dummyPlayerDataProvider(),
            kalamDataProvider = dummyKalamDataProvider(),
            playlistDataProvider = dummyPlaylistDataProvider()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PlayerPreviewDark() {
    SufiIshqTheme(darkTheme = true) {
        Player(
            matColors = MaterialTheme.colors,
            playerDataProvider = dummyPlayerDataProvider(),
            kalamDataProvider = dummyKalamDataProvider(),
            playlistDataProvider = dummyPlaylistDataProvider()
        )
    }
}