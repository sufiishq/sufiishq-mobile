package pk.sufiishq.aurora.components

import androidx.compose.material.Slider
import androidx.compose.material.SliderColors
import androidx.compose.material.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex
import pk.sufiishq.aurora.theme.AuroraColor

@Composable
fun SISlider(
    modifier: Modifier,
    colors: SliderColors =
        SliderDefaults.colors(
            thumbColor = AuroraColor.Secondary.color(),
            activeTickColor = AuroraColor.SecondaryVariant.color(),
            activeTrackColor = AuroraColor.SecondaryVariant.color()
        ),
    value: Float = 0f,
    valueRange: ClosedFloatingPointRange<Float> = 0f..100f,
    enabled: Boolean = true,
    onValueChange: (Float) -> Unit = {},
    onValueChangeFinished: (() -> Unit)? = null
) {
    Slider(
        modifier = modifier.zIndex(100f),
        colors = colors,
        value = value,
        valueRange = valueRange,
        enabled = enabled,
        onValueChange = onValueChange,
        onValueChangeFinished = onValueChangeFinished
    )
}