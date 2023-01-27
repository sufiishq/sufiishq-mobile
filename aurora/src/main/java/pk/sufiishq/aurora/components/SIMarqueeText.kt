package pk.sufiishq.aurora.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import pk.sufiishq.aurora.theme.AuroraColor

@Composable
fun SIMarqueeText(
    text: String,
    textColor: AuroraColor,
    backgroundColor: AuroraColor = AuroraColor.Transparent,
    textSize: TextSize = TextSize.Regular,
    modifier: Modifier = Modifier
) {

    MarqueeText(
        modifier = modifier,
        color = textColor.color(),
        fontSize = textSize.value.sp,
        text = text,
        gradientEdgeColor = backgroundColor.color()
    )
}