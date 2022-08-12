package pk.sufiishq.app.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp


@Composable
fun OutlinedTextFieldValidation(
    value: String,
    onValueChange: (String) -> Unit,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null,
    error: String = "",
    maxLength: Int = Int.MAX_VALUE
) {

    Column {
        OutlinedTextField(
            enabled = true,
            readOnly = false,
            value = value,
            onValueChange = {
                if (it.length <= maxLength) onValueChange(it)
            },
            modifier = modifier.fillMaxWidth(),
            singleLine = true,
            textStyle = LocalTextStyle.current,
            placeholder = null,
            leadingIcon = null,
            label = label,
            isError = error.isNotEmpty(),
            visualTransformation = VisualTransformation.None,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Words
            ),
            keyboardActions = keyboardActions,
            maxLines = 1,
            interactionSource = remember { MutableInteractionSource() },
            shape = MaterialTheme.shapes.small,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                disabledTextColor = Color.Black
            )
        )
        if (error.isNotEmpty()) {
            Text(
                text = error,
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(start = 16.dp, top = 0.dp)
            )
        }
    }
}