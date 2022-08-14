package pk.sufiishq.app.ui.components.dialogs

import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import pk.sufiishq.app.ui.components.SufiIshqDialog

@Composable
fun ShowCircularProgressDialog(
    showDialog: State<Boolean?>
) {
    if (showDialog.value == true) {
        SufiIshqDialog {
            CircularProgressIndicator()
        }
    }
}