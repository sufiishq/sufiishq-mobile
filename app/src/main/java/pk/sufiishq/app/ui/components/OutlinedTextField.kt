package pk.sufiishq.app.ui.components

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import pk.sufiishq.app.utils.rem
import pk.sufiishq.aurora.components.SIOutlinedTextField

@Composable
fun OutlinedTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (value: String) -> Unit,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        imeAction = ImeAction.Done,
        keyboardType = KeyboardType.Text,
        capitalization = KeyboardCapitalization.Words
    ),
    label: String? = null,
    emptyFieldError: String? = null,
    maxLength: Int = Integer.MAX_VALUE,
    maxLines: Int = 1,
    singleLine: Boolean = true
) {

    val isError = rem(false)

    SIOutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = { text ->
            if (text.length <= maxLength) {
                onValueChange(text)

                emptyFieldError?.let {
                    isError.value = text.trim().isEmpty()
                }
            }
        },
        keyboardActions = keyboardActions,
        keyboardOptions = keyboardOptions,
        singleLine = singleLine,
        maxLines = maxLines,
        label = label,
        isError = isError,
        errorText = emptyFieldError
    )
}