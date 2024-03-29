/*
 * Copyright 2022-2023 SufiIshq
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pk.sufiishq.app.ui.main.topbar

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import pk.sufiishq.app.annotations.ExcludeFromJacocoGeneratedReport
import pk.sufiishq.app.feature.app.controller.MainController
import pk.sufiishq.app.feature.app.controller.MainViewModel
import pk.sufiishq.app.helpers.popupmenu.PopupMenuItem
import pk.sufiishq.app.utils.ImageRes
import pk.sufiishq.app.utils.TextRes
import pk.sufiishq.app.utils.extention.optString
import pk.sufiishq.app.utils.fakeMainController
import pk.sufiishq.aurora.components.SIDropdownMenuItem
import pk.sufiishq.aurora.models.DataMenuItem
import pk.sufiishq.aurora.theme.AuroraColor
import pk.sufiishq.aurora.theme.AuroraDark
import pk.sufiishq.aurora.theme.AuroraLight
import pk.sufiishq.aurora.widgets.SIPopupMenu

@Composable
fun AppBarOverflowMenu(
    iconColor: AuroraColor? = null,
    mainController: MainController = hiltViewModel<MainViewModel>(),
) {
    val context = LocalContext.current

    SIPopupMenu(
        modifier = Modifier.testTag("test_tag_app_bar_popup_menu"),
        resId = ImageRes.ic_baseline_more_vert_24,
        iconTint = iconColor,
        data = mainController.popupMenuItems(),
        onClick = { handleClick(mainController, it, context) },
    )
}

private fun handleClick(
    mainController: MainController,
    popupMenuItem: DataMenuItem,
    context: Context,
) {
    when (popupMenuItem) {
        is PopupMenuItem.Share -> mainController.shareApp(context as ComponentActivity)
        is PopupMenuItem.Facebook -> {
            mainController.openFacebookGroup(
                context,
                "https://www.facebook.com/groups/375798102574085",
            )
        }
    }
}

@ExcludeFromJacocoGeneratedReport
@Preview
@Composable
fun AppBarOverflowMenuPreviewLight() {
    AuroraLight {
        AppBarOverflowMenu(
            AuroraColor.OnPrimary,
            fakeMainController(),
        )
    }
}

@ExcludeFromJacocoGeneratedReport
@Preview
@Composable
fun AppBarOverflowMenuPreviewDark() {
    AuroraDark {
        AppBarOverflowMenu(
            AuroraColor.OnPrimary,
            fakeMainController(),
        )
    }
}

@ExcludeFromJacocoGeneratedReport
@Preview(widthDp = 150)
@Composable
fun OverflowMenuItemPreviewLight() {
    AuroraLight {
        SIDropdownMenuItem(
            label = optString(TextRes.menu_item_share),
            labelColor = AuroraColor.OnBackground,
            resId = ImageRes.share,
            onClick = {},
        )
    }
}

@ExcludeFromJacocoGeneratedReport
@Preview(widthDp = 150)
@Composable
fun OverflowMenuItemPreviewDark() {
    AuroraDark {
        SIDropdownMenuItem(
            label = optString(TextRes.menu_item_share),
            labelColor = AuroraColor.OnBackground,
            resId = ImageRes.share,
            onClick = {},
        )
    }
}
