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

package pk.sufiishq.app.feature.occasions.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import pk.sufiishq.app.di.qualifier.SharedPreferences
import pk.sufiishq.app.feature.app.AppNotificationManager
import pk.sufiishq.app.feature.occasions.data.repository.OccasionRepository
import pk.sufiishq.app.feature.occasions.model.Occasion
import pk.sufiishq.app.feature.storage.KeyValueStorage
import pk.sufiishq.app.utils.TextRes
import pk.sufiishq.app.utils.extention.notify
import pk.sufiishq.app.utils.getString

@HiltWorker
class OccasionSyncWorker @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val appNotificationManager: AppNotificationManager,
    private val occasionRepository: OccasionRepository,
    @SharedPreferences private val keyValueStorage: KeyValueStorage,
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        val currentCount = occasionRepository.getCount()

        // fetch old occasions first
        occasionRepository.fetchOccasions(
            offset = 1,
            requestType = Occasion.PAST,
            timestamp = occasionRepository.getLowestTimestamp() ?: -1L,
        )

        // then fetch latest occasions
        occasionRepository.fetchOccasions(
            offset = 1,
            requestType = Occasion.LATEST,
            timestamp = occasionRepository.getHighestTimestamp() ?: -1L,
        )

        val nowCount = occasionRepository.getCount()

        if (currentCount != nowCount) {
            context.notify(
                2,
                appNotificationManager.make(
                    title = getString(TextRes.app_name),
                    content = getString(TextRes.msg_occasions_updated),
                ).build(),
            )
        }

        setSyncStatus(true, keyValueStorage)

        return Result.success()
    }

    companion object {

        private const val OCCASION_SYNCED_KEY = "occasion_synced"

        fun init(context: Context, keyValueStorage: KeyValueStorage) {
            if (!keyValueStorage.get(OCCASION_SYNCED_KEY, false)) {
                val constraints = Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()

                WorkManager.getInstance(context).enqueue(
                    OneTimeWorkRequestBuilder<OccasionSyncWorker>()
                        .setConstraints(constraints)
                        .build(),
                )
            }
        }

        fun setSyncStatus(status: Boolean, keyValueStorage: KeyValueStorage) {
            keyValueStorage.put(OCCASION_SYNCED_KEY, status)
        }
    }
}
