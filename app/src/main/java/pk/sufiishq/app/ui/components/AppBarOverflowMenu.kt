package pk.sufiishq.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import pk.sufiishq.app.R
import pk.sufiishq.app.annotations.ExcludeFromJacocoGeneratedReport
import pk.sufiishq.app.core.event.events.GlobalEvents
import pk.sufiishq.app.helpers.ScreenType
import pk.sufiishq.app.ui.theme.SufiIshqTheme
import pk.sufiishq.app.utils.dispatch
import pk.sufiishq.app.utils.rem

@Composable
fun AppBarOverflowMenu(
    navController: NavController
) {

    val showOverflowMenu = rem(false)
    val context = LocalContext.current

    IconButton(
        modifier = Modifier.testTag("overflow_menu_button"),
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
        modifier = Modifier
            .width(150.dp)
            .testTag("dropdown_menu"),
        onDismissRequest = {
            showOverflowMenu.value = false
        }
    ) {

        OverflowMenuItem(
            label = "Share",
            drawableId = R.drawable.ic_round_share_24,
            onClick = {
                showOverflowMenu.value = false
                GlobalEvents.ShareApp(context).dispatch()
            }
        )

        OverflowMenuItem(
            label = "Facebook",
            drawableId = R.drawable.ic_round_groups_24,
            onClick = {
                showOverflowMenu.value = false
                GlobalEvents.OpenFacebookGroup(
                    context,
                    "https://www.facebook.com/groups/375798102574085"
                ).dispatch()
            }
        )

        OverflowMenuItem(
            label = "Help",
            drawableId = R.drawable.ic_round_help_24,
            onClick = {
                showOverflowMenu.value = false
                navController.navigate(ScreenType.Help.route)
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

@ExcludeFromJacocoGeneratedReport
@Preview
@Composable
fun AppBarOverflowMenuPreviewLight() {
    SufiIshqTheme {
        Box(
            Modifier.background(MaterialTheme.colors.secondaryVariant)
        ) {
            AppBarOverflowMenu(rememberNavController())
        }
    }
}

@ExcludeFromJacocoGeneratedReport
@Preview
@Composable
fun AppBarOverflowMenuPreviewDark() {
    SufiIshqTheme(darkTheme = true) {
        Box(
            Modifier.background(MaterialTheme.colors.secondaryVariant)
        ) {
            AppBarOverflowMenu(rememberNavController())
        }
    }
}

@ExcludeFromJacocoGeneratedReport
@Preview(widthDp = 150)
@Composable
fun OverflowMenuItemPreviewLight() {
    SufiIshqTheme {
        Box(
            Modifier.background(MaterialTheme.colors.secondaryVariant)
        ) {
            OverflowMenuItem(
                label = "Share",
                drawableId = R.drawable.ic_round_share_24,
                onClick = {}
            )
        }
    }
}

@ExcludeFromJacocoGeneratedReport
@Preview(widthDp = 150)
@Composable
fun OverflowMenuItemPreviewDark() {
    SufiIshqTheme(darkTheme = true) {
        Box(
            Modifier.background(MaterialTheme.colors.secondaryVariant)
        ) {
            OverflowMenuItem(
                label = "Share",
                drawableId = R.drawable.ic_round_share_24,
                onClick = {}
            )
        }
    }
}