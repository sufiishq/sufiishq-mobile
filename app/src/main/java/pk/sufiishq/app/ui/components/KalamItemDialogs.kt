package pk.sufiishq.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import org.apache.commons.io.FilenameUtils
import pk.sufiishq.app.data.providers.KalamDataProvider
import pk.sufiishq.app.data.providers.PlayerDataProvider
import pk.sufiishq.app.helpers.KalamSplitManager
import pk.sufiishq.app.models.Kalam
import pk.sufiishq.app.models.KalamItemParam
import pk.sufiishq.app.utils.KALAM_DIR

@Composable
fun KalamItemDownloadDialog(
    showDownloadDialog: MutableState<Boolean>,
    kalamItemParam: KalamItemParam
) {
    if (showDownloadDialog.value) {

        val kalam = kalamItemParam.kalam
        val playerDataProvider = kalamItemParam.playerDataProvider
        val kalamDataProvider = kalamItemParam.kalamDataProvider

        val progress = playerDataProvider.getDownloadProgress().observeAsState().value ?: 0f

        Dialog(
            onDismissRequest = {
                if (progress >= 100) showDownloadDialog.value = false
            },
        ) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colors.surface,
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    ShowTitle(progress = progress, title = kalam.title)

                    when {
                        (progress > 0 && progress < 100) -> ShowDownloadProgress(playerDataProvider)
                        (progress >= 100) -> ShowDownloadSuccessfully(kalam, kalamDataProvider)
                        else -> {
                            LinearProgressIndicator(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp)
                            )
                        }
                    }

                    DownloadBottomButton(progress, playerDataProvider, showDownloadDialog)
                }
            }
        }
    }
}

@Composable
private fun ShowTitle(progress: Float, title: String) {
    if (progress < 100) Text(text = title)
}

@Composable
private fun ShowDownloadProgress(playerDataProvider: PlayerDataProvider) {
    LinearProgressIndicator(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        progress = playerDataProvider.getDownloadProgress()
            .observeAsState().value
            ?: 0f,
    )
}

@Composable
private fun ShowDownloadSuccessfully(
    kalam: Kalam,
    kalamDataProvider: KalamDataProvider
) {
    kalam.offlineSource =
        "$KALAM_DIR/${FilenameUtils.getName(kalam.onlineSource)}"
    kalamDataProvider.update(kalam)

    Text(
        buildAnnotatedString {
            append("Kalam ")
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append(kalam.title)
            }
            append(" successfully downloaded")
        }
    )
}

@Composable
private fun DownloadBottomButton(
    progress: Float,
    playerDataProvider: PlayerDataProvider,
    showDownloadDialog: MutableState<Boolean>
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {

        if (progress < 100) {
            TextButton(onClick = {
                playerDataProvider.disposeDownload()
                showDownloadDialog.value = false
            }) {
                Text(text = "Cancel")
            }
        } else {
            TextButton(onClick = { showDownloadDialog.value = false }) {
                Text(text = "OK")
            }
        }
    }
}

@Composable
fun KalamItemConfirmDeleteDialog(
    showDeleteKalamConfirmDialog: MutableState<Boolean>,
    kalamItemParam: KalamItemParam
) {

    if (showDeleteKalamConfirmDialog.value) {

        val (kalam,
            _,
            _,
            kalamDataProvider,
            _,
            lazyKalamItems,
            _,
            searchText,
            trackType,
            playlistId) = kalamItemParam

        Dialog(
            onDismissRequest = {
                showDeleteKalamConfirmDialog.value = false
            },
        ) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colors.surface,
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    Text(
                        buildAnnotatedString {
                            append("Are you sure you want to delete ")
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append(kalam.title)
                            }
                        }
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = {
                            showDeleteKalamConfirmDialog.value = false
                        }) {
                            Text(text = "NO")
                        }
                        TextButton(onClick = {
                            showDeleteKalamConfirmDialog.value = false
                            kalamDataProvider.delete(kalam, trackType)
                            kalamDataProvider.searchKalam(searchText.value, trackType, playlistId)
                            lazyKalamItems.refresh()
                        }) {
                            Text(text = "YES")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun KalamDownloadErrorDialog(
    downloadError: State<String?>,
    showDownloadDialog: MutableState<Boolean>,
    kalamItemParam: KalamItemParam
) {
    downloadError.value?.let { error ->
        if (error.isNotEmpty()) {
            val playerDataProvider = kalamItemParam.playerDataProvider
            playerDataProvider.disposeDownload()
            showDownloadDialog.value = false
            AlertDialog(
                onDismissRequest = {
                    playerDataProvider.setDownloadError("")
                },
                title = {
                    Text(text = "Download Error", fontWeight = FontWeight.Bold)
                },
                text = {
                    Text(error)
                },
                confirmButton = {

                },
                dismissButton = {
                    Button(
                        onClick = {
                            playerDataProvider.setDownloadError("")
                        }
                    ) {
                        Text("OK")
                    }
                }
            )
        }
    }
}

@Composable
fun KalamItemSplitDialog(
    showSplitterDialog: MutableState<Boolean>,
    kalamSplitManager: MutableState<KalamSplitManager>,
    kalamItemParam: KalamItemParam
) {
    if (showSplitterDialog.value) {

        SufiIshqDialog {
            KalamSplitter(
                showDialog = showSplitterDialog,
                kalamSplitManager = kalamSplitManager.value,
                kalamItemParam = kalamItemParam
            )
        }
    }
}