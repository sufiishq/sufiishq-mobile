package pk.sufiishq.app.core.downloader

import pk.sufiishq.app.models.FileInfo
import pk.sufiishq.app.models.Kalam

sealed interface KalamDownloadState {

    object Idle : KalamDownloadState
    class Started(val kalam: Kalam) : KalamDownloadState
    class InProgress(val fileInfo: FileInfo, val kalam: Kalam) : KalamDownloadState
    class Error(val error: String, val kalam: Kalam) : KalamDownloadState
    class Completed(val kalam: Kalam) : KalamDownloadState
}