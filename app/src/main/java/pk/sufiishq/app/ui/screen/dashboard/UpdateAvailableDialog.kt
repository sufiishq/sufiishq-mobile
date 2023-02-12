package pk.sufiishq.app.ui.screen.dashboard

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import io.github.esentsov.PackagePrivate
import pk.sufiishq.app.R
import pk.sufiishq.app.data.providers.MainDataProvider
import pk.sufiishq.app.utils.optString
import pk.sufiishq.aurora.components.SIButton
import pk.sufiishq.aurora.components.SIHeightSpace
import pk.sufiishq.aurora.components.SIText
import pk.sufiishq.aurora.components.TextSize
import pk.sufiishq.aurora.layout.SIDialog

@PackagePrivate
@Composable
fun UpdateAvailableDialog(
    isUpdateAvailable: MutableState<Boolean>,
    mainDataProvider: MainDataProvider
) {

    if (isUpdateAvailable.value) {
        SIDialog(
            onDismissRequest = {
                isUpdateAvailable.value = false
            }
        ) { textColor ->
            SIText(
                modifier = Modifier.fillMaxWidth(),
                text = optString(R.string.app_name),
                textColor = textColor,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            SIHeightSpace(value = 16)
            SIText(
                text = optString(R.string.msg_update_available),
                textColor = textColor,
                textSize = TextSize.Regular
            )
            SIHeightSpace(value = 12)
            SIButton(
                modifier = Modifier.fillMaxWidth(),
                text = optString(R.string.label_update_now),
                textSize = TextSize.Regular,
                onClick = {
                    mainDataProvider.handleUpdate()
                }
            )
        }
    }
}