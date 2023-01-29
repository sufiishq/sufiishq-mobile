package pk.sufiishq.aurora.components

import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import pk.sufiishq.aurora.theme.AuroraColor
import pk.sufiishq.aurora.theme.validateBackground

@Composable
fun SIDivider(
    modifier: Modifier = Modifier,
    color: AuroraColor = AuroraColor.Primary,
    thickness: Dp = 1.dp,
    startIndent: Dp = 0.dp
) {
    Divider(
        modifier = modifier,
        color.validateBackground().color(),
        thickness = thickness,
        startIndent = startIndent
    )
}