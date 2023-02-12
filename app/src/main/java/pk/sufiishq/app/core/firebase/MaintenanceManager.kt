package pk.sufiishq.app.core.firebase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import pk.sufiishq.app.data.repository.AdminSettingsRepository
import pk.sufiishq.app.di.qualifier.IoDispatcher
import kotlin.coroutines.CoroutineContext

class MaintenanceManager @Inject constructor(
    private val adminSettingsRepository: AdminSettingsRepository,
    @IoDispatcher private val dispatcher: CoroutineContext
) {

    private val activeStatus = MutableLiveData(false)
    private val strictMode = MutableLiveData(false)

    init {
        fetch()
    }

    private fun fetch() {
        inCoroutine {
            adminSettingsRepository
                .readMaintenance()
                .takeIf { it is FirebaseDatabaseStatus.ReadMaintenance }
                ?.let {
                    it as FirebaseDatabaseStatus.ReadMaintenance
                    activeStatus.postValue(it.activeStatus)
                    strictMode.postValue(it.strictMode)
                }
        }
    }

    suspend fun setActiveStatus(status: Boolean): FirebaseDatabaseStatus {
        val firebaseDatabaseStatus = adminSettingsRepository.updateMaintenance("active", status)
        activeStatus.postValue(
            if (firebaseDatabaseStatus is FirebaseDatabaseStatus.Failed) !status else status
        )

        return firebaseDatabaseStatus
    }

    fun getActiveStatus(): LiveData<Boolean> {
        return activeStatus
    }

    suspend fun setStrictMode(mode: Boolean): FirebaseDatabaseStatus {

        val firebaseDatabaseStatus = adminSettingsRepository.updateMaintenance("strict", mode)
        strictMode.postValue(
            if (firebaseDatabaseStatus is FirebaseDatabaseStatus.Failed) !mode else mode
        )

        return firebaseDatabaseStatus
    }

    fun getStrictMode(): LiveData<Boolean> {
        return strictMode
    }

    private fun inCoroutine(block: suspend CoroutineScope.() -> Unit) {
        CoroutineScope(dispatcher).launch {
            block()
        }
    }
}