package pk.sufiishq.aurora.components

import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pk.sufiishq.aurora.theme.AuroraColor

@Composable
fun SICircularProgressIndicator(
    color: AuroraColor = AuroraColor.Secondary,
    modifier: Modifier = Modifier,
    strokeWidth: Int? = 0
) {

    CircularProgressIndicator(
        color = color.color(),
        modifier = modifier,
        strokeWidth = strokeWidth?.dp ?: ProgressIndicatorDefaults.StrokeWidth
    )
}