package pk.sufiishq.aurora.components

import androidx.compose.material.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import pk.sufiishq.aurora.theme.AuroraColor

@Composable
fun SILinearProgressIndicator(
    modifier: Modifier,
    progress: Float? = null
) {
    progress?.apply {
        LinearProgressIndicator(
            modifier = modifier,
            backgroundColor = AuroraColor.Disabled.color(),
            color = AuroraColor.Secondary.color(),
            progress = progress
        )
    } ?: run {
        LinearProgressIndicator(
            modifier = modifier,
            backgroundColor = AuroraColor.Disabled.color(),
            color = AuroraColor.Secondary.color()
        )
    }
}