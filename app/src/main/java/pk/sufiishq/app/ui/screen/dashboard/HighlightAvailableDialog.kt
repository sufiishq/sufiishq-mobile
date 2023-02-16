package pk.sufiishq.app.ui.screen.dashboard

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.esentsov.PackagePrivate
import pk.sufiishq.app.R
import pk.sufiishq.app.models.Highlight
import pk.sufiishq.app.utils.addCharAtIndex
import pk.sufiishq.app.utils.contactsAsListPair
import pk.sufiishq.app.utils.launchCallIntent
import pk.sufiishq.app.utils.optString
import pk.sufiishq.aurora.components.SIButton
import pk.sufiishq.aurora.components.SIHeightSpace
import pk.sufiishq.aurora.components.SIText
import pk.sufiishq.aurora.components.TextSize
import pk.sufiishq.aurora.layout.SIDialog
import pk.sufiishq.aurora.layout.SIRow

@PackagePrivate
@Composable
fun HighlightAvailableDialog(
    showDialog: MutableState<Highlight?>
) {

    showDialog.value?.apply {

        SIDialog(
            modifier = Modifier.heightIn(max = 600.dp),
            title = optString(R.string.label_highlight),
            onDismissRequest = {
                showDialog.value = null
            }
        ) {

            val context = LocalContext.current
            val highlight = this@apply

            highlight.title?.apply {

                SIText(
                    text = highlight.title,
                    textColor = it,
                    fontWeight = FontWeight.Bold
                )
                SIHeightSpace(value = 12)
            }

            SIText(
                modifier = Modifier
                    .heightIn(max = 250.dp)
                    .verticalScroll(rememberScrollState()),
                text = highlight.detail,
                textColor = it
            )
            Divider()
            SIHeightSpace(value = 12)

            highlight
                .contactsAsListPair()
                ?.forEach { pair ->

                    SIRow(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) { textColor ->
                        SIText(
                            modifier = Modifier.weight(1f),
                            text = pair.first,
                            textColor = textColor,
                            textSize = TextSize.Small
                        )
                        SIButton(
                            text = pair.second.addCharAtIndex('-', 4),
                            onClick = {
                                context.launchCallIntent(pair.second)
                            }
                        )
                    }
                }
        }
    }
}