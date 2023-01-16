package pk.sufiishq.app.ui.components.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import pk.sufiishq.app.core.event.events.KalamEvents
import pk.sufiishq.app.models.KalamDeleteItem
import pk.sufiishq.app.utils.dispatch

@Composable
fun KalamConfirmDeleteDialog(
    kalamDeleteItem: State<KalamDeleteItem?>
) {

    kalamDeleteItem.value
        ?.apply {

            SufiIshqDialog(
                onDismissRequest = {
                    hideDialog()
                }
            ) {
                Text(
                    buildAnnotatedString {
                        append("Are you sure you want to delete ")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(kalam.title)
                        }
                    }
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = {
                        hideDialog()
                    }) {
                        Text(text = "NO")
                    }
                    TextButton(onClick = {
                        hideDialog()
                        KalamEvents.DeleteKalam(this@apply).dispatch()
                    }) {
                        Text(text = "YES")
                    }
                }
            }
        }
}

private fun hideDialog() {
    KalamEvents.ShowKalamConfirmDeleteDialog(null).dispatch()
}