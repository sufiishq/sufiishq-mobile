package pk.sufiishq.app.ui.components.buttons

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import pk.sufiishq.app.R
import pk.sufiishq.app.core.event.dispatcher.EventDispatcher
import pk.sufiishq.app.core.event.events.GlobalEvents

@Composable
fun ShareIconButton(
    eventDispatcher: EventDispatcher
) {
    val context = LocalContext.current
    IconButton(onClick = {
        eventDispatcher.dispatch(GlobalEvents.ShareApp(context))
    }) {
        Icon(
            painter = painterResource(id = R.drawable.ic_round_share_24),
            contentDescription = null,
            tint = MaterialTheme.colors.primary
        )
    }
}