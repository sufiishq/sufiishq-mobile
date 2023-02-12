package pk.sufiishq.app.ui.screen.admin

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.esentsov.PackagePrivate
import pk.sufiishq.app.ui.components.OutlinedTextField
import pk.sufiishq.aurora.components.SIHeightSpace

@PackagePrivate
@Composable
fun AdminTextView(
    modifier: Modifier = Modifier,
    label: String,
    text: String,
    maxLength: Int = 10000,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onValueChange: (value: String) -> Unit
) {

    SIHeightSpace(value = 8)
    OutlinedTextField(
        modifier = modifier,
        value = text,
        label = label,
        onValueChange = onValueChange,
        keyboardOptions = keyboardOptions,
        maxLength = maxLength
    )
}