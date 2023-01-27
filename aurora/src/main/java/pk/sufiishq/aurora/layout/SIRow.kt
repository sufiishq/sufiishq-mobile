package pk.sufiishq.aurora.layout

import androidx.annotation.IntRange
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pk.sufiishq.aurora.theme.AuroraColor
import pk.sufiishq.aurora.theme.getForegroundColor
import pk.sufiishq.aurora.theme.validateBackground

@Composable
fun SIRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    bgColor: AuroraColor = AuroraColor.Transparent,
    @IntRange(from = 0, to = 12) padding: Int = 0,
    content: @Composable RowScope.(fgColor: AuroraColor) -> Unit
) {
    Row(modifier = modifier
        .background(
            bgColor
                .validateBackground()
                .color()
        )
        .padding(padding.coerceIn(0, 12).dp),
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment,
        content = {
            content(bgColor.getForegroundColor())
        })
}