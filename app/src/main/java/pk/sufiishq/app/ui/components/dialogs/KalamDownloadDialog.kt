package pk.sufiishq.app.ui.components.dialogs

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import okhttp3.internal.format
import pk.sufiishq.app.core.downloader.KalamDownloadState
import pk.sufiishq.app.core.event.events.PlayerEvents
import pk.sufiishq.app.utils.dispatch
import pk.sufiishq.aurora.components.SIHeightSpace
import pk.sufiishq.aurora.components.SILinearProgressIndicator
import pk.sufiishq.aurora.components.SIText
import pk.sufiishq.aurora.components.TextSize
import pk.sufiishq.aurora.layout.SIDialog
import pk.sufiishq.aurora.layout.SIRow

@Composable
fun KalamDownloadStartedDialog(
    kalamDownloadState: State<KalamDownloadState?>
) {
    kalamDownloadState.value?.apply {

        if (this is KalamDownloadState.Started) {

            SIDialog(
                onNoText = "Cancel",
                onNoClick = ::onNoClick
            ) { textColor ->
                SIText(
                    text = kalam.title,
                    textColor = textColor,
                    textSize = TextSize.Regular
                )

                SILinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )
            }
        }
    }
}

@Composable
fun KalamDownloadInProgressDialog(
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

            SIDialog(
                onNoText = "Cancel",
                onNoClick = ::onNoClick
            ) { textColor ->

                SIText(
                    text = title,
                    textColor = textColor,
                    textSize = TextSize.Regular
                )

                SIRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {

                    SIText(
                        text = "${fileInfo.progress}% of ",
                        textColor = textColor,
                        textSize = TextSize.Small
                    )
                    SIText(
                        text = format("%.2f", fileInfo.totalSize / 1024 / 1024) + " MB",
                        textColor = textColor,
                        textSize = TextSize.Small
                    )
                }

                SILinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    progress = progress,
                )
            }
        }
    }
}

@Composable
fun KalamDownloadCompletedDialog(
    kalamDownloadState: State<KalamDownloadState?>
) {
    kalamDownloadState.value?.apply {

        if (this is KalamDownloadState.Completed) {
            SIDialog(
                onYesText = "OK",
                onYesClick = ::onYesClick
            ) { textColor ->

                SIText(
                    text = kalam.title,
                    textColor = textColor,
                    textSize = TextSize.Regular
                )

                SIText(
                    text = buildAnnotatedString {
                        append("Kalam ")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(kalam.title)
                        }
                        append(" successfully downloaded")
                    },
                    textColor = textColor
                )

            }
        }
    }
}

@Composable
fun KalamDownloadErrorDialog(
    kalamDownloadState: State<KalamDownloadState?>
) {

    kalamDownloadState.value?.apply {
        if (this is KalamDownloadState.Error) {

            SIDialog(
                onYesText = "OK",
                onYesClick = ::onYesClick
            ) { textColor ->

                SIText(
                    text = kalam.title,
                    textColor = textColor,
                    textSize = TextSize.Regular
                )

                SIHeightSpace(value = 12)

                SIText(
                    text = "Download Error",
                    textColor = textColor,
                    fontWeight = FontWeight.Bold
                )

                SIText(
                    text = error,
                    textColor = textColor
                )

            }
        }
    }
}

private fun onYesClick() {
    PlayerEvents.ChangeDownloadState(KalamDownloadState.Idle).dispatch()
}

private fun onNoClick() {
    PlayerEvents.DisposeDownload.dispatch()
    PlayerEvents.ChangeDownloadState(KalamDownloadState.Idle).dispatch()
}