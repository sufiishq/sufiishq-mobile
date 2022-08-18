package pk.sufiishq.app

import android.app.Application
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.HiltAndroidApp
import pk.sufiishq.app.configs.AppConfig
import pk.sufiishq.app.core.storage.KeyValueStorage
import pk.sufiishq.app.di.qualifier.SecureSharedPreferences
import pk.sufiishq.app.worker.CacheRemoveWorker
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class SufiIshqApp : Application() {

    @Inject
    @SecureSharedPreferences
    lateinit var keyValueStorage: KeyValueStorage

    lateinit var appConfig: AppConfig

    override fun onCreate() {
        super.onCreate()
        instance = this
        appConfig = AppConfig()

        // enable crashlytics only in release build
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG)

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())

        initCacheRemoveWorkRequest()

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

    companion object {
        private lateinit var instance: SufiIshqApp

        fun getInstance(): SufiIshqApp {
            return instance
        }
    }
}