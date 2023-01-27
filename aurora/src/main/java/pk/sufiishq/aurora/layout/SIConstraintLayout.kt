package pk.sufiishq.aurora.layout

import androidx.annotation.IntRange
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintLayoutScope
import pk.sufiishq.aurora.theme.AuroraColor
import pk.sufiishq.aurora.theme.getForegroundColor
import pk.sufiishq.aurora.theme.validateBackground

@Composable
inline fun SIConstraintLayout(
    modifier: Modifier = Modifier,
    bgColor: AuroraColor = AuroraColor.Transparent,
    @IntRange(from = 0, to = 12) padding: Int = 0,
    crossinline content: @Composable ConstraintLayoutScope.(fgColor: AuroraColor) -> Unit
) {

    ConstraintLayout(
        modifier = modifier
            .background(bgColor.validateBackground().color())
            .padding(padding.coerceIn(0, 12).dp),
        content = {
            content(bgColor.getForegroundColor())
        }
    )
}