package pk.sufiishq.app.data.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import pk.sufiishq.app.R
import pk.sufiishq.app.core.firebase.FirebaseDatabaseReference
import pk.sufiishq.app.core.firebase.FirebaseDatabaseStatus
import pk.sufiishq.app.di.qualifier.IoDispatcher
import pk.sufiishq.app.models.Highlight
import pk.sufiishq.app.utils.getString
import kotlin.coroutines.CoroutineContext

class AdminSettingsRepository @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase,
    @IoDispatcher private val dispatcher: CoroutineContext
) {

    suspend fun addOrUpdateHighlight(highlight: Highlight): FirebaseDatabaseStatus {
        return safeCall {
            firebaseDatabase.getReference(FirebaseDatabaseReference.HIGHLIGHT).setValue(highlight)
                .await()
            FirebaseDatabaseStatus.Write(message = getString(R.string.msg_highlight_done))
        }
    }

    suspend fun readHighlight(): FirebaseDatabaseStatus {
        return safeCall {
            val data =
                firebaseDatabase.getReference(FirebaseDatabaseReference.HIGHLIGHT).get().await()
            val highlight = Highlight(
                startDateTime = data.child(FirebaseDatabaseReference.START_DATE_TIME)
                    .getValue(Long::class.java)!!,
                endDateTime = data.child(FirebaseDatabaseReference.END_DATE_TIME)
                    .getValue(Long::class.java)!!,
                title = data.child(FirebaseDatabaseReference.TITLE).getValue(String::class.java)!!,
                detail = data.child(FirebaseDatabaseReference.DETAIL)
                    .getValue(String::class.java)!!,
                contacts = resolveContacts(data)
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
            firebaseDatabase.getReference(FirebaseDatabaseReference.VALID_EMAILS)
                .get().await().getValue(String::class.java)!!
        }
    }

    suspend fun getValidDeveloperEmails(): String {
        return withContext(dispatcher) {
            firebaseDatabase.getReference(FirebaseDatabaseReference.VALID_DEVELOPER_EMAILS)
                .get().await().getValue(String::class.java)!!
        }
    }

    suspend fun readMaintenance(): FirebaseDatabaseStatus {
        return safeCall {
            val data =
                firebaseDatabase.getReference(FirebaseDatabaseReference.MAINTENANCE).get().await()
            val activeStatus =
                data.child(FirebaseDatabaseReference.ACTIVE).getValue(Boolean::class.java)!!
            val strictMode =
                data.child(FirebaseDatabaseReference.STRICT).getValue(Boolean::class.java)!!
            FirebaseDatabaseStatus.ReadMaintenance(activeStatus, strictMode)
        }
    }

    suspend fun updateMaintenance(key: String, value: Boolean): FirebaseDatabaseStatus {
        return safeCall {
            firebaseDatabase.getReference(FirebaseDatabaseReference.MAINTENANCE).child(key)
                .setValue(value).await()
            FirebaseDatabaseStatus.Write(message = getString(R.string.msg_maintenance_status_done))
        }
    }

    private fun resolveContacts(data: DataSnapshot): Map<String, Map<String, String>>? {
        return try {
            val contacts = data.child(FirebaseDatabaseReference.CONTACTS)
            val name = contacts.child(FirebaseDatabaseReference.NAME).children
            val number = contacts.child(FirebaseDatabaseReference.NUMBER).children
            mapOf(
                FirebaseDatabaseReference.NAME to name.mapIndexed { index, value ->
                    index.toString() to value.getValue(
                        String::class.java
                    )!!
                }.toMap(),
                FirebaseDatabaseReference.NUMBER to number.mapIndexed { index, value ->
                    index.toString() to value.getValue(
                        String::class.java
                    )!!
                }.toMap(),
            )
        } catch (ex: Exception) {
            null
        }
    }

    private suspend fun safeCall(block: suspend (CoroutineScope) -> FirebaseDatabaseStatus): FirebaseDatabaseStatus {
        return withContext(dispatcher) {
            try {
                block(this)
            } catch (ex: Exception) {
                FirebaseDatabaseStatus.Failed(ex)
            }
        }
    }
}