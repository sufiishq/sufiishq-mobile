package pk.sufiishq.aurora.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.sp
import pk.sufiishq.aurora.theme.AuroraColor

@Composable
fun SITextField(
    searchText: State<String>,
    textColor: AuroraColor,
    placeholderText: String? = null,
    onValueChange: (String) -> Unit,
) {
    val focusManager = LocalFocusManager.current

    val colors = TextFieldDefaults.outlinedTextFieldColors(
        textColor = textColor.color(),
        cursorColor = textColor.color(),
        unfocusedBorderColor = textColor.color().copy(alpha = 0.3f),
        focusedBorderColor = textColor.color().copy(alpha = 0.5f),
        unfocusedLabelColor = textColor.color().copy(alpha = 0.3f),
        focusedLabelColor = textColor.color().copy(alpha = 0.7f),
        placeholderColor = textColor.color().copy(0.5f)
    )

    TextField(
        value = searchText.value,
        onValueChange = onValueChange,
        placeholder = {
            if (placeholderText != null) {
                SIText(
                    text = "Search in $placeholderText",
                    textColor = textColor,
                    textSize = TextSize.Small
                )
            }
        },
        singleLine = true,
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        maxLines = 1,
        colors = colors,
        textStyle = TextStyle(
            fontSize = TextSize.Small.value.sp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .background(AuroraColor.PrimaryVariant.color())
    )
}