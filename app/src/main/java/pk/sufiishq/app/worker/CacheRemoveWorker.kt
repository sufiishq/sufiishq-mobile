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

package pk.sufiishq.app.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import pk.sufiishq.app.utils.extention.deleteContent
import java.util.concurrent.TimeUnit

@HiltWorker
class CacheRemoveWorker @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted workerParam: WorkerParameters,
) : Worker(context, workerParam) {

    override fun doWork(): Result {
        applicationContext.cacheDir.deleteContent()
        return Result.success()
    }

    companion object {
        fun init(context: Context) {
            val cacheRemoveWorkRequest =
                PeriodicWorkRequestBuilder<CacheRemoveWorker>(24, TimeUnit.HOURS).build()

            WorkManager.getInstance(context)
                .enqueueUniquePeriodicWork(
                    CacheRemoveWorker::class.java.simpleName,
                    ExistingPeriodicWorkPolicy.KEEP,
                    cacheRemoveWorkRequest,
                )
        }
    }
}
