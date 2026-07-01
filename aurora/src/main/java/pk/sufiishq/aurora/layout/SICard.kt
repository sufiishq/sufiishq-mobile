package pk.sufiishq.aurora.layout

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import pk.sufiishq.aurora.theme.AuroraColor
import pk.sufiishq.aurora.theme.getForegroundColor

@Composable
fun SICard(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.medium,
    bgColor: AuroraColor = AuroraColor.Background,
    contentColor: Color = contentColorFor(bgColor.getForegroundColor(bgColor.color()).color()),
    border: BorderStroke? = null,
    elevation: Dp = 1.dp,
    content: @Composable ColumnScope.(fgColor: AuroraColor) -> Unit
) {
    Card(
        modifier = modifier,
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = bgColor.color(),
            contentColor = contentColor
        ),
        border = border,
        elevation = CardDefaults.cardElevation(
            defaultElevation = elevation
        ),
    ) {
        content(bgColor.getForegroundColor(bgColor.color()))
    }
}