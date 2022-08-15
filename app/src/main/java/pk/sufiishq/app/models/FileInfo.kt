package pk.sufiishq.app.models

import java.io.File

data class FileInfo(
    val downloadingFile: File,
    val totalSize: Double,
    var progress: Int = 0
)
