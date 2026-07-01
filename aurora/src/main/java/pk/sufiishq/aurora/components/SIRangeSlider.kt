package pk.sufiishq.aurora.components

import androidx.compose.material3.RangeSlider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import pk.sufiishq.aurora.theme.AuroraColor

@Composable
fun SIRangeSlider(
    modifier: Modifier = Modifier,
    colors: SliderColors =
        SliderDefaults.colors(
            thumbColor = AuroraColor.Secondary.color(),
            activeTickColor = AuroraColor.SecondaryContainer.color(),
            activeTrackColor = AuroraColor.SecondaryContainer.color()
        ),
    value: ClosedFloatingPointRange<Float>,
    onValueChange: (ClosedFloatingPointRange<Float>) -> Unit,
    enabled: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    onValueChangeFinished: (() -> Unit)? = null,
) {

    RangeSlider(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        enabled = enabled,
        valueRange = valueRange,
        steps = steps,
        onValueChangeFinished = onValueChangeFinished,
        colors = colors
    )
}