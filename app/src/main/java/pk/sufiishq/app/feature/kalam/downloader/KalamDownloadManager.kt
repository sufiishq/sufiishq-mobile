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

package pk.sufiishq.app.feature.kalam.downloader

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.launch
import org.apache.commons.io.FilenameUtils
import pk.sufiishq.app.di.qualifier.IoDispatcher
import pk.sufiishq.app.feature.kalam.data.repository.KalamRepository
import pk.sufiishq.app.feature.kalam.extension.isOfflineFileExists
import pk.sufiishq.app.feature.kalam.model.Kalam
import pk.sufiishq.app.utils.Constants.KALAM_DIR
import pk.sufiishq.app.utils.TextRes
import pk.sufiishq.app.utils.extention.appendPath
import pk.sufiishq.app.utils.extention.moveTo
import pk.sufiishq.app.utils.getString
import timber.log.Timber
import java.io.File
import java.net.SocketException
import java.net.UnknownHostException
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class KalamDownloadManager
@Inject
constructor(
    @ApplicationContext private val appContext: Context,
    @IoDispatcher private val dispatcher: CoroutineContext,
    private val fileDownloader: FileDownloader,
    private val kalamRepository: KalamRepository,
) {

    private val kalamDownloadState = MutableLiveData<KalamDownloadState>(KalamDownloadState.Idle(false))
    private var job: Job? = null

    fun getKalamDownloadState(): LiveData<KalamDownloadState> {
        return kalamDownloadState
    }

    fun startDownload(kalam: Kalam, silent: Boolean) {
        setKalamDownloadState(KalamDownloadState.Started(kalam, silent))

        job =
            CoroutineScope(dispatcher).launch {
                fileDownloader
                    .download(kalam.onlineSource, getTempFile(getFileName(kalam)))
                    .cancellable()
                    .collect {
                        when (it) {
                            is FileInfo.Downloading -> downloading(it, kalam, silent)
                            is FileInfo.Finished -> downloadFinished(kalam, silent)
                            is FileInfo.Failed -> downloadFailed(it, kalam, silent)
                        }
                    }
            }
    }

    fun dismissDownload() {
        job?.cancel()
        setKalamDownloadState(KalamDownloadState.Idle(false))
    }

    private fun downloading(fileInfo: FileInfo.Downloading, kalam: Kalam, silent: Boolean) {
        setKalamDownloadState(KalamDownloadState.InProgress(fileInfo, kalam, silent))
    }

    private suspend fun downloadFinished(kalam: Kalam, silent: Boolean) {
        val fileName = getFileName(kalam)
        val source = getTempFile(fileName)
        val destination = getDestFile(fileName)

        source.moveTo(destination)

        kalam.offlineSource = KALAM_DIR + File.separator + fileName

        if (kalam.isOfflineFileExists()) {
            kalamRepository.update(kalam)
        } else {
            kalam.offlineSource = ""
        }

        setKalamDownloadState(KalamDownloadState.Completed(kalam, silent))
    }

    private fun downloadFailed(fileInfo: FileInfo.Failed, kalam: Kalam, silent: Boolean) {
        val throwable = fileInfo.throwable
        Timber.e(throwable)

        val error =
            if (throwable is SocketException || throwable is UnknownHostException) {
                getString(TextRes.msg_no_network_connection)
            } else {
                throwable.localizedMessage ?: throwable.message
                    ?: getString(TextRes.label_unknown_error)
            }

        setKalamDownloadState(KalamDownloadState.Error(error, kalam, silent))
    }

    private fun setKalamDownloadState(state: KalamDownloadState) {
        kalamDownloadState.postValue(state)
    }

    private fun getKalamDir(): File {
        val kalamDir = getFilesDir().appendPath(KALAM_DIR)
        if (!kalamDir.exists()) kalamDir.mkdir()
        return kalamDir
    }

    private fun getFileName(kalam: Kalam) = FilenameUtils.getName(kalam.onlineSource)
    private fun getDestFile(fileName: String) = getKalamDir().appendPath(fileName)
    private fun getTempFile(fileName: String) = getCacheDir().appendPath(fileName)
    private fun getCacheDir() = appContext.cacheDir
    private fun getFilesDir(): File = appContext.filesDir
}
