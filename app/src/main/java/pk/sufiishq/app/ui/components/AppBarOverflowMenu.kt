package pk.sufiishq.app.ui.components

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import pk.sufiishq.app.R
import pk.sufiishq.app.annotations.ExcludeFromJacocoGeneratedReport
import pk.sufiishq.app.core.event.events.GlobalEvents
import pk.sufiishq.app.data.providers.GlobalDataProvider
import pk.sufiishq.app.helpers.PopupMenuItem
import pk.sufiishq.app.helpers.ScreenType
import pk.sufiishq.app.utils.dispatch
import pk.sufiishq.app.utils.dummyGlobalDataProvider
import pk.sufiishq.app.viewmodels.GlobalViewModel
import pk.sufiishq.aurora.components.SIDropdownMenuItem
import pk.sufiishq.aurora.models.DataMenuItem
import pk.sufiishq.aurora.theme.AuroraColor
import pk.sufiishq.aurora.theme.AuroraDark
import pk.sufiishq.aurora.theme.AuroraLight
import pk.sufiishq.aurora.widgets.SIPopupMenu

@Composable
fun AppBarOverflowMenu(
    navController: NavController,
    iconColor: AuroraColor? = null,
    globalDataProvider: GlobalDataProvider = hiltViewModel<GlobalViewModel>()
) {

    val context = LocalContext.current

    SIPopupMenu(
        resId = R.drawable.ic_baseline_more_vert_24,
        iconTint = iconColor,
        data = globalDataProvider.popupMenuItems(),
        onClick = {
            handleClick(it, context, navController)
        }
    )
}

private fun handleClick(
    popupMenuItem: DataMenuItem,
    context: Context,
    navController: NavController
) {
    when (popupMenuItem) {
        is PopupMenuItem.Share -> GlobalEvents.ShareApp(context).dispatch()
        is PopupMenuItem.Facebook -> {
            GlobalEvents.OpenFacebookGroup(
                context,
                "https://www.facebook.com/groups/375798102574085"
            ).dispatch()
        }
        is PopupMenuItem.Help -> {
            navController.navigate(ScreenType.Help.route()) {
                popUpTo(ScreenType.Dashboard.route())
                launchSingleTop = true
            }
        }
        is PopupMenuItem.Theme -> {
            navController.navigate(ScreenType.Theme.route()) {
                popUpTo(ScreenType.Dashboard.route())
                launchSingleTop = true
            }
        }
    }
}


@ExcludeFromJacocoGeneratedReport
@Preview
@Composable
fun AppBarOverflowMenuPreviewLight() {
    AuroraLight {
        AppBarOverflowMenu(
            rememberNavController(),
            AuroraColor.OnPrimary,
            dummyGlobalDataProvider()
        )
    }
}

@ExcludeFromJacocoGeneratedReport
@Preview
@Composable
fun AppBarOverflowMenuPreviewDark() {
    AuroraDark {
        AppBarOverflowMenu(
            rememberNavController(),
            AuroraColor.OnPrimary,
            dummyGlobalDataProvider()
        )
    }
}

@ExcludeFromJacocoGeneratedReport
@Preview(widthDp = 150)
@Composable
fun OverflowMenuItemPreviewLight() {
    AuroraLight {
        SIDropdownMenuItem(
            label = "Share",
            labelColor = AuroraColor.OnBackground,
            resId = R.drawable.ic_round_share_24,
            onClick = {}
        )
    }
}

@ExcludeFromJacocoGeneratedReport
@Preview(widthDp = 150)
@Composable
fun OverflowMenuItemPreviewDark() {
    AuroraDark {
        SIDropdownMenuItem(
            label = "Share",
            labelColor = AuroraColor.OnBackground,
            resId = R.drawable.ic_round_share_24,
            onClick = {}
        )
    }
}