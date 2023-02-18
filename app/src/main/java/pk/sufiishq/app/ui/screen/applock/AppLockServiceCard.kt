/*
 * Copyright 2022-2023 SufiIshq
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
import pk.sufiishq.app.R
import pk.sufiishq.app.feature.applock.model.AutoLockDuration
import pk.sufiishq.app.utils.instantAutoLockDuration
import pk.sufiishq.app.utils.extention.optString
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
    actionButtonClick: suspend CoroutineScope.() -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()

    AppLockServiceCard(
        modifier = modifier,
        infoDrawableId = infoDrawableId,
        title = title,
        detail = detail,
    ) {
        SIButton(
            modifier = Modifier.align(Alignment.BottomEnd),
            text = actionButtonTitle,
            onClick = { coroutineScope.launch { actionButtonClick(this) } },
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
    onCheckedChanged: suspend CoroutineScope.(isChecked: Boolean) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val check = rem(false)
    check.value = isCheck

    AppLockServiceCard(
        modifier = modifier,
        infoDrawableId = infoDrawableId,
        title = title,
        detail = detail,
    ) {
        Switch(
            modifier = Modifier.align(Alignment.BottomEnd),
            checked = check.value,
            onCheckedChange = {
                coroutineScope.launch {
                    check.value = it
                    onCheckedChanged(this, check.value)
                }
            },
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
    onDurationChanged: suspend CoroutineScope.(autoLockDuration: AutoLockDuration) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val data = rem(getAutoLockDurations())
    val selectedDuration = rem(data.value[selectedDurationCode])
    val isExpanded = rem(false)

    AppLockServiceCard(
        modifier = modifier,
        infoDrawableId = infoDrawableId,
        title = title,
        detail = detail,
    ) {
        SIButton(
            modifier = Modifier.align(Alignment.CenterEnd),
            text = selectedDuration.value.label,
            onClick = { isExpanded.value = true },
        )
        SIBox(modifier = Modifier.align(Alignment.BottomEnd)) {
            SIPopupMenu(
                isExpanded = isExpanded,
                data = data.value.reversed(),
                onClick = {
                    coroutineScope.launch {
                        selectedDuration.value = it as AutoLockDuration
                        onDurationChanged(it)
                    }
                },
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
    content: @Composable BoxScope.(fgColor: AuroraColor) -> Unit,
) {
    SIRow(
        modifier = modifier.fillMaxWidth().padding(16.dp),
        bgColor = AuroraColor.Background,
        radius = 4,
    ) { textColor ->
        SIImage(resId = infoDrawableId)
        SIWidthSpace(value = 12)
        SIColumn(modifier = Modifier.fillMaxWidth()) {
            SIText(text = title, textColor = textColor, fontWeight = FontWeight.Bold)
            SIHeightSpace(value = 6)
            SIText(
                text = detail,
                textColor = textColor,
                textSize = TextSize.Small,
            )
            SIHeightSpace(value = 6)
            SIBox(modifier = Modifier.fillMaxWidth()) { content(this, it) }
        }
    }
}

@Composable
private fun getAutoLockDurations(): List<AutoLockDuration> {
    return listOf(
        instantAutoLockDuration(optString(R.string.label_instant)),
        AutoLockDuration(1, optString(R.string.label_30_seconds), 30000),
        AutoLockDuration(2, optString(R.string.label_1_minute), 60000),
        AutoLockDuration(3, optString(R.string.label_3_minutes), 180000),
        AutoLockDuration(4, optString(R.string.label_5_minutes), 300000),
        AutoLockDuration(5, optString(R.string.label_10_minutes), 600000),
        AutoLockDuration(6, optString(R.string.label_30_minutes), 1800000),
        AutoLockDuration(7, optString(R.string.label_1_hour), 3600000),
    )
}
