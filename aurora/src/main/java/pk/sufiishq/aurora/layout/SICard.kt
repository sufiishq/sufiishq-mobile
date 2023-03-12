package pk.sufiishq.aurora.layout

import androidx.compose.foundation.BorderStroke
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import pk.sufiishq.aurora.theme.AuroraColor
import pk.sufiishq.aurora.theme.getForegroundColor
import pk.sufiishq.aurora.theme.validateBackground

@Composable
fun SICard(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.medium,
    bgColor: AuroraColor = AuroraColor.Background,
    contentColor: Color = contentColorFor(bgColor.getForegroundColor(bgColor.color()).color()),
    border: BorderStroke? = null,
    elevation: Dp = 1.dp,
    content: @Composable (fgColor: AuroraColor) -> Unit
) {
    Card(
        modifier = modifier,
        shape = shape,
        backgroundColor = bgColor.color(),
        contentColor = contentColor,
        border = border,
        elevation = elevation,
    ) {
        content(bgColor.getForegroundColor(bgColor.color()))
    }
}