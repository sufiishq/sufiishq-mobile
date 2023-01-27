package pk.sufiishq.app.ui.components.dialogs

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import pk.sufiishq.aurora.components.SICircularProgressIndicator
import pk.sufiishq.aurora.layout.SIBox
import pk.sufiishq.aurora.layout.SIDialog

@Composable
fun ShowCircularProgressDialog(showDialog: State<Boolean?>) {
    if (showDialog.value == true) {
        SIDialog {
            SIBox(modifier = Modifier.fillMaxWidth()) {
                SICircularProgressIndicator(strokeWidth = 2)
            }
        }
    }
}