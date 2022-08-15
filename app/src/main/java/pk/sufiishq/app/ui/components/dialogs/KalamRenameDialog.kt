package pk.sufiishq.app.ui.components.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pk.sufiishq.app.core.event.dispatcher.EventDispatcher
import pk.sufiishq.app.core.event.events.KalamEvents
import pk.sufiishq.app.models.Kalam
import pk.sufiishq.app.ui.components.OutlinedTextFieldValidation
import pk.sufiishq.app.utils.KALAM_TITLE_LENGTH
import pk.sufiishq.app.utils.checkValue
import pk.sufiishq.app.utils.ifNotEmpty
import pk.sufiishq.app.utils.rem

@Composable
fun KalamRenameDialog(
    kalamState: State<Kalam?>,
    eventDispatcher: EventDispatcher
) {

    kalamState.value?.apply {

        val kalamTitle = rem(title)
        val error = rem(title.checkValue("", "Title cannot be empty"))

        SufiIshqDialog {

            Text(text = "Rename Kalam", style = MaterialTheme.typography.subtitle1)

            OutlinedTextFieldValidation(
                value = kalamTitle.value,
                onValueChange = {
                    kalamTitle.value = it
                    error.value = kalamTitle.value.checkValue("", "Title cannot be empty")
                },
                keyboardActions = KeyboardActions(onDone = {
                    kalamTitle.value.ifNotEmpty {
                        title = it
                        updateKalam(kalam = this@apply, eventDispatcher = eventDispatcher)
                    }
                }),
                modifier = Modifier.padding(top = 8.dp),
                label = {
                    Text(text = "Title")
                },
                error = error.value,
                maxLength = KALAM_TITLE_LENGTH
            )

            // BUTTONS
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = {
                    eventDispatcher.dispatch(KalamEvents.ShowKalamRenameDialog(null))
                }) {
                    Text(text = "Cancel")
                }
                TextButton(onClick = {
                    kalamTitle.value.ifNotEmpty {
                        title = it
                        updateKalam(this@apply, eventDispatcher)
                    }
                }) {
                    Text(text = "Rename")
                }
            }
        }
    }
}

private fun updateKalam(
    kalam: Kalam,
    eventDispatcher: EventDispatcher
) {
    eventDispatcher.dispatch(

        // update kalam
        KalamEvents.UpdateKalam(kalam),

        // hide dialog
        KalamEvents.ShowKalamRenameDialog(null)
    )
}