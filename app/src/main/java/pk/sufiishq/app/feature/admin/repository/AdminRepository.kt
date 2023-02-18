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

package pk.sufiishq.app.feature.admin.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import pk.sufiishq.app.R
import pk.sufiishq.app.feature.admin.FirebaseDatabaseReference
import pk.sufiishq.app.feature.admin.FirebaseDatabaseStatus
import pk.sufiishq.app.feature.admin.model.Highlight
import pk.sufiishq.app.feature.admin.model.Maintenance
import pk.sufiishq.app.di.qualifier.IoDispatcher
import pk.sufiishq.app.utils.getString
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class AdminRepository
@Inject
constructor(
    private val firebaseDatabase: FirebaseDatabase,
    @IoDispatcher private val dispatcher: CoroutineContext,
) {

    suspend fun addOrUpdateHighlight(highlight: Highlight): FirebaseDatabaseStatus {
        return safeCall {
            firebaseDatabase.getReference(FirebaseDatabaseReference.HIGHLIGHT).setValue(highlight).await()
            FirebaseDatabaseStatus.Write(message = getString(R.string.msg_highlight_done))
        }
    }

    suspend fun readHighlight(): FirebaseDatabaseStatus {
        return safeCall {
            val data = firebaseDatabase.getReference(FirebaseDatabaseReference.HIGHLIGHT).get().await()
            val highlight =
                Highlight(
                    startDateTime =
                    data
                        .child(FirebaseDatabaseReference.START_DATE_TIME)
                        .getValue(Long::class.java)!!,
                    endDateTime =
                    data.child(FirebaseDatabaseReference.END_DATE_TIME).getValue(Long::class.java)!!,
                    title = data.child(FirebaseDatabaseReference.TITLE).getValue(String::class.java),
                    detail = data.child(FirebaseDatabaseReference.DETAIL).getValue(String::class.java)!!,
                    contacts = resolveContacts(data),
                )
            FirebaseDatabaseStatus.ReadHighlight(highlight)
        }
    }

    suspend fun deleteHighlight(): FirebaseDatabaseStatus {
        return safeCall {
            firebaseDatabase.getReference(FirebaseDatabaseReference.HIGHLIGHT).removeValue().await()
            FirebaseDatabaseStatus.Delete()
        }
    }

    suspend fun getValidEmails(): String {
        return withContext(dispatcher) {
            firebaseDatabase
                .getReference(FirebaseDatabaseReference.VALID_EMAILS)
                .get()
                .await()
                .getValue(String::class.java)!!
        }
    }

    suspend fun getValidDeveloperEmails(): String {
        return withContext(dispatcher) {
            firebaseDatabase
                .getReference(FirebaseDatabaseReference.VALID_DEVELOPER_EMAILS)
                .get()
                .await()
                .getValue(String::class.java)!!
        }
    }

    suspend fun readMaintenance(): FirebaseDatabaseStatus {
        return safeCall {
            val data = firebaseDatabase.getReference(FirebaseDatabaseReference.MAINTENANCE).get().await()
            val activeStatus =
                data.child(FirebaseDatabaseReference.ACTIVE).getValue(Boolean::class.java)!!
            val strictMode = data.child(FirebaseDatabaseReference.STRICT).getValue(Boolean::class.java)!!
            FirebaseDatabaseStatus.ReadMaintenance(
                Maintenance(
                    activeStatus = activeStatus,
                    strictMode = strictMode,
                ),
            )
        }
    }

    suspend fun updateMaintenance(key: String, value: Boolean): FirebaseDatabaseStatus {
        return safeCall {
            firebaseDatabase
                .getReference(FirebaseDatabaseReference.MAINTENANCE)
                .child(key)
                .setValue(value)
                .await()
            FirebaseDatabaseStatus.Write(message = getString(R.string.msg_maintenance_status_done))
        }
    }

    private fun resolveContacts(data: DataSnapshot): Map<String, Map<String, String>>? {
        return try {
            val contacts = data.child(FirebaseDatabaseReference.CONTACTS)
            val name = contacts.child(FirebaseDatabaseReference.NAME).children
            val number = contacts.child(FirebaseDatabaseReference.NUMBER).children
            mapOf(
                FirebaseDatabaseReference.NAME to
                    name
                        .mapIndexed { index, value ->
                            index.toString() to
                                value.getValue(
                                    String::class.java,
                                )!!
                        }
                        .toMap(),
                FirebaseDatabaseReference.NUMBER to
                    number
                        .mapIndexed { index, value ->
                            index.toString() to
                                value.getValue(
                                    String::class.java,
                                )!!
                        }
                        .toMap(),
            )
        } catch (ex: Exception) {
            null
        }
    }

    private suspend fun safeCall(
        block: suspend (CoroutineScope) -> FirebaseDatabaseStatus,
    ): FirebaseDatabaseStatus {
        return withContext(dispatcher) {
            try {
                block(this)
            } catch (ex: Exception) {
                FirebaseDatabaseStatus.Failed(ex)
            }
        }
    }
}
