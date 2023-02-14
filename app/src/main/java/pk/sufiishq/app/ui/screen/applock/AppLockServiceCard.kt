package pk.sufiishq.app.ui.screen.applock

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.esentsov.PackagePrivate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import pk.sufiishq.app.models.AutoLockDuration
import pk.sufiishq.app.utils.instantAutoLockDuration
import pk.sufiishq.app.utils.rem
import pk.sufiishq.aurora.components.SIButton
import pk.sufiishq.aurora.components.SIHeightSpace
import pk.sufiishq.aurora.components.SIImage
import pk.sufiishq.aurora.components.SIText
import pk.sufiishq.aurora.components.SIWidthSpace
import pk.sufiishq.aurora.components.TextSize
import pk.sufiishq.aurora.layout.SIBox
import pk.sufiishq.aurora.layout.SIColumn
import pk.sufiishq.aurora.layout.SIRow
import pk.sufiishq.aurora.theme.AuroraColor
import pk.sufiishq.aurora.widgets.SIPopupMenu

@PackagePrivate
@Composable
fun AppLockServiceCardWithButton(
    modifier: Modifier = Modifier,
    @DrawableRes infoDrawableId: Int,
    title: String,
    detail: String,
    actionButtonTitle: String,
    actionButtonClick: suspend CoroutineScope.() -> Unit
) {

    val coroutineScope = rememberCoroutineScope()

    AppLockServiceCard(
        modifier = modifier,
        infoDrawableId = infoDrawableId,
        title = title,
        detail = detail
    ) {
        SIButton(
            modifier = Modifier.align(Alignment.BottomEnd),
            text = actionButtonTitle,
            onClick = {
                coroutineScope.launch {
                    actionButtonClick(this)
                }
            }
        )
    }
}

@PackagePrivate
@Composable
fun AppLockServiceCardWithToggle(
    modifier: Modifier = Modifier,
    @DrawableRes infoDrawableId: Int,
    title: String,
    detail: String,
    isCheck: Boolean,
    onCheckedChanged: suspend CoroutineScope.(isChecked: Boolean) -> Unit
) {

    val coroutineScope = rememberCoroutineScope()
    val check = rem(false)
    check.value = isCheck

    AppLockServiceCard(
        modifier = modifier,
        infoDrawableId = infoDrawableId,
        title = title,
        detail = detail
    ) {
        Switch(
            modifier = Modifier.align(Alignment.BottomEnd),
            checked = check.value,
            onCheckedChange = {
                coroutineScope.launch {
                    check.value = it
                    onCheckedChanged(this, check.value)
                }
            }
        )
    }
}

@PackagePrivate
@Composable
fun AppLockDurationServiceCard(
    modifier: Modifier = Modifier,
    @DrawableRes infoDrawableId: Int,
    title: String,
    detail: String,
    selectedDurationCode: Int = 0,
    onDurationChanged: suspend CoroutineScope.(autoLockDuration: AutoLockDuration) -> Unit
) {

    val coroutineScope = rememberCoroutineScope()
    val data = rem(getAutoLockDurations())
    val selectedDuration = rem(data.value[selectedDurationCode])
    val isExpanded = rem(false)

    AppLockServiceCard(
        modifier = modifier,
        infoDrawableId = infoDrawableId,
        title = title,
        detail = detail
    ) {
        SIButton(
            modifier = Modifier.align(Alignment.CenterEnd),
            text = selectedDuration.value.label,
            onClick = {
                isExpanded.value = true
            }
        )
        SIBox(modifier = Modifier.align(Alignment.BottomEnd)) {
            SIPopupMenu(
                isExpanded = isExpanded,
                data = data.value,
                onClick = {
                    coroutineScope.launch {
                        selectedDuration.value = it as AutoLockDuration
                        onDurationChanged(it)
                    }
                }
            )
        }
    }
}

@PackagePrivate
@Composable
fun AppLockServiceCard(
    modifier: Modifier = Modifier,
    @DrawableRes infoDrawableId: Int,
    title: String,
    detail: String,
    content: @Composable BoxScope.(fgColor: AuroraColor) -> Unit
) {

    SIRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        bgColor = AuroraColor.Background,
        radius = 4
    ) { textColor ->
        SIImage(resId = infoDrawableId)
        SIWidthSpace(value = 12)
        SIColumn(modifier = Modifier.fillMaxWidth()) {
            SIText(text = title, textColor = textColor, fontWeight = FontWeight.Bold)
            SIHeightSpace(value = 6)
            SIText(
                text = detail,
                textColor = textColor,
                textSize = TextSize.Small
            )
            SIHeightSpace(value = 6)
            SIBox(modifier = Modifier.fillMaxWidth()) {
                content(this, it)
            }
        }
    }
}

private fun getAutoLockDurations(): List<AutoLockDuration> {
    return listOf(
        instantAutoLockDuration(),
        AutoLockDuration(1, "30 Seconds", 30000),
        AutoLockDuration(2, "1 Minute", 60000),
        AutoLockDuration(3, "3 Minutes", 180000),
        AutoLockDuration(4, "5 Minutes", 300000),
        AutoLockDuration(5, "10 Minutes", 600000),
        AutoLockDuration(6, "30 Minutes", 1800000),
        AutoLockDuration(7, "1 Hour", 3600000),
    )
}