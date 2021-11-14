package pk.sufiishq.app

import android.app.Application
import android.content.Intent
import android.os.Build

class SufiIshqApp: Application() {

    private val playerIntent by lazy { Intent(this, AudioPlayerService::class.java) }

    override fun onCreate() {
        super.onCreate()
        instance = this

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(playerIntent)
        }
        else {
            startService(playerIntent)
        }*/
        startService(playerIntent)
    }

    companion object {
        private lateinit var instance: SufiIshqApp

        fun getInstance(): SufiIshqApp {
            return instance
        }
    }
}