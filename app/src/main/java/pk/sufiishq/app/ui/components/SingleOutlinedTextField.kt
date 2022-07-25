package pk.sufiishq.app.ui.components

import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun SingleOutlinedTextField(
    value: String,
    onValueChange: (value: String) -> Unit,
    keyboardActionDone: KeyboardActionScope.() -> Unit = {},
    label: String = "",
    isError: Boolean = false,
    maxLength: Int = Integer.MAX_VALUE
) {

    OutlinedTextField(
        value = value,
        onValueChange = {
            if (it.length <= maxLength) onValueChange(it)
        },
        keyboardActions = KeyboardActions(onDone = keyboardActionDone),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Text,
            capitalization = KeyboardCapitalization.Words
        ),
        singleLine = true,
        maxLines = 1,
        label = {
            Text(text = label)
        },
        isError = isError
    )
}