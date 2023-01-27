package pk.sufiishq.aurora.components

import androidx.compose.foundation.background
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import pk.sufiishq.aurora.theme.AuroraColor
import pk.sufiishq.aurora.theme.getForegroundColor

@Composable
fun SISurface(
    modifier: Modifier = Modifier,
    bgColor: AuroraColor = AuroraColor.Surface,
    content: @Composable (bgColor: AuroraColor, fgColor: AuroraColor) -> Unit
) {
    Surface(
        modifier = modifier.background(bgColor.color()),
        content = {
            content(bgColor, bgColor.getForegroundColor())
        }
    )
}