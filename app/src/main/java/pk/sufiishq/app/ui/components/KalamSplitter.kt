package pk.sufiishq.app.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import pk.sufiishq.app.R
import pk.sufiishq.app.helpers.KalamSplitManager
import pk.sufiishq.app.helpers.SplitCompleted
import pk.sufiishq.app.helpers.SplitDone
import pk.sufiishq.app.models.KalamItemParam
import pk.sufiishq.app.utils.*

@Composable
fun KalamSplitter(
    showDialog: MutableState<Boolean>,
    kalamSplitManager: KalamSplitManager,
    kalamItemParam: KalamItemParam
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
                showDialog,
                kalamSplitManager,
                kalamItemParam
            )
            is SplitDone -> SplitDoneView(kalamSplitManager, showDialog, kalamItemParam)
            else -> SplitInProgressView()
        }
    }
}

@Composable
private fun SplitCompletedView(
    status: SplitCompleted,
    showDialog: MutableState<Boolean>,
    kalamSplitManager: KalamSplitManager,
    kalamItemParam: KalamItemParam
) {
    if (status.returnCode == SPLIT_SUCCESS) {
        SplitSuccessView(kalamSplitManager = kalamSplitManager)
    } else {

        if (status.returnCode != SPLIT_CANCEL) {
            LocalContext.current.toast("Execution failed with ${status.returnCode}")
        }

        SplitView(showDialog = showDialog, kalamSplitManager = kalamSplitManager)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun SplitView(
    showDialog: MutableState<Boolean>,
    kalamSplitManager: KalamSplitManager
) {

    val splitStart = kalamSplitManager.getSplitStart().observeAsState()
    val splitEnd = kalamSplitManager.getSplitEnd().observeAsState()
    val kalamLength = kalamSplitManager.getKalamLength().observeAsState()

    Column {
        RangeSlider(
            values = splitStart.optValue(0).toFloat()..splitEnd.optValue(0).toFloat(),
            onValueChange = {
                kalamSplitManager.setSplitStart(it.start.toInt())
                kalamSplitManager.setSplitEnd(it.endInclusive.toInt())
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

        TextButton(onClick = { showDialog.value = false }) {
            Text(text = "Cancel")
        }
        TextButton(onClick = { kalamSplitManager.startPreview() }) {
            Text(text = "Preview")
        }
    }
}

@Composable
private fun SplitSuccessView(kalamSplitManager: KalamSplitManager) {

    val previewPlayStart = kalamSplitManager.getPreviewPlayStart().observeAsState()
    val previewKalamProgress = kalamSplitManager.getPreviewKalamProgress().observeAsState()
    val kalamPreviewLength = kalamSplitManager.getKalamPreviewLength().observeAsState()

    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                modifier = Modifier
                    .width(35.dp)
                    .clickable {
                        kalamSplitManager.playPreview()
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
                    kalamSplitManager.updateSeekbarValue(it)
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

        TextButton(onClick = { kalamSplitManager.setSplitStatus(SplitCompleted()) }) {
            Text(text = "Back")
        }
        TextButton(onClick = { kalamSplitManager.setSplitStatus(SplitDone) }) {
            Text(text = "Done")
        }
    }
}

@Composable
private fun SplitDoneView(
    kalamSplitManager: KalamSplitManager,
    showDialog: MutableState<Boolean>,
    kalamItemParam: KalamItemParam
) {

    val context = LocalContext.current
    val kalamTitle = rem("")
    val kalamTitleError = rem(false)

    val (
        _,
        _,
        _,
        kalamDataProvider,
        _,
        lazyKalamItems,
        _,
        searchText,
        trackType,
        playlistId) = kalamItemParam

    Column {

        SingleOutlinedTextField(
            value = kalamTitle.value,
            onValueChange = {
                kalamTitle.value = it
                kalamTitleError.value = false
            },
            label = "Kalam Title",
            isError = kalamTitleError.value,
            maxLength = 30
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp),
            horizontalArrangement = Arrangement.End
        ) {

            TextButton(onClick = {
                kalamSplitManager.setSplitStatus(
                    SplitCompleted(
                        SPLIT_SUCCESS
                    )
                )
            }) {
                Text(text = "Back")
            }
            TextButton(onClick = {
                if (kalamTitle.value.trim().isEmpty()) {
                    kalamTitleError.value = true
                    context.toast("Kalam Title cannot be empty.")
                } else {
                    kalamDataProvider.save(
                        kalamSplitManager.getKalam(),
                        kalamSplitManager.getSplitFile(),
                        kalamTitle.value.trim()
                    )
                    kalamDataProvider.searchKalam(searchText.value, trackType, playlistId)
                    lazyKalamItems.refresh()
                    showDialog.value = false

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