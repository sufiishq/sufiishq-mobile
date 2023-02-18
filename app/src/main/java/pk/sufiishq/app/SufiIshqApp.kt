/*
 * Copyright 2022-2023 SufiIshq
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
import pk.sufiishq.app.feature.storage.KeyValueStorage
import pk.sufiishq.app.di.qualifier.SecureSharedPreferences
import pk.sufiishq.app.worker.CacheRemoveWorker
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class SufiIshqApp : Application() {

    @Inject @SecureSharedPreferences
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
        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(
                CacheRemoveWorker.TAG,
                ExistingPeriodicWorkPolicy.KEEP,
                cacheRemoveWorkRequest,
            )
    }

    private fun logDebugAppCheckToken() {
        FirebaseApp.initializeApp(this)
        val firebaseAppCheck = FirebaseAppCheck.getInstance()
        firebaseAppCheck.installAppCheckProviderFactory(
            DebugAppCheckProviderFactory.getInstance(),
        )
    }

    companion object {
        private lateinit var instance: SufiIshqApp

        fun getInstance(): SufiIshqApp {
            return instance
        }
    }
}
