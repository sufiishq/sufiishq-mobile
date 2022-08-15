package pk.sufiishq.app.ui.components.dialogs

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.MutableLiveData
import okhttp3.internal.format
import pk.sufiishq.app.core.downloader.KalamDownloadState
import pk.sufiishq.app.core.event.dispatcher.EventDispatcher
import pk.sufiishq.app.core.event.events.PlayerEvents
import pk.sufiishq.app.ui.theme.SufiIshqTheme
import pk.sufiishq.app.utils.dummyKalam
import pk.sufiishq.app.utils.rem

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

            val title = kalam.title
            val progress by animateFloatAsState(
                targetValue = fileInfo.progress.toFloat() / 100f * 1f,
                animationSpec = tween(
                    durationMillis = 800,
                    easing = LinearOutSlowInEasing
                )
            )

            DownloadDialog {
                Text(text = title)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(text = "${fileInfo.progress}% of ")
                    Text(text = format("%.2f", fileInfo.totalSize / 1024 / 1024) + " MB")
                }
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

/*
@Preview(showBackground = true)
@Composable
fun LightPreviewKalamDownloadInProgressDialog() {
    SufiIshqTheme {
        KalamDownloadInProgressDialog(
            eventDispatcher = EventDispatcher(),
            kalamDownloadState = MutableLiveData<KalamDownloadState?>(
                KalamDownloadState.InProgress(0.5f, dummyKalam())
            ).observeAsState()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DarkPreviewKalamDownloadInProgressDialog() {
    SufiIshqTheme(darkTheme = true) {
        KalamDownloadInProgressDialog(
            eventDispatcher = EventDispatcher(),
            kalamDownloadState = MutableLiveData<KalamDownloadState?>(
                KalamDownloadState.InProgress(0.5f, dummyKalam())
            ).observeAsState()
        )
    }
}*/
