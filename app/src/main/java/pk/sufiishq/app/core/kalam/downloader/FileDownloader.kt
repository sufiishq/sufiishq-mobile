package pk.sufiishq.app.core.kalam.downloader

import java.io.File
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import pk.sufiishq.app.di.qualifier.IoDispatcher
import kotlin.coroutines.CoroutineContext

class FileDownloader @Inject constructor(
    @IoDispatcher private val dispatchers: CoroutineContext,
    private val downloadFileService: DownloadFileService
) {

    fun download(url: String, outFile: File): Flow<FileInfo> {

        return flow {

            try {
                val response = downloadFileService.downloadFile(url)

                response
                    .byteStream()
                    .use { inputStream ->

                        val length = response.contentLength().toDouble()

                        outFile
                            .outputStream()
                            .use { outputStream ->

                                val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                                var bytesCopied = 0L
                                var bytes = inputStream.read(buffer)
                                var progress = 0

                                while (bytes >= 0) {

                                    outputStream.write(buffer, 0, bytes)
                                    bytesCopied += bytes
                                    bytes = inputStream.read(buffer)
                                    val currentProgress = (bytesCopied / length * 100.toDouble()).toInt()

                                    if (currentProgress > progress) {
                                        progress = currentProgress
                                        emit(
                                            FileInfo.Downloading(
                                                downloadingFile = outFile,
                                                totalSize = length,
                                                progress = progress
                                            )
                                        )
                                    }
                                }
                            }
                    }
                emit(FileInfo.Finished)
            } catch (e: Exception) {
                emit(FileInfo.Failed(e))
            }
        }.flowOn(dispatchers)
    }
}