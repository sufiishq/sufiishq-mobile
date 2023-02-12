package pk.sufiishq.app.ui.components.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import pk.sufiishq.app.R
import pk.sufiishq.app.utils.optString
import pk.sufiishq.aurora.components.SIText
import pk.sufiishq.aurora.layout.SIDialog

class ConfirmDialogParam(val message: String, val onDismissed: () -> Unit = {}, val onConfirmed: () -> Unit)

@Composable
fun ConfirmationDialog(
    state: MutableState<ConfirmDialogParam?>
) {
    state.value?.apply {

        SIDialog(
            onDismissRequest = {
                onDismissed()
            },
            onNoText = optString(R.string.label_no),
            onNoClick = {
                onDismissed()
                state.value = null
            },
            onYesText = optString(R.string.label_yes),
            onYesClick = {
                state.value = null
                onConfirmed()
            }
        ) {
            SIText(
                text = message,
                textColor = it
            )
        }
    }
}