package pk.sufiishq.app.ui.components.dialogs

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import pk.sufiishq.aurora.components.SIButton
import pk.sufiishq.aurora.components.SIHeightSpace
import pk.sufiishq.aurora.components.SIText
import pk.sufiishq.aurora.components.TextSize
import pk.sufiishq.aurora.layout.SIDialog

@Composable
fun UpdateAvailableDialog(
    isUpdateAvailable: MutableState<Boolean>
) {

    if (isUpdateAvailable.value) {
        SIDialog(
            onDismissRequest = {
                isUpdateAvailable.value = false
            }
        ) { textColor ->
            SIText(
                modifier = Modifier.fillMaxWidth(),
                text = "SufiIshq",
                textColor = textColor,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            SIHeightSpace(value = 16)
            SIText(
                text = "New update available now",
                textColor = textColor,
                textSize = TextSize.Regular
            )
            SIHeightSpace(value = 12)
            SIButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Update Now",
                textSize = TextSize.Regular,
                onClick = {

                }
            )
        }
    }
}