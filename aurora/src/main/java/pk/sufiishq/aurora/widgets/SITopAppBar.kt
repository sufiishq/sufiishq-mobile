package pk.sufiishq.aurora.widgets

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pk.sufiishq.aurora.components.SIDivider
import pk.sufiishq.aurora.components.SIImage
import pk.sufiishq.aurora.layout.SIBox
import pk.sufiishq.aurora.theme.AuroraColor
import pk.sufiishq.aurora.theme.getForegroundColor
import pk.sufiishq.aurora.theme.validateBackground

@Composable
fun SITopAppBar(
    bgColor: AuroraColor = AuroraColor.Background,
    centerDrawable: Int,
    navigationIcon: @Composable ((fgColor: AuroraColor) -> Unit)? = null,
    actions: @Composable RowScope.(fgColor: AuroraColor) -> Unit = {}
) {

    SITopAppBar(
        bgColor = bgColor,
        centerContent = {
            SIBox(
                modifier = Modifier
                    .fillMaxWidth(),
                padding = 12,
                contentAlignment = Alignment.Center
            ) {
                SIImage(
                    modifier = Modifier.height(32.dp),
                    resId = centerDrawable,
                    tintColor = it
                )
            }
        },
        navigationIcon = navigationIcon,
        actions = actions
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SITopAppBar(
    bgColor: AuroraColor = AuroraColor.Background,
    navigationIcon: @Composable ((fgColor: AuroraColor) -> Unit)? = null,
    centerContent: @Composable (fgColor: AuroraColor) -> Unit = {},
    actions: @Composable RowScope.(fgColor: AuroraColor) -> Unit = {}
) {

    val mBgColor = bgColor.validateBackground()
    val mFgColor = bgColor.getForegroundColor(bgColor.color())

    SIBox {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = mBgColor.color()
            ),
            title = {
                centerContent(mFgColor)
            },
            navigationIcon = {
                navigationIcon?.invoke(mFgColor)
            },
            actions = {
                actions(mFgColor)
            }
        )
        SIDivider(
            modifier = Modifier.align(Alignment.BottomCenter),
            color = AuroraColor.SecondaryContainer,
            thickness = 3.dp
        )
    }

}

