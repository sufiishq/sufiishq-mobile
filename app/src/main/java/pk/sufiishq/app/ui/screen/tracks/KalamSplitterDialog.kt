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

package pk.sufiishq.app.ui.screen.tracks

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.esentsov.PackagePrivate
import pk.sufiishq.app.feature.kalam.controller.KalamController
import pk.sufiishq.app.feature.kalam.splitter.SplitKalamInfo
import pk.sufiishq.app.feature.kalam.splitter.SplitStatus
import pk.sufiishq.app.ui.components.OutlinedTextField
import pk.sufiishq.app.utils.ImageRes
import pk.sufiishq.app.utils.KALAM_TITLE_LENGTH
import pk.sufiishq.app.utils.TextRes
import pk.sufiishq.app.utils.extention.formatTime
import pk.sufiishq.app.utils.extention.optString
import pk.sufiishq.app.utils.quickToast
import pk.sufiishq.app.utils.rem
import pk.sufiishq.aurora.components.SICircularProgressIndicator
import pk.sufiishq.aurora.components.SIHeightSpace
import pk.sufiishq.aurora.components.SIImage
import pk.sufiishq.aurora.components.SIRangeSlider
import pk.sufiishq.aurora.components.SISlider
import pk.sufiishq.aurora.components.SIText
import pk.sufiishq.aurora.components.TextSize
import pk.sufiishq.aurora.layout.SIBox
import pk.sufiishq.aurora.layout.SIDialog
import pk.sufiishq.aurora.layout.SIRow
import pk.sufiishq.aurora.theme.AuroraColor

@PackagePrivate
@Composable
fun KalamSplitDialog(
    kalamController: KalamController,
) {
    val showSplitDialog = kalamController.showKalamSplitDialog().observeAsState()

    showSplitDialog.value?.apply {
        when (val status = splitStatus) {
            is SplitStatus.Start -> StartSplitView(this, kalamController)
            is SplitStatus.InProgress -> SplitInProgressView(this)
            is SplitStatus.Done -> SplitDoneView(this, kalamController)
            is SplitStatus.Completed -> SplitCompletedView(this, kalamController)
            is SplitStatus.Error -> quickToast(status.error)
        }
    }
}

@Composable
private fun StartSplitView(
    splitKalamInfo: SplitKalamInfo,
    kalamController: KalamController,
) {
    ShowDialog(
        splitKalamInfo = splitKalamInfo,
        onNoText = optString(TextRes.label_cancel),
        onNoClick = { dismissDialog(kalamController) },
        onYesText = optString(TextRes.label_preview),
        onYesClick = { startSplitting(kalamController) },
    ) { textColor ->
        SIRangeSlider(
            value = splitKalamInfo.splitStart.toFloat()..splitKalamInfo.splitEnd.toFloat(),
            onValueChange = {
                var start = it.start.toInt()
                var end = it.endInclusive.toInt()

                when {
                    (start == splitKalamInfo.kalamLength) -> start = end.minus(1000)
                    (end == 0) -> end = 1000
                    (start == end) -> end = end.plus(1000)
                }

                kalamController.setSplitStart(start)
                kalamController.setSplitEnd(end)
            },
            valueRange = 0f..splitKalamInfo.kalamLength.toFloat(),
        )

        SIRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            SIText(
                text = splitKalamInfo.splitStart.formatTime,
                textColor = textColor,
                textSize = TextSize.Small,
            )

            SIText(
                text = splitKalamInfo.splitEnd.formatTime,
                textColor = textColor,
                textSize = TextSize.Small,
            )
        }
    }
}

