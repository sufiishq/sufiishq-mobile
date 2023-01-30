package pk.sufiishq.app.ui.components.player

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import pk.sufiishq.app.R
import pk.sufiishq.app.annotations.ExcludeFromJacocoGeneratedReport
import pk.sufiishq.app.core.event.events.PlayerEvents
import pk.sufiishq.app.data.providers.PlayerDataProvider
import pk.sufiishq.app.ui.components.dialogs.KalamDownloadCompletedDialog
import pk.sufiishq.app.ui.components.dialogs.KalamDownloadErrorDialog
import pk.sufiishq.app.ui.components.dialogs.KalamDownloadInProgressDialog
import pk.sufiishq.app.ui.components.dialogs.KalamDownloadStartedDialog
import pk.sufiishq.app.ui.components.dialogs.PlaylistDialog
import pk.sufiishq.app.utils.dispatch
import pk.sufiishq.app.utils.fakePlayerDataProvider
import pk.sufiishq.app.utils.formatTime
import pk.sufiishq.app.utils.rem
import pk.sufiishq.app.viewmodels.PlayerViewModel
import pk.sufiishq.aurora.components.SIIcon
import pk.sufiishq.aurora.components.SIText
import pk.sufiishq.aurora.components.TextSize
import pk.sufiishq.aurora.layout.SIBox
import pk.sufiishq.aurora.layout.SIColumn
import pk.sufiishq.aurora.layout.SIConstraintLayout
import pk.sufiishq.aurora.layout.SIRow
import pk.sufiishq.aurora.theme.AuroraColor
import pk.sufiishq.aurora.theme.AuroraDark
import pk.sufiishq.aurora.theme.AuroraLight

@Composable
fun Player(
    playerDataProvider: PlayerDataProvider = hiltViewModel<PlayerViewModel>(),
) {

    val kalamInfo = playerDataProvider.getKalamInfo().observeAsState()
    val shuffleState = playerDataProvider.getShuffleState().observeAsState().value

    val showMenu = rem(false)

    SIBox(
        modifier = Modifier.height(90.dp),
        bgColor = AuroraColor.Primary
    ) { textColor ->

        SIColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            SIConstraintLayout(
                modifier = Modifier.fillMaxWidth(),
            ) {

                TrackTitleAndMeta(
                    scope = this,
                    onColor = textColor,
                    kalamInfo = kalamInfo.value
                )
            }

            SIRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                SIText(
                    text = kalamInfo.value?.currentProgress?.formatTime ?: 0.formatTime,
                    textColor = textColor,
                    textSize = TextSize.ExtraSmall
                )

                val drawable =
                    if (shuffleState!!) R.drawable.ic_baseline_shuffle_on_24 else R.drawable.ic_round_shuffle_24

                SIIcon(resId = drawable, tint = textColor, onClick = {
                    PlayerEvents.ChangeShuffle.dispatch()
                })

                SIIcon(
                    resId = R.drawable.ic_round_keyboard_double_arrow_left_24,
                    tint = textColor,
                    onClick = {
                        PlayerEvents.PlayPrevious.dispatch()
                    }
                )

                PlayPauseButton(
                    kalamInfo = kalamInfo,
                    contentColor = textColor
                )

                SIIcon(
                    resId = R.drawable.ic_round_keyboard_double_arrow_right_24,
                    tint = textColor,
                    onClick = {
                        PlayerEvents.PlayNext.dispatch()
                    }
                )

                SIBox {

                    SIIcon(
                        resId = R.drawable.ic_baseline_more_vert_24,
                        tint = textColor,
                        onClick = {
                            showMenu.value = !showMenu.value
                        }
                    )

                    kalamInfo.value?.let {
                        val menu = playerDataProvider.getPopupMenuItems(it.kalam)
                        PlayerOverflowMenu(
                            menu = menu,
                            showMenu = showMenu,
                            kalam = it.kalam
                        )
                    }
                }

                SIText(
                    text = kalamInfo.value?.totalDuration?.formatTime ?: 0.formatTime,
                    textColor = textColor,
                    textSize = TextSize.ExtraSmall
                )
            }

        }

        TrackSlider(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .height(0.dp),
            kalamInfo = kalamInfo.value
        )
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
    AuroraLight {
        Player(
            playerDataProvider = fakePlayerDataProvider()
        )
    }
}

@ExcludeFromJacocoGeneratedReport
@Preview(showBackground = true)
@Composable
fun PlayerPreviewDark() {
    AuroraDark {
        Player(
            playerDataProvider = fakePlayerDataProvider()
        )
    }
}