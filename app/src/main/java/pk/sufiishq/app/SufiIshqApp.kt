package pk.sufiishq.app

import android.app.Application
import android.content.Intent
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.HiltAndroidApp
import pk.sufiishq.app.services.AudioPlayerService
import pk.sufiishq.app.services.PlayerController
import pk.sufiishq.app.worker.CacheRemoveWorker
import timber.log.Timber
import java.util.concurrent.TimeUnit

@HiltAndroidApp
class SufiIshqApp : Application() {

    private val playerIntent by lazy { Intent(this, AudioPlayerService::class.java) }
    private var playerController: PlayerController? = null

    override fun onCreate() {
        super.onCreate()
        instance = this

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())

        initCacheRemoveWorkRequest()

        startService(playerIntent)
    }

    private fun initCacheRemoveWorkRequest() {
        val cacheRemoveWorkRequest =
            PeriodicWorkRequestBuilder<CacheRemoveWorker>(24, TimeUnit.HOURS).build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            CacheRemoveWorker.TAG,
            ExistingPeriodicWorkPolicy.KEEP,
            cacheRemoveWorkRequest
        )
    }

    fun setPlayerController(playerController: PlayerController?) {
        this.playerController = playerController
    }

    fun getPlayerController() = playerController

    companion object {
        private lateinit var instance: SufiIshqApp

        fun getInstance(): SufiIshqApp {
            return instance
        }
    }
}