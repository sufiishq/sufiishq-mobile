package pk.sufiishq.app.ui.components.player

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import pk.sufiishq.app.R
import pk.sufiishq.app.core.event.dispatcher.EventDispatcher
import pk.sufiishq.app.core.event.events.PlayerEvents
import pk.sufiishq.app.helpers.PlayerState
import pk.sufiishq.app.models.KalamInfo

@Composable
fun PlayPauseButton(
    kalamInfo: KalamInfo?,
    contentColor: Color
) {

    Box(
        modifier = Modifier
            .width(25.dp)
            .height(25.dp),
        contentAlignment = Alignment.Center
    ) {
        if (kalamInfo?.playerState == PlayerState.LOADING) {
            CircularProgressIndicator(
                modifier = Modifier
                    .height(25.dp)
                    .width(25.dp),
                strokeWidth = 2.dp
            )
        } else {
            IconButton(onClick = {
                EventDispatcher.getInstance().dispatch(PlayerEvents.PlayPauseEvent)
            }) {
                Icon(
                    modifier = Modifier.size(25.dp),
                    painter = painterResource(id = if (kalamInfo?.playerState == PlayerState.PAUSE || kalamInfo?.playerState == PlayerState.IDLE) R.drawable.ic_play else R.drawable.ic_pause),
                    tint = contentColor,
                    contentDescription = null
                )
            }
        }
    }
}