package pk.sufiishq.app.ui.components.dialogs

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import pk.sufiishq.app.R
import pk.sufiishq.app.core.event.events.KalamEvents
import pk.sufiishq.app.core.event.events.KalamSplitManagerEvents
import pk.sufiishq.app.core.splitter.KalamSplitManager
import pk.sufiishq.app.core.splitter.SplitStatus
import pk.sufiishq.app.ui.components.SingleOutlinedTextField
import pk.sufiishq.app.utils.KALAM_TITLE_LENGTH
import pk.sufiishq.app.utils.dispatch
import pk.sufiishq.app.utils.formatTime
import pk.sufiishq.app.utils.optValue
import pk.sufiishq.app.utils.rem
import pk.sufiishq.app.utils.toast
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

@Composable
fun KalamItemSplitDialog(kalamSplitManager: State<KalamSplitManager?>) {

    kalamSplitManager.value?.apply {

        val splitStatus = getSplitStatus().observeAsState()

        when (val status = splitStatus.value) {
            is SplitStatus.Start -> StartSplitView(kalamSplitManager = this)
            is SplitStatus.InProgress -> SplitInProgressView(kalamSplitManager = this)
            is SplitStatus.Done -> SplitDoneView(kalamSplitManager = this)
            is SplitStatus.Completed -> SplitCompletedView(kalamSplitManager = this)
            is SplitStatus.Error -> LocalContext.current.toast(status.error)
            else -> throw IllegalStateException("${status.toString()} is not covered")
        }
    }
}

@Composable
private fun StartSplitView(kalamSplitManager: KalamSplitManager) {

    val splitStart = kalamSplitManager.getSplitStart().observeAsState()
    val splitEnd = kalamSplitManager.getSplitEnd().observeAsState()
    val kalamLength = kalamSplitManager.getKalamLength().observeAsState()

    ShowDialog(
        kalamSplitManager = kalamSplitManager,
        onNoText = "Cancel",
        onNoClick = ::hideDialog,
        onYesText = "Preview",
        onYesClick = ::startSplitting
    ) { textColor ->

        SIRangeSlider(
            value = splitStart.optValue(0).toFloat()..splitEnd.optValue(0).toFloat(),
            onValueChange = {
                var start = it.start.toInt()
                var end = it.endInclusive.toInt()

                when {
                    (start == kalamLength.optValue(0)) -> start = end.minus(1000)
                    (end == 0) -> end = 1000
                    (start == end) -> end = end.plus(1000)
                }

                KalamSplitManagerEvents.SetSplitStart(start)
                    .dispatch(KalamSplitManagerEvents.SetSplitEnd(end))
            },
            valueRange = 0f..kalamLength.optValue(0).toFloat()
        )

        SIRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SIText(
                text = splitStart.optValue(0).formatTime,
                textColor = textColor,
                textSize = TextSize.Small
            )

            SIText(
                text = splitEnd.optValue(0).formatTime,
                textColor = textColor,
                textSize = TextSize.Small
            )
        }
    }
}

@Composable
private fun SplitCompletedView(kalamSplitManager: KalamSplitManager) {

    val context = LocalContext.current
    val kalamTitle = rem("")
    val kalamTitleError = rem(false)

    ShowDialog(
        kalamSplitManager = kalamSplitManager,
        onNoText = "Back",
        onNoClick = ::backToDone,
        onYesText = "Save",
        onYesClick = {
            saveKalam(context, kalamTitle.value, kalamSplitManager, kalamTitleError)
        }
    ) {
        SingleOutlinedTextField(
            value = kalamTitle.value,
            onValueChange = {
                kalamTitle.value = it
                kalamTitleError.value = false
            },
            label = "Kalam Title",
            isError = kalamTitleError,
            maxLength = KALAM_TITLE_LENGTH
        )
    }
}

@Composable
private fun SplitDoneView(kalamSplitManager: KalamSplitManager) {

    val previewPlayStart = kalamSplitManager.getPreviewPlayStart().observeAsState()
    val previewKalamProgress = kalamSplitManager.getPreviewKalamProgress().observeAsState()
    val kalamPreviewLength = kalamSplitManager.getKalamPreviewLength().observeAsState()

    ShowDialog(
        kalamSplitManager = kalamSplitManager,
        onNoText = "Back",
        onNoClick = ::backToStart,
        onYesText = "Done",
        onYesClick = ::done
    ) { textColor ->

        SIRow(
            verticalAlignment = Alignment.CenterVertically
        ) {
            SIImage(
                modifier = Modifier
                    .width(35.dp)
                    .clickable {
                        KalamSplitManagerEvents.PlayPreview.dispatch()
                    },
                resId = if (previewPlayStart.optValue(false)) R.drawable.ic_pause else R.drawable.ic_play,
                tintColor = textColor,
            )

            SISlider(
                modifier = Modifier.padding(start = 8.dp),
                value = previewKalamProgress.optValue(0).toFloat(),
                valueRange = 0f..kalamPreviewLength.optValue(0).toFloat(),
                enabled = true,
                onValueChange = {
                    KalamSplitManagerEvents.UpdateSeekbar(it).dispatch()
                }
            )
        }

        SIHeightSpace(value = 8)

        SIRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SIText(
                text = previewKalamProgress.optValue(0).formatTime,
                textColor = textColor,
                textSize = TextSize.Small
            )
            SIText(
                text = kalamPreviewLength.optValue(0).formatTime,
                textColor = textColor,
                textSize = TextSize.Small
            )
        }
    }
}

@Composable
private fun SplitInProgressView(kalamSplitManager: KalamSplitManager) {
    ShowDialog(kalamSplitManager = kalamSplitManager) {
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
    kalamSplitManager: KalamSplitManager,
    onNoText: String? = null,
    onNoClick: () -> Unit = {},
    onYesText: String? = null,
    onYesClick: () -> Unit = {},
    content: @Composable ColumnScope.(fgColor: AuroraColor) -> Unit
) {
    SIDialog(
        onNoText = onNoText,
        onNoClick = onNoClick,
        onYesText = onYesText,
        onYesClick = onYesClick,
    ) { textColor ->

        SIText(
            text = "Split Kalam ${kalamSplitManager.getKalam().title}",
            textColor = textColor,
            fontWeight = FontWeight.Bold
        )

        SIHeightSpace(value = 12)

        content(textColor)
    }
}

private fun hideDialog() {
    KalamEvents.ShowKalamSplitManagerDialog(null).dispatch()
}

private fun startSplitting() {
    KalamSplitManagerEvents.StartSplitting.dispatch()
}

private fun backToStart() {
    KalamSplitManagerEvents.SetSplitStatus(SplitStatus.Start).dispatch()
}

private fun done() {
    KalamSplitManagerEvents.SetSplitStatus(SplitStatus.Completed).dispatch()
}

private fun backToDone() {
    KalamSplitManagerEvents.SetSplitStatus(SplitStatus.Done).dispatch()
}

private fun saveKalam(
    context: Context,
    kalamTitle: String,
    kalamSplitManager: KalamSplitManager,
    kalamTitleError: MutableState<Boolean>
) {

    if (kalamTitle.trim().isEmpty()) {
        kalamTitleError.value = true
        context.toast("Kalam Title cannot be empty.")
    } else {

        // save split kalam
        KalamEvents.SaveSplitKalam(
            kalamSplitManager.getKalam(),
            kalamSplitManager.getSplitFile(),
            kalamTitle.trim()
        ).dispatch()

        hideDialog()
    }
}