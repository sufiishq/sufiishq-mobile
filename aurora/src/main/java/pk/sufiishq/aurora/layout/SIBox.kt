package pk.sufiishq.aurora.layout

import androidx.annotation.IntRange
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pk.sufiishq.aurora.theme.AuroraColor
import pk.sufiishq.aurora.theme.getForegroundColor
import pk.sufiishq.aurora.theme.validateBackground

@Composable
fun SIBox(
    modifier: Modifier = Modifier,
    bgColor: AuroraColor = AuroraColor.Transparent,
    @IntRange(from = 0, to = 12) padding: Int = 0,
    contentAlignment: Alignment = Alignment.Center,
    content: @Composable BoxScope.(fgColor: AuroraColor) -> Unit
) {

    Box(
        modifier = modifier
            .background(
                bgColor
                    .validateBackground()
                    .color()
            )
            .padding(padding.coerceIn(0, 12).dp),
        contentAlignment = contentAlignment
    ) {
        content(bgColor.getForegroundColor(bgColor.color()))
    }
}