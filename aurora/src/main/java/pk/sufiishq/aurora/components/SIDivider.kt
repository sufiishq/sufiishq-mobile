package pk.sufiishq.aurora.components

import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import pk.sufiishq.aurora.theme.AuroraColor
import pk.sufiishq.aurora.theme.validateBackground

@Composable
fun SIDivider(
    modifier: Modifier = Modifier,
    color: AuroraColor = AuroraColor.PrimaryContainer,
    thickness: Dp = 1.dp,
    topSpace: Int = 0,
    bottomSpace: Int = 0
) {

    SIHeightSpace(value = topSpace)

    HorizontalDivider(
        modifier = modifier,
        color = color.validateBackground().color(),
        thickness = thickness,
    )

    SIHeightSpace(value = bottomSpace)
}