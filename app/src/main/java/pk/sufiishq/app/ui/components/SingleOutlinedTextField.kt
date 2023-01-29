package pk.sufiishq.app.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import pk.sufiishq.aurora.components.SIOutlinedTextField

@Composable
fun SingleOutlinedTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (value: String) -> Unit,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    label: String? = null,
    isError: State<Boolean> = mutableStateOf(false),
    errorText: String? = null,
    maxLength: Int = Integer.MAX_VALUE
) {

    SIOutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = value,
        onValueChange = {
            if (it.length <= maxLength) onValueChange(it)
        },
        keyboardActions = keyboardActions,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Text,
            capitalization = KeyboardCapitalization.Words
        ),
        singleLine = true,
        maxLines = 1,
        label = label,
        isError = isError,
        errorText = errorText
    )
}