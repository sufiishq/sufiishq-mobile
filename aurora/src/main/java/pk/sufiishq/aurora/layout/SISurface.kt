package pk.sufiishq.aurora.layout

import androidx.compose.foundation.BorderStroke
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import pk.sufiishq.aurora.theme.AuroraColor
import pk.sufiishq.aurora.theme.getForegroundColor

@Composable
fun SISurface(
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    bgColor: AuroraColor = AuroraColor.Surface,
    border: BorderStroke? = null,
    elevation: Dp = 0.dp,
    content: @Composable (fgColor: AuroraColor) -> Unit
) {
    Surface(
        modifier = modifier,
        shape = shape,
        color = bgColor.color(),
        contentColor = bgColor.getForegroundColor(bgColor.color()).color(),
        border = border,
        elevation = elevation
    ) {
        content(bgColor.getForegroundColor(bgColor.color()))
    }
}