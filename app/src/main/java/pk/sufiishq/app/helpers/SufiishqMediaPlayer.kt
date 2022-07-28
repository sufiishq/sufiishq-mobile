package pk.sufiishq.app.helpers

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import pk.sufiishq.app.models.Kalam
import pk.sufiishq.app.utils.hasOfflineSource

class SufiishqMediaPlayer : MediaPlayer() {

    fun setDataSource(context: Context, kalam: Kalam) {
        if (kalam.hasOfflineSource()) {
            setDataSource(context.filesDir.absolutePath + "/" + kalam.offlineSource)
        } else {
            setDataSource(context, Uri.parse(kalam.onlineSource))
        }
    }
}