package pk.sufiishq.app.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import pk.sufiishq.app.R
import pk.sufiishq.app.data.providers.KalamDataProvider
import pk.sufiishq.app.helpers.KalamSplitManager
import pk.sufiishq.app.helpers.SplitCompleted
import pk.sufiishq.app.helpers.SplitDone
import pk.sufiishq.app.models.Kalam
import pk.sufiishq.app.utils.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun KalamSplitter(
    showDialog: MutableState<Boolean>,
    kalamSplitManager: KalamSplitManager,
    kalamDataProvider: KalamDataProvider,
    searchValue: String,
    trackType: String,
    playlistId: Int,
    lazyKalamItems: LazyPagingItems<Kalam>
) {

    val context = LocalContext.current
    val splitStatus = kalamSplitManager.getSplitStatus().observeAsState()
    val splitStart = kalamSplitManager.getSplitStart().observeAsState()
    val splitEnd = kalamSplitManager.getSplitEnd().observeAsState()
    val kalamPreviewLength = kalamSplitManager.getKalamPreviewLength().observeAsState()
    val kalamLength = kalamSplitManager.getKalamLength().observeAsState()
    val previewPlayStart = kalamSplitManager.getPreviewPlayStart().observeAsState()
    val previewKalamProgress = kalamSplitManager.getPreviewKalamProgress().observeAsState()

    Column(modifier = Modifier.fillMaxWidth()) {

        Text(
            modifier = Modifier.padding(bottom = 12.dp),
            text = "Split Kalam ${kalamSplitManager.getKalam().title}",
            fontWeight = FontWeight.Bold
        )

        val status = splitStatus.value
        if (status is SplitCompleted) {
            if (status.returnCode == SPLIT_SUCCESS) {
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

            } else {

                if (status.returnCode != SPLIT_CANCEL) {
                    LocalContext.current.toast("Execution failed with ${status.returnCode}")
                }

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
        } else if (status is SplitDone) {

            val kalamTitle = remember { mutableStateOf("") }
            val kalamTitleError = remember { mutableStateOf(false) }

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
                            kalamDataProvider.searchKalam(searchValue, trackType, playlistId)
                            lazyKalamItems.refresh()
                            showDialog.value = false

                        }
                    }) {
                        Text(text = "Save")
                    }
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}