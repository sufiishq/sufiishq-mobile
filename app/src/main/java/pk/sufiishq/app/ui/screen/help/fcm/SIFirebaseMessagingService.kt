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

package pk.sufiishq.app.ui.screen.help.fcm

import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import pk.sufiishq.app.feature.occasions.worker.OccasionSyncWorker
import timber.log.Timber

class SIFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.d("New Token Received: $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        // Check if message contains a notification payload.
        message.notification?.let {
            Timber.d("Message Notification Body: ${it.body}")
            // it.body?.let { body -> sendNotification(body) }
        }

        message
            .data
            .takeIf { it.isNotEmpty() && it.containsKey(ACTION_KEY) }
            ?.let {
                when (it[ACTION_KEY]) {
                    ACTION_FETCH_OCCASIONS -> fetchOccasions()
                }
            }
    }

    private fun fetchOccasions() {
        WorkManager.getInstance(this).enqueue(
            OneTimeWorkRequestBuilder<OccasionSyncWorker>().build(),
        )
    }

    companion object {
        const val ACTION_KEY = "action"
        const val ACTION_FETCH_OCCASIONS = "fetch_occasions"
    }
}
