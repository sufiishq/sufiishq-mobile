package pk.sufiishq.app

import android.app.Application
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import pk.sufiishq.app.core.storage.KeyValueStorage
import pk.sufiishq.app.di.qualifier.SecureSharedPreferences
import pk.sufiishq.app.worker.CacheRemoveWorker
import timber.log.Timber
import kotlin.math.log

@HiltAndroidApp
class SufiIshqApp : Application() {

    @Inject
    @SecureSharedPreferences
    lateinit var keyValueStorage: KeyValueStorage


    override fun onCreate() {
        super.onCreate()
        instance = this
        logDebugAppCheckToken()

        // enable crashlytics only in release build
        FirebaseCrashlytics.getInstance()
            .setCrashlyticsCollectionEnabled(BuildConfig.ENABLE_CRASHLYTICS)

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

    private fun logDebugAppCheckToken() {
        FirebaseApp.initializeApp(this)
        val firebaseAppCheck = FirebaseAppCheck.getInstance()
        firebaseAppCheck.installAppCheckProviderFactory(
            DebugAppCheckProviderFactory.getInstance()
        )
    }

    companion object {
        private lateinit var instance: SufiIshqApp

        fun getInstance(): SufiIshqApp {
            return instance
        }
    }
}