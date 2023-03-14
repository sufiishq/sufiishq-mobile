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
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import pk.sufiishq.app.feature.app.AppNotificationManager
import pk.sufiishq.app.feature.events.data.repository.EventRepository
import pk.sufiishq.app.feature.events.model.Event
import pk.sufiishq.app.utils.TextRes
import pk.sufiishq.app.utils.extention.notify
import java.util.concurrent.TimeUnit

@HiltWorker
class EventUpdateWorker @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val eventRepository: EventRepository,
    private val appNotificationManager: AppNotificationManager,
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        eventRepository.invalidateAllEvents()

        eventRepository.getUpcomingEvents()
            .filter { it.enableAlert }
            .forEach { event ->
                context.notify(
                    event.id.toInt(),
                    appNotificationManager.make(
                        title = event.title,
                        content = getContent(event, context),
                    ).build(),
                )
            }

        return Result.success()
    }

    private fun getContent(event: Event, context: Context): String {
        return if (event.remainingDays == 0) {
            context.getString(TextRes.label_now)
        } else {
            context.getString(TextRes.dynamic_event_days_remaining).format(event.remainingDays)
        }
    }

    companion object {

        fun init(context: Context) {
            val eventUpdateWorkRequest =
                PeriodicWorkRequestBuilder<EventUpdateWorker>(24, TimeUnit.HOURS).build()

            WorkManager.getInstance(context)
                .enqueueUniquePeriodicWork(
                    EventUpdateWorker::class.java.simpleName,
                    ExistingPeriodicWorkPolicy.KEEP,
                    eventUpdateWorkRequest,
                )
        }
    }
}
