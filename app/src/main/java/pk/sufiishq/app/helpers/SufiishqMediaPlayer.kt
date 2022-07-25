package pk.sufiishq.app.helpers

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import pk.sufiishq.app.models.Kalam

class SufiishqMediaPlayer : MediaPlayer() {

    fun setDataSource(context: Context, kalam: Kalam) {
        if (canPlayOffline(kalam)) {
            setDataSource(context.filesDir.absolutePath + "/" + kalam.offlineSource)
        } else {
            setDataSource(context, Uri.parse(kalam.onlineSource))
        }
    }

    fun canPlayOffline(kalam: Kalam?): Boolean {
        return kalam?.offlineSource?.isNotEmpty() ?: false
    }
}