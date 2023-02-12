package pk.sufiishq.app.core.kalam.downloader

import java.io.File

sealed interface FileInfo  {
    class Downloading(
        val downloadingFile: File,
        val totalSize: Double,
        val progress: Int = 0
    ) : FileInfo

    class Failed(val throwable: Throwable) : FileInfo
    object Finished: FileInfo
}