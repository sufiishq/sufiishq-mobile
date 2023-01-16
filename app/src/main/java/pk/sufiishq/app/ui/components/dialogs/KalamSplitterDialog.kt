package pk.sufiishq.app.ui.components.dialogs

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import pk.sufiishq.app.R
import pk.sufiishq.app.core.event.events.KalamEvents
import pk.sufiishq.app.core.event.events.KalamSplitManagerEvents
import pk.sufiishq.app.helpers.KalamSplitManager
import pk.sufiishq.app.helpers.SplitCompleted
import pk.sufiishq.app.helpers.SplitDone
import pk.sufiishq.app.ui.components.SingleOutlinedTextField
import pk.sufiishq.app.utils.*

@Composable
fun KalamItemSplitDialog(
    kalamSplitManager: State<KalamSplitManager?>
) {
    kalamSplitManager.value?.apply {
        SufiIshqDialog {
            KalamSplitter(
                kalamSplitManager = this
            )
        }
    }
}

@Composable
private fun KalamSplitter(
    kalamSplitManager: KalamSplitManager
) {

    val splitStatus = kalamSplitManager.getSplitStatus().observeAsState()

    Column(modifier = Modifier.fillMaxWidth()) {

        Text(
            modifier = Modifier.padding(bottom = 12.dp),
            text = "Split Kalam ${kalamSplitManager.getKalam().title}",
            fontWeight = FontWeight.Bold
        )

        when (val status = splitStatus.value) {
            is SplitCompleted -> SplitCompletedView(
                status,
                kalamSplitManager
            )
            is SplitDone -> SplitDoneView(
                kalamSplitManager
            )
            else -> SplitInProgressView()
        }
    }
}

@Composable
private fun SplitCompletedView(
    status: SplitCompleted,
    kalamSplitManager: KalamSplitManager
) {
    if (status.returnCode == SPLIT_SUCCESS) {
        SplitSuccessView(
            kalamSplitManager = kalamSplitManager
        )
    } else {

        if (status.returnCode != SPLIT_CANCEL) {
            LocalContext.current.toast("Execution failed with ${status.returnCode}")
        }

        SplitView(kalamSplitManager = kalamSplitManager)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun SplitView(
    kalamSplitManager: KalamSplitManager
) {

    val splitStart = kalamSplitManager.getSplitStart().observeAsState()
    val splitEnd = kalamSplitManager.getSplitEnd().observeAsState()
    val kalamLength = kalamSplitManager.getKalamLength().observeAsState()

    Column {
        RangeSlider(
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
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = splitStart.optValue(0).formatTime)
        Text(text = splitEnd.optValue(0).formatTime)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 6.dp),
        horizontalArrangement = Arrangement.End
    ) {

        TextButton(onClick = { KalamEvents.ShowKalamSplitManagerDialog(null).dispatch() }) {
            Text(text = "Cancel")
        }
        TextButton(onClick = { KalamSplitManagerEvents.StartPreview.dispatch() }) {
            Text(text = "Preview")
        }
    }
}

@Composable
private fun SplitSuccessView(
    kalamSplitManager: KalamSplitManager
) {

    val previewPlayStart = kalamSplitManager.getPreviewPlayStart().observeAsState()
    val previewKalamProgress = kalamSplitManager.getPreviewKalamProgress().observeAsState()
    val kalamPreviewLength = kalamSplitManager.getKalamPreviewLength().observeAsState()

    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                modifier = Modifier
                    .width(35.dp)
                    .clickable {
                        KalamSplitManagerEvents.PlayPreview.dispatch()
                    },
                painter = painterResource(id = if (previewPlayStart.optValue(false)) R.drawable.ic_pause else R.drawable.ic_play),
                colorFilter = ColorFilter.tint(MaterialTheme.colors.primary),
                contentDescription = null
            )
            Slider(
                modifier = Modifier.padding(start = 8.dp),
                value = previewKalamProgress.optValue(0).toFloat(),
                valueRange = 0f..kalamPreviewLength.optValue(0).toFloat(),
                enabled = true,
                onValueChange = {
                    KalamSplitManagerEvents.UpdateSeekbar(it).dispatch()
                })
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = previewKalamProgress.optValue(0).formatTime)
        Text(text = kalamPreviewLength.optValue(0).formatTime)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 6.dp),
        horizontalArrangement = Arrangement.End
    ) {

        TextButton(onClick = {
            KalamSplitManagerEvents.SetSplitStatus(SplitCompleted()).dispatch()
        }) {
            Text(text = "Back")
        }

        TextButton(onClick = {
            KalamSplitManagerEvents.SetSplitStatus(SplitDone).dispatch()
        }) {
            Text(text = "Done")
        }
    }
}

@Composable
private fun SplitDoneView(
    kalamSplitManager: KalamSplitManager
) {

    val context = LocalContext.current
    val kalamTitle = rem("")
    val kalamTitleError = rem(false)

    Column {

        SingleOutlinedTextField(
            value = kalamTitle.value,
            onValueChange = {
                kalamTitle.value = it
                kalamTitleError.value = false
            },
            label = "Kalam Title",
            isError = kalamTitleError.value,
            maxLength = KALAM_TITLE_LENGTH
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp),
            horizontalArrangement = Arrangement.End
        ) {

            TextButton(onClick = {
                KalamSplitManagerEvents.SetSplitStatus(
                    SplitCompleted(
                        SPLIT_SUCCESS
                    )
                ).dispatch()
            }) {
                Text(text = "Back")
            }
            TextButton(onClick = {
                if (kalamTitle.value.trim().isEmpty()) {
                    kalamTitleError.value = true
                    context.toast("Kalam Title cannot be empty.")
                } else {

                    // save split kalam
                    KalamEvents.SaveSplitKalam(
                        kalamSplitManager.getKalam(),
                        kalamSplitManager.getSplitFile(),
                        kalamTitle.value.trim()
                    ).dispatch(

                        // hide split dialog
                        KalamEvents.ShowKalamSplitManagerDialog(null)
                    )

                }
            }) {
                Text(text = "Save")
            }
        }
    }
}

@Composable
private fun SplitInProgressView() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}