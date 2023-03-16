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

package pk.sufiishq.app.feature.theme.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import pk.sufiishq.app.feature.theme.model.AutoChangeColorDuration
import pk.sufiishq.aurora.config.AuroraConfig

@HiltWorker
class AutoColorChangeWorker @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted workerParameters: WorkerParameters,
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        AuroraConfig.applyRandomPalette(context)
        return Result.success()
    }

    companion object {

        fun init(context: Context, autoChangeColorDuration: AutoChangeColorDuration) {

            val autoColorChangeWorkRequest =
                PeriodicWorkRequestBuilder<AutoColorChangeWorker>(
                    autoChangeColorDuration.repeatInterval,
                    autoChangeColorDuration.repeatIntervalTimeUnit
                )
                    .setInitialDelay(
                        autoChangeColorDuration.repeatInterval,
                        autoChangeColorDuration.repeatIntervalTimeUnit
                    )
                    .build()

            WorkManager.getInstance(context)
                .enqueueUniquePeriodicWork(
                    AutoColorChangeWorker::class.java.simpleName,
                    ExistingPeriodicWorkPolicy.REPLACE,
                    autoColorChangeWorkRequest,
                )
        }

        fun cancel(context: Context) {
            WorkManager.getInstance(context)
                .cancelUniqueWork(AutoColorChangeWorker::class.java.simpleName)
        }
    }
}
