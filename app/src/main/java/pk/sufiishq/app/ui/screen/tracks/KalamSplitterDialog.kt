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
import pk.sufiishq.app.R
import pk.sufiishq.app.core.kalam.splitter.SplitKalamInfo
import pk.sufiishq.app.core.kalam.splitter.SplitStatus
import pk.sufiishq.app.data.providers.KalamDataProvider
import pk.sufiishq.app.ui.components.OutlinedTextField
import pk.sufiishq.app.utils.KALAM_TITLE_LENGTH
import pk.sufiishq.app.utils.formatTime
import pk.sufiishq.app.utils.optString
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
    kalamDataProvider: KalamDataProvider
) {

    val showSplitDialog = kalamDataProvider.showKalamSplitDialog().observeAsState()

    showSplitDialog.value?.apply {

        when (val status = splitStatus) {
            is SplitStatus.Start -> StartSplitView(this, kalamDataProvider)
            is SplitStatus.InProgress -> SplitInProgressView(this)
            is SplitStatus.Done -> SplitDoneView(this, kalamDataProvider)
            is SplitStatus.Completed -> SplitCompletedView(this, kalamDataProvider)
            is SplitStatus.Error -> quickToast(status.error)
        }
    }
}

@Composable
private fun StartSplitView(
    splitKalamInfo: SplitKalamInfo, kalamDataProvider: KalamDataProvider
) {

    ShowDialog(
        splitKalamInfo = splitKalamInfo,
        onNoText = optString(R.string.label_cancel),
        onNoClick = {
            dismissDialog(kalamDataProvider)
        },
        onYesText = optString(R.string.label_preview),
        onYesClick = {
            startSplitting(kalamDataProvider)
        }
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

                kalamDataProvider.setSplitStart(start)
                kalamDataProvider.setSplitEnd(end)
            },
            valueRange = 0f..splitKalamInfo.kalamLength.toFloat()
        )

        SIRow(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SIText(
                text = splitKalamInfo.splitStart.formatTime,
                textColor = textColor,
                textSize = TextSize.Small
            )

            SIText(
                text = splitKalamInfo.splitEnd.formatTime,
                textColor = textColor,
                textSize = TextSize.Small
            )
        }
    }
}

@Composable
private fun SplitCompletedView(
    splitKalamInfo: SplitKalamInfo, kalamDataProvider: KalamDataProvider
) {

    val kalamTitle = rem("")

    ShowDialog(
        splitKalamInfo = splitKalamInfo,
        onNoText = optString(R.string.label_back),
        onNoClick = {
            backToDone(kalamDataProvider)
        },
        onYesText = optString(R.string.label_save),
        onYesClick = {
            saveKalam(kalamTitle.value.trim(), splitKalamInfo, kalamDataProvider)
        }
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = kalamTitle.value,
            onValueChange = {
                kalamTitle.value = it
            },
            label = optString(R.string.label_kalam_title),
            emptyFieldError = optString(R.string.msg_kalam_title_required),
            maxLength = KALAM_TITLE_LENGTH
        )
    }
}

@Composable
private fun SplitDoneView(
    splitKalamInfo: SplitKalamInfo, kalamDataProvider: KalamDataProvider
) {

    val previewPlayStart = splitKalamInfo.previewPlayStart
    val previewKalamProgress = splitKalamInfo.previewKalamProgress
    val kalamPreviewLength = splitKalamInfo.previewKalamLength

    ShowDialog(
        splitKalamInfo = splitKalamInfo,
        onNoText = optString(R.string.label_back),
        onNoClick = {
            backToStart(kalamDataProvider)
        },
        onYesText = optString(R.string.label_done),
        onYesClick = {
            done(kalamDataProvider)
        }
    ) { textColor ->

        SIRow(
            verticalAlignment = Alignment.CenterVertically
        ) {
            SIImage(
                modifier = Modifier
                    .width(35.dp)
                    .clickable {
                        kalamDataProvider.playSplitKalamPreview()
                    },
                resId = if (previewPlayStart) R.drawable.ic_pause else R.drawable.ic_play,
                tintColor = textColor,
            )

            SISlider(modifier = Modifier.padding(start = 8.dp),
                value = previewKalamProgress.toFloat(),
                valueRange = 0f..kalamPreviewLength.toFloat(),
                enabled = true,
                onValueChange = {
                    kalamDataProvider.updateSplitSeekbarValue(it)
                })
        }

        SIHeightSpace(value = 8)

        SIRow(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SIText(
                text = previewKalamProgress.formatTime,
                textColor = textColor,
                textSize = TextSize.Small
            )
            SIText(
                text = kalamPreviewLength.formatTime,
                textColor = textColor,
                textSize = TextSize.Small
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
            modifier = Modifier.fillMaxWidth()
        ) {
            SICircularProgressIndicator(
                strokeWidth = 2
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
    content: @Composable ColumnScope.(fgColor: AuroraColor) -> Unit
) {
    SIDialog(
        title = optString(R.string.dynamic_title_split_kalam, splitKalamInfo.kalam.title),
        onNoText = onNoText,
        onNoClick = onNoClick,
        onYesText = onYesText,
        onYesClick = onYesClick,
    ) { textColor ->

        content(textColor)
    }
}

private fun dismissDialog(kalamDataProvider: KalamDataProvider) {
    kalamDataProvider.dismissKalamSplitDialog()
}

private fun startSplitting(kalamDataProvider: KalamDataProvider) {
    kalamDataProvider.startSplitting()
}

private fun backToStart(kalamDataProvider: KalamDataProvider) {
    kalamDataProvider.setSplitStatus(SplitStatus.Start)
}

private fun done(kalamDataProvider: KalamDataProvider) {
    kalamDataProvider.setSplitStatus(SplitStatus.Completed)
}

private fun backToDone(kalamDataProvider: KalamDataProvider) {
    kalamDataProvider.setSplitStatus(SplitStatus.Done)
}

private fun saveKalam(
    kalamTitle: String,
    splitKalamInfo: SplitKalamInfo,
    kalamDataProvider: KalamDataProvider
) {

    if (kalamTitle.isEmpty()) {
        quickToast(R.string.msg_kalam_title_required)
    } else {

        kalamDataProvider.saveSplitKalam(
            sourceKalam = splitKalamInfo.kalam, kalamTitle
        )

        dismissDialog(kalamDataProvider)
    }
}