package pk.sufiishq.app.ui.main.player

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import io.github.esentsov.PackagePrivate
import pk.sufiishq.app.R
import pk.sufiishq.app.core.player.PlayerState
import pk.sufiishq.app.data.controller.PlayerController
import pk.sufiishq.app.models.KalamInfo
import pk.sufiishq.app.ui.components.ContentBackground
import pk.sufiishq.aurora.components.SICircularProgressIndicator
import pk.sufiishq.aurora.components.SIIcon
import pk.sufiishq.aurora.components.SIWidthSpace
import pk.sufiishq.aurora.layout.SICard
import pk.sufiishq.aurora.layout.SIRow
import pk.sufiishq.aurora.theme.AuroraColor

@PackagePrivate
@Composable
fun PlayPauseButton(
    playerController: PlayerController,
    kalamInfo: State<KalamInfo?>,
    boxScope: BoxScope
) {

    with(boxScope) {
        SIRow(
            modifier = Modifier
                .align(Alignment.CenterStart)
        ) {
            SIWidthSpace(value = 2)
            SICard(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape),
                elevation = 0.dp,
            ) {

                ContentBackground {
                    SICard(
                        modifier = Modifier
                            .fillMaxSize(0.75f)
                            .clip(CircleShape)
                            .clickable {
                                playerController.doPlayOrPause()
                            },
                        elevation = 0.dp,
                        bgColor = AuroraColor.Background
                    ) { contentColor ->

                        if (kalamInfo.value == null || kalamInfo.value?.playerState == PlayerState.LOADING) {
                            SICircularProgressIndicator(
                                color = AuroraColor.SecondaryVariant,
                                strokeWidth = 4
                            )
                        } else {

                            SIIcon(
                                modifier = Modifier.padding(20.dp),
                                resId = if (kalamInfo.value?.playerState == PlayerState.PAUSE || kalamInfo.value?.playerState == PlayerState.IDLE) R.drawable.ic_play else R.drawable.ic_pause,
                                tint = contentColor
                            )
                        }
                    }
                }
            }
        }
    }

}