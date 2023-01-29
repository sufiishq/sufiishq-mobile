package pk.sufiishq.aurora.widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import pk.sufiishq.aurora.components.SIDivider
import pk.sufiishq.aurora.layout.SIBox
import pk.sufiishq.aurora.theme.AuroraColor
import pk.sufiishq.aurora.theme.getForegroundColor
import pk.sufiishq.aurora.theme.validateBackground

@Composable
fun SITopAppBar(
    bgColor: AuroraColor = AuroraColor.Primary,
    centerDrawable: Int,
    navigationIcon: @Composable ((fgColor: AuroraColor) -> Unit)? = null,
    actions: @Composable RowScope.(fgColor: AuroraColor) -> Unit = {}
) {

    SITopAppBar(
        bgColor = bgColor,
        centerContent = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = centerDrawable),
                    contentDescription = null
                )
            }
        },
        navigationIcon = navigationIcon,
        actions = actions
    )
}

@Composable
fun SITopAppBar(
    bgColor: AuroraColor = AuroraColor.Primary,
    navigationIcon: @Composable ((fgColor: AuroraColor) -> Unit)? = null,
    centerContent: @Composable (fgColor: AuroraColor) -> Unit = {},
    actions: @Composable RowScope.(fgColor: AuroraColor) -> Unit = {}
) {

    val mBgColor = bgColor.validateBackground()
    val mFgColor = bgColor.getForegroundColor()

    SIBox {
        TopAppBar(
            backgroundColor = mBgColor.color(),
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
            color = AuroraColor.SecondaryVariant,
            thickness = 3.dp
        )
    }

}

