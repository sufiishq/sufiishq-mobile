package pk.sufiishq.aurora.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
    bgColor: AuroraColor = AuroraColor.SecondaryContainer,
    textSize: TextSize = TextSize.Small,
    defaultElevation: Int = 2,
    @DrawableRes leadingIcon: Int? = null
) {
    if (visibility) {
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = bgColor.color()
            ),
            modifier = modifier,
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = defaultElevation.dp
            )
        ) {

            if (leadingIcon != null) {
                SIIcon(
                    modifier = Modifier.padding(end = 12.dp),
                    resId = leadingIcon,
                    tint = bgColor.getForegroundColor(bgColor.color())
                )
            }

            SIText(
                text = text,
                textSize = textSize,
                textColor = bgColor.getForegroundColor(bgColor.color()),
            )
        }
    }
}