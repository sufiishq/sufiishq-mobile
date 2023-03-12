package pk.sufiishq.aurora.layout

import androidx.annotation.IntRange
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pk.sufiishq.aurora.theme.AuroraColor
import pk.sufiishq.aurora.theme.getForegroundColor
import pk.sufiishq.aurora.theme.validateBackground

@Composable
fun SIColumn(
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    bgColor: AuroraColor = AuroraColor.Transparent,
    @IntRange(from = 0, to = 12) padding: Int = 0,
    content: @Composable ColumnScope.(fgColor: AuroraColor) -> Unit
) {
    Column(
        modifier = modifier
            .background(
                bgColor
                    .validateBackground()
                    .color()
            )
            .padding(padding.coerceIn(0, 12).dp),
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment,
        content = {
            content(bgColor.getForegroundColor(bgColor.color()))
        }
    )
}