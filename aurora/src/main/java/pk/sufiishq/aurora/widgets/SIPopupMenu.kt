package pk.sufiishq.aurora.widgets

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import pk.sufiishq.aurora.components.SIDropdownMenuItem
import pk.sufiishq.aurora.components.SIIcon
import pk.sufiishq.aurora.layout.SIBox
import pk.sufiishq.aurora.layout.SIDropdownMenu
import pk.sufiishq.aurora.models.DataMenuItem
import pk.sufiishq.aurora.theme.AuroraColor
import pk.sufiishq.aurora.theme.validateForeground

@Composable
fun SIPopupMenu(
    @DrawableRes resId: Int,
    iconTint: AuroraColor? = null,
    data: List<DataMenuItem>,
    onClick: (DataMenuItem) -> Unit
) {

    val isExpanded = remember { mutableStateOf(false) }

    SIBox { textColor ->
        SIIcon(
            resId = resId,
            tint = iconTint?.validateForeground() ?: textColor,
            onClick = {
                isExpanded.value = !isExpanded.value
            }
        )

        SIPopupMenu(
            isExpanded = isExpanded,
            data = data,
            onClick = onClick
        )
    }
}

@Composable
fun SIPopupMenu(
    isExpanded: MutableState<Boolean>,
    data: List<DataMenuItem>,
    onClick: (DataMenuItem) -> Unit
) {

    SIDropdownMenu(
        isExpand = isExpanded,
        onHide = {
            isExpanded.value = false
        }
    ) { textColor ->
        data.map {
            SIDropdownMenuItem(
                label = it.label,
                resId = it.resId,
                labelColor = textColor,
                iconTint = it.iconTint,
                onClick = {
                    isExpanded.value = false
                    onClick(it)
                }
            )
        }
    }
}