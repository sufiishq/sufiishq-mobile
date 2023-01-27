package pk.sufiishq.aurora.components

import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pk.sufiishq.aurora.theme.AuroraColor
import pk.sufiishq.aurora.theme.getForegroundColor

@Composable
fun SIButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    visibility: Boolean = true,
    bgColor: AuroraColor = AuroraColor.SecondaryVariant,
    textSize: TextSize = TextSize.Small,
    defaultElevation: Int = 2
) {
    if (visibility) {
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = bgColor.color()
            ),
            modifier = modifier,
            elevation = ButtonDefaults.elevation(
                defaultElevation = defaultElevation.dp
            )
        ) {
            SIText(
                text = text,
                textSize = textSize,
                textColor = bgColor.getForegroundColor()
            )
        }
    }
}