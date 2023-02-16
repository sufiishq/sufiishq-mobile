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

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import io.github.esentsov.PackagePrivate
import okhttp3.internal.format
import pk.sufiishq.app.R
import pk.sufiishq.app.core.kalam.downloader.FileInfo
import pk.sufiishq.app.core.kalam.downloader.KalamDownloadState
import pk.sufiishq.app.data.controller.KalamController
import pk.sufiishq.app.utils.optString
import pk.sufiishq.aurora.components.SILinearProgressIndicator
import pk.sufiishq.aurora.components.SIText
import pk.sufiishq.aurora.components.TextSize
import pk.sufiishq.aurora.layout.SIDialog
import pk.sufiishq.aurora.layout.SIRow
import pk.sufiishq.aurora.theme.AuroraColor
import timber.log.Timber

@PackagePrivate
@Composable
fun KalamDownloadDialog(
    kalamController: KalamController,
) {
    val kalamDownloadState = kalamController.getKalamDownloadState().observeAsState()

    kalamDownloadState.value?.apply {
        when (this) {
            is KalamDownloadState.Started ->
                KalamDownloadStartedDialog(
                    this,
                    kalamController,
                )
            is KalamDownloadState.InProgress ->
                KalamDownloadInProgressDialog(
                    this,
                    kalamController,
                )
            is KalamDownloadState.Error ->
                KalamDownloadErrorDialog(
                    this,
                    kalamController,
                )
            is KalamDownloadState.Completed ->
                KalamDownloadCompletedDialog(
                    this,
                    kalamController,
                )
            else -> Timber.d("$this state is used for only hide dialog")
        }
    }
}

@Composable
private fun KalamDownloadStartedDialog(
    kalamDownloadState: KalamDownloadState.Started,
    kalamController: KalamController,
) {
    ShowDialog(
        title = kalamDownloadState.kalam.title,
        onNoText = optString(R.string.label_cancel),
        onNoClick = { dismissDownload(kalamController) },
    ) {
        SILinearProgressIndicator(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
        )
    }
}

@Composable
private fun KalamDownloadInProgressDialog(
    kalamDownloadState: KalamDownloadState.InProgress,
    kalamController: KalamController,
) {
    val title = kalamDownloadState.kalam.title
    val fileInfo = kalamDownloadState.fileInfo as FileInfo.Downloading

    val progress by
        animateFloatAsState(
            targetValue = fileInfo.progress.toFloat() / 100f * 1f,
            animationSpec =
            tween(
                durationMillis = 800,
                easing = LinearOutSlowInEasing,
            ),
        )

    ShowDialog(
        title = title,
        onNoText = optString(R.string.label_cancel),
        onNoClick = { dismissDownload(kalamController) },
    ) { textColor ->
        SIRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
        ) {
            SIText(
                text = "${fileInfo.progress}% of ",
                textColor = textColor,
                textSize = TextSize.Small,
            )
            SIText(
                text = format("%.2f", fileInfo.totalSize / 1024 / 1024) + " MB",
                textColor = textColor,
                textSize = TextSize.Small,
            )
        }

        SILinearProgressIndicator(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            progress = progress,
        )
    }
}

@Composable
private fun KalamDownloadCompletedDialog(
    kalamDownloadState: KalamDownloadState.Completed,
    kalamController: KalamController,
) {
    val kalam = kalamDownloadState.kalam

    ShowDialog(
        title = kalam.title,
        onYesText = optString(R.string.label_ok),
        onYesClick = { dismissDownload(kalamController) },
    ) { textColor ->
        SIText(
            text =
            buildAnnotatedString {
                append("Kalam ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) { append(kalam.title) }
                append(" successfully downloaded")
            },
            textColor = textColor,
        )
    }
}

@Composable
private fun KalamDownloadErrorDialog(
    kalamDownloadState: KalamDownloadState.Error,
    kalamController: KalamController,
) {
    val kalam = kalamDownloadState.kalam
    val error = kalamDownloadState.error

    ShowDialog(
        title = kalam.title,
        onYesText = optString(R.string.label_ok),
        onYesClick = { dismissDownload(kalamController) },
    ) { textColor ->
        SIText(
            text = optString(R.string.label_download_error),
            textColor = textColor,
            fontWeight = FontWeight.Bold,
        )

        SIText(
            text = error,
            textColor = textColor,
        )
    }
}

@Composable
private fun ShowDialog(
    title: String,
    onNoText: String? = null,
    onNoClick: () -> Unit = {},
    onYesText: String? = null,
    onYesClick: () -> Unit = {},
    content: @Composable ColumnScope.(fgColor: AuroraColor) -> Unit,
) {
    SIDialog(
        title = optString(R.string.dynamic_download_kalam, title),
        onNoText = onNoText,
        onNoClick = onNoClick,
        onYesText = onYesText,
        onYesClick = onYesClick,
    ) { textColor ->
        content(textColor)
    }
}

private fun dismissDownload(kalamController: KalamController) {
    kalamController.dismissDownload()
}