@Composable
private fun SplitCompletedView(
    splitKalamInfo: SplitKalamInfo,
    kalamController: KalamController,
) {
    val kalamTitle = rem("")

    ShowDialog(
        splitKalamInfo = splitKalamInfo,
        onNoText = optString(TextRes.label_back),
        onNoClick = { backToDone(kalamController) },
        onYesText = optString(TextRes.label_save),
        onYesClick = { saveKalam(kalamTitle.value.trim(), splitKalamInfo, kalamController) },
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = kalamTitle.value,
            onValueChange = { kalamTitle.value = it },
            label = optString(TextRes.label_kalam_title),
            emptyFieldError = optString(TextRes.msg_kalam_title_required),
            maxLength = KALAM_TITLE_LENGTH,
        )
    }
}

@Composable
private fun SplitDoneView(
    splitKalamInfo: SplitKalamInfo,
    kalamController: KalamController,
) {
    val previewPlayStart = splitKalamInfo.previewPlayStart
    val previewKalamProgress = splitKalamInfo.previewKalamProgress
    val kalamPreviewLength = splitKalamInfo.previewKalamLength

    ShowDialog(
        splitKalamInfo = splitKalamInfo,
        onNoText = optString(TextRes.label_back),
        onNoClick = { backToStart(kalamController) },
        onYesText = optString(TextRes.label_done),
        onYesClick = { done(kalamController) },
    ) { textColor ->
        SIRow(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SIImage(
                modifier = Modifier
                    .width(35.dp)
                    .clickable { kalamController.playSplitKalamPreview() },
                resId = if (previewPlayStart) ImageRes.ic_pause else ImageRes.ic_play,
                tintColor = textColor,
            )

            SISlider(
                modifier = Modifier.padding(start = 8.dp),
                value = previewKalamProgress.toFloat(),
                valueRange = 0f..kalamPreviewLength.toFloat(),
                enabled = true,
                onValueChange = { kalamController.updateSplitSeekbarValue(it) },
            )
        }

        SIHeightSpace(value = 8)

        SIRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            SIText(
                text = previewKalamProgress.formatTime,
                textColor = textColor,
                textSize = TextSize.Small,
            )
            SIText(
                text = kalamPreviewLength.formatTime,
                textColor = textColor,
                textSize = TextSize.Small,
            )
        }
    }
}

@Composable
private fun SplitInProgressView(
    splitKalamInfo: SplitKalamInfo,
) {
    ShowDialog(
        splitKalamInfo = splitKalamInfo,
    ) {
        SIBox(
            modifier = Modifier.fillMaxWidth(),
        ) {
            SICircularProgressIndicator(
                strokeWidth = 2,
            )
        }
    }
}

@Composable
private fun ShowDialog(
    splitKalamInfo: SplitKalamInfo,
    onNoText: String? = null,
    onNoClick: () -> Unit = {},
    onYesText: String? = null,
    onYesClick: () -> Unit = {},
    content: @Composable ColumnScope.(fgColor: AuroraColor) -> Unit,
) {
    SIDialog(
        title = optString(TextRes.dynamic_title_split_kalam, splitKalamInfo.kalam.title),
        onNoText = onNoText,
        onNoClick = onNoClick,
        onYesText = onYesText,
        onYesClick = onYesClick,
    ) { textColor ->
        content(textColor)
    }
}

private fun dismissDialog(kalamController: KalamController) {
    kalamController.dismissKalamSplitDialog()
}

private fun startSplitting(kalamController: KalamController) {
    kalamController.startSplitting()
}

private fun backToStart(kalamController: KalamController) {
    kalamController.setSplitStatus(SplitStatus.Start)
}

private fun done(kalamController: KalamController) {
    kalamController.setSplitStatus(SplitStatus.Completed)
}

private fun backToDone(kalamController: KalamController) {
    kalamController.setSplitStatus(SplitStatus.Done)
}

private fun saveKalam(
    kalamTitle: String,
    splitKalamInfo: SplitKalamInfo,
    kalamController: KalamController,
) {
    if (kalamTitle.isEmpty()) {
        quickToast(TextRes.msg_kalam_title_required)
    } else {
        kalamController.saveSplitKalam(
            sourceKalam = splitKalamInfo.kalam,
            kalamTitle,
        )

        dismissDialog(kalamController)
    }
}
