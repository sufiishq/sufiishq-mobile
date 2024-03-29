package pk.sufiishq.aurora.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.DropdownMenu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.PopupProperties
import pk.sufiishq.aurora.theme.AuroraColor
import pk.sufiishq.aurora.theme.getForegroundColor
import pk.sufiishq.aurora.theme.validateBackground

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SIDropdownMenu(
    modifier: Modifier = Modifier,
    isExpand: MutableState<Boolean>,
    onHide: (() -> Unit)? = null,
    bgColor: AuroraColor = AuroraColor.Background,
    content: @Composable ColumnScope.(fgColor: AuroraColor) -> Unit
) {

    DropdownMenu(
        modifier = modifier.background(bgColor.validateBackground().color()),
        expanded = isExpand.value,
        onDismissRequest = {
            isExpand.value = false
            onHide?.invoke()
        },
        properties = PopupProperties(
            usePlatformDefaultWidth = true
        )
    ) {
        content(bgColor.getForegroundColor(bgColor.color()))
    }
}