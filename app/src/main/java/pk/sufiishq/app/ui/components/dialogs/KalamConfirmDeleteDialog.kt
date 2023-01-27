package pk.sufiishq.app.ui.components.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import pk.sufiishq.app.core.event.events.KalamEvents
import pk.sufiishq.app.models.KalamDeleteItem
import pk.sufiishq.app.utils.dispatch
import pk.sufiishq.aurora.components.SIText
import pk.sufiishq.aurora.layout.SIDialog

@Composable
fun KalamConfirmDeleteDialog(
    kalamDeleteItem: State<KalamDeleteItem?>
) {

    kalamDeleteItem.value
        ?.apply {

            SIDialog(
                onNoText = "NO",
                onNoClick = ::hideDialog,
                onYesText = "YES",
                onYesClick = {
                    hideDialog()
                    KalamEvents.DeleteKalam(this@apply).dispatch()
                },
                onDismissRequest = ::hideDialog
            ) { textColor ->

                SIText(
                    text = buildAnnotatedString {
                        append("Are you sure you want to delete ")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(kalam.title)
                        }
                    },
                    textColor = textColor
                )
            }
        }
}

private fun hideDialog() {
    KalamEvents.ShowKalamConfirmDeleteDialog(null).dispatch()
}