package pk.sufiishq.aurora.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.size
import androidx.compose.material.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pk.sufiishq.aurora.theme.AuroraColor

@Composable
fun SIDropdownMenuItem(
    label: String,
    labelColor: AuroraColor,
    @DrawableRes resId: Int? = null,
    iconTint: AuroraColor? = labelColor,
    onClick: () -> Unit
) {
    DropdownMenuItem(onClick = onClick) {

        resId?.apply {
            SIIcon(
                resId = resId,
                tint = iconTint,
                modifier = Modifier.size(20.dp)
            )

            SIWidthSpace(value = 10)
        }
        SIText(
            text = label,
            textColor = labelColor,
            textSize = TextSize.Small
        )
    }
}