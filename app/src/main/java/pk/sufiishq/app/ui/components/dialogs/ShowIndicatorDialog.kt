package pk.sufiishq.app.ui.components.dialogs

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pk.sufiishq.aurora.components.SICircularProgressIndicator
import pk.sufiishq.aurora.layout.SIBox
import pk.sufiishq.aurora.layout.SIDialog

@Composable
fun ShowIndicatorDialog() {
    SIDialog(modifier = Modifier.width(170.dp)) {
        SIBox(modifier = Modifier.fillMaxWidth()) {
            SICircularProgressIndicator(strokeWidth = 2)
        }
    }
}