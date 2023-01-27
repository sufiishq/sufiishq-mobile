package pk.sufiishq.app.ui.components.dialogs

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pk.sufiishq.app.core.event.events.KalamEvents
import pk.sufiishq.app.models.Kalam
import pk.sufiishq.app.ui.components.SingleOutlinedTextField
import pk.sufiishq.app.utils.KALAM_TITLE_LENGTH
import pk.sufiishq.app.utils.checkValue
import pk.sufiishq.app.utils.dispatch
import pk.sufiishq.app.utils.ifNotEmpty
import pk.sufiishq.app.utils.rem
import pk.sufiishq.aurora.components.SIText
import pk.sufiishq.aurora.layout.SIDialog

@Composable
fun KalamRenameDialog(kalamState: State<Kalam?>) {

    kalamState.value?.apply {

        val kalamTitle = rem(title)
        val errorText = rem(title.checkValue("", "Title cannot be empty"))
        val isError = rem(false)

        SIDialog(
            onNoText = "Cancel",
            onNoClick = ::hideDialog,
            onYesText = "Rename",
            onYesClick = {
                kalamTitle.value.ifNotEmpty {
                    updateKalam(kalam = this@apply, it)
                }
            }
        ) { textColor ->

            SIText(
                text = "Rename Kalam",
                textColor = textColor
            )

            SingleOutlinedTextField(
                value = kalamTitle.value,
                onValueChange = {
                    kalamTitle.value = it
                    errorText.value =
                        kalamTitle.value.trim().checkValue("", "Title cannot be empty")
                    isError.value = errorText.value.isNotEmpty()
                },
                keyboardActions = KeyboardActions(onDone = {
                    kalamTitle.value.ifNotEmpty {
                        updateKalam(kalam = this@apply, it)
                    }
                }),
                modifier = Modifier.padding(top = 8.dp),
                label = "Title",
                isError = isError,
                errorText = errorText.value,
                maxLength = KALAM_TITLE_LENGTH
            )
        }
    }
}

private fun hideDialog() {
    KalamEvents.ShowKalamRenameDialog(null).dispatch()
}

private fun updateKalam(kalam: Kalam, newTitle: String) {
    kalam.title = newTitle
    KalamEvents.UpdateKalam(kalam).dispatch()
    hideDialog()
}