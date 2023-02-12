package pk.sufiishq.app.utils

import android.content.Context
import java.io.File
import pk.sufiishq.app.R
import pk.sufiishq.app.models.Kalam

fun Kalam?.hasOfflineSource() = this?.offlineSource?.isNotEmpty() ?: false

fun Kalam?.canPlay(context: Context): Boolean {
    return if (!this.hasOfflineSource() && !context.isNetworkAvailable()) {
        quickToast(R.string.msg_no_network_connection)
        false
    } else true
}

fun Kalam.isOfflineFileExists(): Boolean {
    return File(getApp().filesDir.absolutePath + File.separator + offlineSource).exists()
}

fun Kalam.offlineFile(): File? {
    return takeIf { offlineSource.isNotEmpty() }?.let {
        File(buildString {
            append(getApp().filesDir)
            append(File.separator)
            append(it.offlineSource)
        })
    }
}