package pk.sufiishq.app.ui.main.topbar

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import pk.sufiishq.app.R
import pk.sufiishq.app.annotations.ExcludeFromJacocoGeneratedReport
import pk.sufiishq.app.data.providers.MainDataProvider
import pk.sufiishq.app.helpers.ScreenType
import pk.sufiishq.app.helpers.popupmenu.PopupMenuItem
import pk.sufiishq.app.utils.fakeMainDataProvider
import pk.sufiishq.app.utils.optString
import pk.sufiishq.app.viewmodels.MainViewModel
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
    mainDataProvider: MainDataProvider = hiltViewModel<MainViewModel>()
) {

    val context = LocalContext.current

    SIPopupMenu(
        resId = R.drawable.ic_baseline_more_vert_24,
        iconTint = iconColor,
        data = mainDataProvider.popupMenuItems(),
        onClick = {
            handleClick(mainDataProvider, it, context, navController)
        }
    )
}

private fun handleClick(
    mainDataProvider: MainDataProvider,
    popupMenuItem: DataMenuItem,
    context: Context,
    navController: NavController
) {
    when (popupMenuItem) {
        is PopupMenuItem.Share -> mainDataProvider.shareApp(context as ComponentActivity)
        is PopupMenuItem.Facebook -> {
            mainDataProvider.openFacebookGroup(
                context,
                "https://www.facebook.com/groups/375798102574085"
            )
        }
        is PopupMenuItem.Help -> {
            navController.navigate(ScreenType.Help.buildRoute()) {
                popUpTo(ScreenType.Dashboard.buildRoute())
                launchSingleTop = true
            }
        }
        is PopupMenuItem.Theme -> {
            navController.navigate(ScreenType.Theme.buildRoute()) {
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
            fakeMainDataProvider()
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
            fakeMainDataProvider()
        )
    }
}

@ExcludeFromJacocoGeneratedReport
@Preview(widthDp = 150)
@Composable
fun OverflowMenuItemPreviewLight() {
    AuroraLight {
        SIDropdownMenuItem(
            label = optString(R.string.menu_item_share),
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
            label = optString(R.string.menu_item_share),
            labelColor = AuroraColor.OnBackground,
            resId = R.drawable.ic_round_share_24,
            onClick = {}
        )
    }
}