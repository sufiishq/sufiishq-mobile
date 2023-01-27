package pk.sufiishq.app.ui.components.player

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pk.sufiishq.app.R
import pk.sufiishq.app.core.event.events.PlayerEvents
import pk.sufiishq.app.helpers.PlayerState
import pk.sufiishq.app.models.KalamInfo
import pk.sufiishq.app.utils.dispatch
import pk.sufiishq.aurora.components.SICircularProgressIndicator
import pk.sufiishq.aurora.components.SIIcon
import pk.sufiishq.aurora.layout.SIBox
import pk.sufiishq.aurora.theme.AuroraColor

@Composable
fun PlayPauseButton(
    kalamInfo: State<KalamInfo?>,
    contentColor: AuroraColor
) {

    SIBox(
        modifier = Modifier
            .width(25.dp)
            .height(25.dp),
    ) {
        if (kalamInfo.value?.playerState == PlayerState.LOADING) {
            SICircularProgressIndicator(
                color = contentColor,
                modifier = Modifier.size(25.dp),
                strokeWidth = 2
            )
        } else {

            SIIcon(
                resId = if (kalamInfo.value?.playerState == PlayerState.PAUSE || kalamInfo.value?.playerState == PlayerState.IDLE) R.drawable.ic_play else R.drawable.ic_pause,
                tint = contentColor,
                modifier = Modifier.size(25.dp),
                onClick = {
                    PlayerEvents.PlayPauseEvent.dispatch()
                }
            )
        }
    }
}