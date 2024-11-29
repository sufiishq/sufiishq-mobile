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

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import pk.sufiishq.app.di.qualifier.IoDispatcher
import timber.log.Timber
import java.io.File
import java.lang.RuntimeException
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class FileDownloader
@Inject
constructor(
    @IoDispatcher private val dispatchers: CoroutineContext,
    private val downloadFileService: DownloadFileService,
) {

    fun download(url: String, outFile: File): Flow<FileInfo> {
        return flow {
            try {
                val response = downloadFileService.downloadFile(url)

                response.byteStream().use { inputStream ->
                    val length = response.contentLength().toDouble()
                    if (length < 1) throw RuntimeException("Invalid file length")

                    outFile.outputStream().use { outputStream ->
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
                                        progress = progress,
                                    ),
                                )
                            }
                        }
                    }
                }
                emit(FileInfo.Finished)
            } catch (e: Exception) {
                emit(FileInfo.Failed(e))
            }
        }
            .flowOn(dispatchers)
    }
}
