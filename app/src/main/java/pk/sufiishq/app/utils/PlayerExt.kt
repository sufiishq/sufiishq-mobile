package pk.sufiishq.app.utils

import android.content.Context
import android.content.Intent
import android.os.Build
import pk.sufiishq.app.core.player.AudioPlayer

fun AudioPlayer.startPlayerService(context: Context, playerIntent: Intent) {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        context.startForegroundService(playerIntent)
    } else {
        context.startService(playerIntent)
    }
}