package pk.sufiishq.app.ui.components

import androidx.compose.foundation.layout.width
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import pk.sufiishq.app.R
import pk.sufiishq.app.core.event.dispatcher.EventDispatcher
import pk.sufiishq.app.core.event.events.GlobalEvents
import pk.sufiishq.app.utils.rem

@Composable
fun AppBarOverflowMenu() {

    val eventDispatcher = EventDispatcher.getInstance()
    val showOverflowMenu = rem(false)
    val context = LocalContext.current

    IconButton(
        modifier = Modifier.testTag("menu_button_tag"),
        onClick = {
            showOverflowMenu.value = !showOverflowMenu.value
        }
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_baseline_more_vert_24),
            contentDescription = null,
            tint = MaterialTheme.colors.primary
        )
    }

    DropdownMenu(
        expanded = showOverflowMenu.value,
        modifier = Modifier.width(150.dp).testTag("dropdown_menu_tag"),
        onDismissRequest = {
            showOverflowMenu.value = false
        }
    ) {

        OverflowMenuItem(
            label = "Share",
            drawableId = R.drawable.ic_round_share_24,
            onClick = {
                showOverflowMenu.value = false
                eventDispatcher.dispatch(GlobalEvents.ShareApp(context))
            }
        )

        OverflowMenuItem(
            label = "Facebook",
            drawableId = R.drawable.ic_round_groups_24,
            onClick = {
                showOverflowMenu.value = false
                eventDispatcher.dispatch(
                    GlobalEvents.OpenFacebookGroup(
                        context,
                        "https://www.facebook.com/groups/375798102574085"
                    )
                )
            }
        )
    }
}

@Composable
private fun OverflowMenuItem(
    label: String,
    drawableId: Int? = null,
    iconTint: Color? = MaterialTheme.colors.primary,
    onClick: () -> Unit
) {
    DropdownMenuItem(onClick) {
        PopupMenuLabel(
            label = label,
            drawableId = drawableId,
            iconTint = iconTint
        )
    }
}