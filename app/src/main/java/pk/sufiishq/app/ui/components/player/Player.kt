package pk.sufiishq.app.ui.components.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import pk.sufiishq.app.R
import pk.sufiishq.app.annotations.ExcludeFromJacocoGeneratedReport
import pk.sufiishq.app.core.event.events.KalamEvents
import pk.sufiishq.app.core.event.events.PlayerEvents
import pk.sufiishq.app.data.providers.PlayerDataProvider
import pk.sufiishq.app.ui.components.MarqueeText
import pk.sufiishq.app.ui.components.PopupMenuLabel
import pk.sufiishq.app.ui.components.buttons.SimpleIconButton
import pk.sufiishq.app.ui.components.dialogs.KalamDownloadCompletedDialog
import pk.sufiishq.app.ui.components.dialogs.KalamDownloadErrorDialog
import pk.sufiishq.app.ui.components.dialogs.KalamDownloadInProgressDialog
import pk.sufiishq.app.ui.components.dialogs.KalamDownloadStartedDialog
import pk.sufiishq.app.ui.components.dialogs.PlaylistDialog
import pk.sufiishq.app.ui.theme.SufiIshqTheme
import pk.sufiishq.app.utils.dispatch
import pk.sufiishq.app.utils.dummyPlayerDataProvider
import pk.sufiishq.app.utils.formatDateAs
import pk.sufiishq.app.utils.formatTime
import pk.sufiishq.app.utils.isDarkThem
import pk.sufiishq.app.utils.rem
import pk.sufiishq.app.viewmodels.PlayerViewModel

@Composable
fun Player(
    playerDataProvider: PlayerDataProvider = hiltViewModel<PlayerViewModel>(),

    ) {

    val matColors = MaterialTheme.colors
    var backgroundColor = Color(219, 219, 219, 255)
    var contentColor = Color(43, 43, 43, 255)

    if (isDarkThem()) {
        backgroundColor = Color(34, 34, 34, 255)
        contentColor = Color.White
    }

    val kalamInfo by playerDataProvider.getKalamInfo().observeAsState()
    val shuffleState by playerDataProvider.getShuffleState().observeAsState()

    val menu by rem(playerDataProvider.getMenuItems())
    val showMenu = rem(false)

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
            value = kalamInfo?.currentProgress?.toFloat() ?: 0f,
            valueRange = 0f..(kalamInfo?.totalDuration?.toFloat() ?: 0f),
            enabled = kalamInfo?.enableSeekbar ?: false,
            onValueChange = { PlayerEvents.UpdateSeekbar(it).dispatch() },
            onValueChangeFinished = {
                PlayerEvents.SeekbarChanged(
                    kalamInfo?.currentProgress ?: 0
                ).dispatch()
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
                    text = (kalamInfo?.kalam?.title ?: ""),
                    gradientEdgeColor = backgroundColor
                )

                Text(
                    modifier = Modifier.constrainAs(metaInfoRef) {
                        top.linkTo(titleRef.top)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    },
                    text = (kalamInfo?.kalam?.location
                        ?: "") + (kalamInfo?.kalam?.recordeDate?.formatDateAs(prefix = " - ")
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
                    text = kalamInfo?.currentProgress?.formatTime ?: 0.formatTime,
                    color = contentColor,
                    fontSize = 12.sp
                )

                val drawable = if (shuffleState!!) {
                    R.drawable.ic_baseline_shuffle_on_24
                } else {
                    R.drawable.ic_round_shuffle_24
                }

                SimpleIconButton(
                    resId = drawable,
                    tint = contentColor
                ) {
                    PlayerEvents.ChangeShuffle.dispatch()
                }

                SimpleIconButton(
                    resId = R.drawable.ic_round_keyboard_double_arrow_left_24,
                    tint = contentColor
                ) {
                    PlayerEvents.PlayPrevious.dispatch()
                }

                PlayPauseButton(
                    kalamInfo = kalamInfo,
                    contentColor = contentColor
                )

                SimpleIconButton(
                    resId = R.drawable.ic_round_keyboard_double_arrow_right_24,
                    tint = contentColor
                ) {
                    PlayerEvents.PlayNext.dispatch()
                }

                Box {

                    SimpleIconButton(
                        resId = R.drawable.ic_baseline_more_vert_24,
                        tint = contentColor
                    ) {
                        showMenu.value = !showMenu.value
                    }

                    DropdownMenu(expanded = showMenu.value, onDismissRequest = {
                        showMenu.value = false
                    }) {

                        val labelMarkAsFavorite = stringResource(id = R.string.mark_as_favorite)
                        val labelRemoveFavorite = stringResource(id = R.string.remove_favorite)
                        val labelDownload = stringResource(id = R.string.download_label)
                        val labelAddToPlaylist = stringResource(id = R.string.add_to_playlist)
                        val activeKalam = kalamInfo?.kalam!!
                        menu.filter { label ->

                            when (label) {
                                labelMarkAsFavorite -> {
                                    activeKalam.isFavorite == 0
                                }
                                labelRemoveFavorite -> {
                                    activeKalam.isFavorite == 1
                                }
                                labelDownload -> {
                                    activeKalam.offlineSource.isEmpty()
                                }
                                else -> true
                            }
                        }
                            .forEach { label ->
                                DropdownMenuItem(onClick = {
                                    showMenu.value = false
                                    when (label) {
                                        labelAddToPlaylist -> PlayerEvents.ShowPlaylistDialog(
                                            activeKalam
                                        ).dispatch()
                                        labelMarkAsFavorite -> KalamEvents.MarkAsFavoriteKalam(
                                            activeKalam
                                        ).dispatch()
                                        labelRemoveFavorite -> KalamEvents.RemoveFavoriteKalam(
                                            activeKalam
                                        ).dispatch()
                                        labelDownload -> PlayerEvents.StartDownload(activeKalam)
                                            .dispatch()
                                    }
                                }) {
                                    PopupMenuLabel(label = label)
                                }
                            }
                    }
                }

                Text(
                    text = kalamInfo?.totalDuration?.formatTime ?: 0.formatTime,
                    color = contentColor,
                    fontSize = 12.sp
                )
            }

        }
    }

    val kalamDownloadState = playerDataProvider.getKalamDownloadState().observeAsState()

    // kalam download start dialog
    KalamDownloadStartedDialog(
        kalamDownloadState = kalamDownloadState
    )

    // kalam download in-progress dialog
    KalamDownloadInProgressDialog(
        kalamDownloadState = kalamDownloadState
    )

    // kalam download completed dialog
    KalamDownloadCompletedDialog(
        kalamDownloadState = kalamDownloadState
    )

    // kalam download error dialog
    KalamDownloadErrorDialog(
        kalamDownloadState = kalamDownloadState
    )

    // playlist dialog
    PlaylistDialog(
        playlistState = playerDataProvider.getAllPlaylist().observeAsState(),
        showPlaylistDialog = playerDataProvider.getShowPlaylistDialog().observeAsState()
    )
}

@ExcludeFromJacocoGeneratedReport
@Preview(showBackground = true)
@Composable
fun PlayerPreviewLight() {
    SufiIshqTheme(darkTheme = false) {
        Player(
            playerDataProvider = dummyPlayerDataProvider()
        )
    }
}

@ExcludeFromJacocoGeneratedReport
@Preview(showBackground = true)
@Composable
fun PlayerPreviewDark() {
    SufiIshqTheme(darkTheme = true) {
        Player(
            playerDataProvider = dummyPlayerDataProvider()
        )
    }
}