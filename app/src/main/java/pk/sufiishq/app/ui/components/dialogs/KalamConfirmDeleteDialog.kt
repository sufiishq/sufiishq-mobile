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
import pk.sufiishq.app.core.event.dispatcher.EventDispatcher
import pk.sufiishq.app.core.event.events.KalamEvents
import pk.sufiishq.app.models.KalamDeleteItem

@Composable
fun KalamConfirmDeleteDialog(
    kalamDeleteItem: State<KalamDeleteItem?>,
    eventDispatcher: EventDispatcher
) {

    kalamDeleteItem.value
        ?.apply {
            SufiIshqDialog(
                onDismissRequest = {
                    hideDialog(eventDispatcher)
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
                        hideDialog(eventDispatcher)
                    }) {
                        Text(text = "NO")
                    }
                    TextButton(onClick = {
                        hideDialog(eventDispatcher)
                        eventDispatcher.dispatch(
                            KalamEvents.DeleteKalam(this@apply)
                        )
                    }) {
                        Text(text = "YES")
                    }
                }
            }
        }
}

private fun hideDialog(eventDispatcher: EventDispatcher) {
    eventDispatcher.dispatch(KalamEvents.ShowKalamConfirmDeleteDialog(null))
}