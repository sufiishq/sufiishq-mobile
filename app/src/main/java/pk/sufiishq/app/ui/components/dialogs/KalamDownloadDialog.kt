package pk.sufiishq.app.ui.components.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import pk.sufiishq.app.core.downloader.KalamDownloadState
import pk.sufiishq.app.core.event.dispatcher.EventDispatcher
import pk.sufiishq.app.core.event.events.PlayerEvents

@Composable
fun KalamDownloadStartedDialog(
    eventDispatcher: EventDispatcher,
    kalamDownloadState: State<KalamDownloadState?>
) {
    kalamDownloadState.value?.apply {

        if (this is KalamDownloadState.Started) {

            DownloadDialog {
                Text(text = kalam.title)

                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )

                CancelButton(eventDispatcher = eventDispatcher)
            }
        }
    }
}

@Composable
fun KalamDownloadInProgressDialog(
    eventDispatcher: EventDispatcher,
    kalamDownloadState: State<KalamDownloadState?>
) {
    kalamDownloadState.value?.apply {

        if (this is KalamDownloadState.InProgress) {

            val progress = progress
            val title = kalam.title

            DownloadDialog {
                Text(text = title)

                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    progress = progress,
                )

                CancelButton(eventDispatcher = eventDispatcher)
            }
        }
    }
}

@Composable
fun KalamDownloadCompletedDialog(
    eventDispatcher: EventDispatcher,
    kalamDownloadState: State<KalamDownloadState?>
) {
    kalamDownloadState.value?.apply {

        if (this is KalamDownloadState.Completed) {
            DownloadDialog {
                Text(text = kalam.title)

                Text(
                    buildAnnotatedString {
                        append("Kalam ")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(kalam.title)
                        }
                        append(" successfully downloaded")
                    }
                )

                OKButton(eventDispatcher = eventDispatcher)
            }
        }
    }
}

@Composable
fun KalamDownloadErrorDialog(
    eventDispatcher: EventDispatcher,
    kalamDownloadState: State<KalamDownloadState?>
) {

    kalamDownloadState.value?.apply {
        if (this is KalamDownloadState.Error) {

            DownloadDialog {
                Text(text = kalam.title)

                Spacer(modifier = Modifier.height(12.dp))

                Text(text = "Download Error", fontWeight = FontWeight.Bold)
                Text(error)

                OKButton(eventDispatcher = eventDispatcher)
            }
        }
    }
}

@Composable
private fun DownloadDialog(content: @Composable () -> Unit) {
    Dialog(onDismissRequest = {}) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colors.surface,
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                content()
            }
        }
    }
}

@Composable
private fun OKButton(eventDispatcher: EventDispatcher) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {

        TextButton(onClick = {
            eventDispatcher.dispatch(PlayerEvents.ChangeDownloadState(KalamDownloadState.Idle))
        }) {
            Text(text = "OK")
        }
    }
}

@Composable
private fun CancelButton(eventDispatcher: EventDispatcher) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        TextButton(onClick = {
            eventDispatcher.dispatch(PlayerEvents.DisposeDownload)
            eventDispatcher.dispatch(PlayerEvents.ChangeDownloadState(KalamDownloadState.Idle))
        }) {
            Text(text = "Cancel")
        }
    }
}