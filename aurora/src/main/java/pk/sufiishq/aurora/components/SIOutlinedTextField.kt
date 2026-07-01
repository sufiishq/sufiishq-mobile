package pk.sufiishq.aurora.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDirection
import pk.sufiishq.aurora.layout.SIColumn
import pk.sufiishq.aurora.theme.AuroraColor

@Composable
fun SIOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current.copy(
        textDirection = TextDirection.Content,
    ),
    label: String? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: State<Boolean> = mutableStateOf(false),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = MaterialTheme.shapes.small,
    textColor: AuroraColor = AuroraColor.OnPrimary,
    errorText: String? = null
) {

    val colors = TextFieldDefaults.colors(
        focusedTextColor = textColor.color(),
        unfocusedTextColor = textColor.color(),

        cursorColor = textColor.color(),

        unfocusedIndicatorColor = textColor.color().copy(alpha = 0.3f),
        focusedIndicatorColor = AuroraColor.SecondaryContainer.color().copy(alpha = 0.5f),

        unfocusedLabelColor = textColor.color().copy(alpha = 0.3f),
        focusedLabelColor = AuroraColor.SecondaryContainer.color().copy(alpha = 0.7f),

        unfocusedPlaceholderColor = textColor.color().copy(alpha = 0.5f),
        focusedPlaceholderColor = textColor.color().copy(alpha = 0.5f),

        focusedContainerColor = AuroraColor.Background.color(),
        unfocusedContainerColor = AuroraColor.Background.color()
    )

    val customTextSelectionColors = TextSelectionColors(
        handleColor = AuroraColor.SecondaryContainer.color(),
        backgroundColor = AuroraColor.SecondaryContainer.color().copy(alpha = 0.4f)
    )

    SIColumn {

        CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = modifier,
                enabled = enabled,
                readOnly = readOnly,
                textStyle = textStyle,
                label = {
                    label?.run {
                        SIText(
                            text = label,
                            textColor = textColor,
                            textSize = TextSize.Small
                        )
                    }
                },
                placeholder = placeholder,
                leadingIcon = leadingIcon,
                trailingIcon = trailingIcon,
                isError = isError.value,
                visualTransformation = visualTransformation,
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions,
                singleLine = singleLine,
                maxLines = maxLines,
                interactionSource = interactionSource,
                shape = shape,
                colors = colors
            )
        }

        SIHeightSpace(value = 4)

        if (isError.value && errorText != null && errorText.isNotEmpty()) {
            SIText(
                text = errorText,
                textColor = AuroraColor.OnError,
                textSize = TextSize.Small,
            )
        }
    }
}