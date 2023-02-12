package pk.sufiishq.app.ui.screen.admin

import android.text.TextUtils
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import io.github.esentsov.PackagePrivate
import pk.sufiishq.app.R
import pk.sufiishq.app.utils.optString
import pk.sufiishq.aurora.components.SIHeightSpace
import pk.sufiishq.aurora.components.SIText
import pk.sufiishq.aurora.components.SIWidthSpace
import pk.sufiishq.aurora.layout.SIBox
import pk.sufiishq.aurora.layout.SIRow

@PackagePrivate
@Composable
fun HighlightContactRow(
    label: String,
    contactName: String,
    contactNumber: String,
    onValueChange: (updatedName: String, updatedNumber: String) -> Unit
) {
    SIHeightSpace(value = 8)

    SIRow(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SIText(
            modifier = Modifier.weight(1f),
            text = label,
            textColor = it
        )
        SIRow(
            modifier = Modifier.weight(3f)
        ) {

            SIBox(modifier = Modifier.weight(1f)) {
                AdminTextView(
                    modifier = Modifier.fillMaxWidth(),
                    label = optString(R.string.label_name),
                    text = contactName,
                    maxLength = 20,
                    onValueChange = { value ->
                        onValueChange(value, contactNumber)
                    }
                )
            }
            SIWidthSpace(value = 8)
            SIBox(modifier = Modifier.weight(1f)) {
                AdminTextView(
                    modifier = Modifier.fillMaxWidth(),
                    label = optString(R.string.label_number_hint),
                    text = contactNumber,
                    maxLength = 11,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    onValueChange = { value ->
                        if (TextUtils.isDigitsOnly(value.trim())) {
                            onValueChange(contactName, value.trim())
                        }
                    }
                )
            }
        }
    }
}