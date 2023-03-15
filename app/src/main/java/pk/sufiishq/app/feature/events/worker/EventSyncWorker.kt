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

package pk.sufiishq.app.feature.events.worker

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
import pk.sufiishq.app.feature.events.data.repository.EventRepository
import pk.sufiishq.app.feature.storage.KeyValueStorage

@HiltWorker
class EventSyncWorker @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val eventRepository: EventRepository,
    @SharedPreferences private val keyValueStorage: KeyValueStorage,
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        eventRepository.fetchAllEvents()
        setSyncStatus(true, keyValueStorage)
        return Result.success()
    }

    companion object {

        private const val EVENT_SYNCED_KEY = "event_synced"

        fun init(context: Context, keyValueStorage: KeyValueStorage) {
            if (!keyValueStorage.get(EVENT_SYNCED_KEY, false)) {
                val constraints = Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()

                WorkManager.getInstance(context).enqueue(
                    OneTimeWorkRequestBuilder<EventSyncWorker>()
                        .setConstraints(constraints)
                        .build(),
                )
            }
        }

        fun setSyncStatus(status: Boolean, keyValueStorage: KeyValueStorage) {
            keyValueStorage.put(EVENT_SYNCED_KEY, status)
        }
    }
}
