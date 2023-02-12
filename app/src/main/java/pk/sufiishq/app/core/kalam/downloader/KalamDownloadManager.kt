package pk.sufiishq.app.core.kalam.downloader

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.net.SocketException
import java.net.UnknownHostException
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.launch
import org.apache.commons.io.FilenameUtils
import pk.sufiishq.app.R
import pk.sufiishq.app.data.repository.KalamRepository
import pk.sufiishq.app.di.qualifier.IoDispatcher
import pk.sufiishq.app.models.Kalam
import pk.sufiishq.app.utils.KALAM_DIR
import pk.sufiishq.app.utils.appendPath
import pk.sufiishq.app.utils.getString
import pk.sufiishq.app.utils.isOfflineFileExists
import pk.sufiishq.app.utils.moveTo
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

class KalamDownloadManager @Inject constructor(
    @ApplicationContext private val appContext: Context,
    @IoDispatcher private val dispatcher: CoroutineContext,
    private val fileDownloader: FileDownloader,
    private val kalamRepository: KalamRepository
) {

    private val kalamDownloadState = MutableLiveData<KalamDownloadState>(KalamDownloadState.Idle)
    private var job: Job? = null

    fun getKalamDownloadState(): LiveData<KalamDownloadState> {
        return kalamDownloadState
    }

    fun startDownload(kalam: Kalam) {
        setKalamDownloadState(KalamDownloadState.Started(kalam))

        job = CoroutineScope(dispatcher).launch {
            fileDownloader
                .download(kalam.onlineSource, getTempFile(getFileName(kalam)))
                .cancellable()
                .collect {
                    when (it) {
                        is FileInfo.Downloading -> downloading(it, kalam)
                        is FileInfo.Finished -> downloadFinished(kalam)
                        is FileInfo.Failed -> downloadFailed(it, kalam)
                    }
                }
        }
    }

    fun dismissDownload() {
        setKalamDownloadState(KalamDownloadState.Idle)
        job?.cancel()
    }

    private fun downloading(fileInfo: FileInfo.Downloading, kalam: Kalam) {
        setKalamDownloadState(KalamDownloadState.InProgress(fileInfo, kalam))
    }

    private suspend fun downloadFinished(kalam: Kalam) {

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

        setKalamDownloadState(KalamDownloadState.Completed(kalam))
    }

    private fun downloadFailed(fileInfo: FileInfo.Failed, kalam: Kalam) {

        val throwable = fileInfo.throwable
        Timber.e(throwable)

        val error = if (throwable is SocketException || throwable is UnknownHostException) {
            getString(R.string.msg_no_network_connection)
        } else {
            throwable.localizedMessage ?: throwable.message
            ?: getString(R.string.label_unknown_error)
        }

        setKalamDownloadState(KalamDownloadState.Error(error, kalam))
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