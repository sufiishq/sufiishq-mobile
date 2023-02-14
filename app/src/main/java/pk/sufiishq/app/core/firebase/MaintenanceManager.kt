package pk.sufiishq.app.core.firebase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import pk.sufiishq.app.core.player.AudioPlayer
import pk.sufiishq.app.data.repository.AdminSettingsRepository
import pk.sufiishq.app.di.qualifier.AndroidMediaPlayer
import pk.sufiishq.app.di.qualifier.IoDispatcher
import pk.sufiishq.app.models.Maintenance
import kotlin.coroutines.CoroutineContext

class MaintenanceManager @Inject constructor(
    private val adminSettingsRepository: AdminSettingsRepository,
    @AndroidMediaPlayer private val player: AudioPlayer,
    @IoDispatcher private val dispatcher: CoroutineContext
) {

    private val maintenance = MutableLiveData<Maintenance?>()

    init {
        //fetch()
    }

    private fun fetch() {
        inCoroutine {
            adminSettingsRepository
                .readMaintenance()
                .takeIf { it is FirebaseDatabaseStatus.ReadMaintenance }
                ?.let { fbStatus ->
                    fbStatus as FirebaseDatabaseStatus.ReadMaintenance
                    maintenance.postValue(
                        fbStatus.maintenance.also {
                            if (it.activeStatus && it.strictMode && player.isPlaying()) {
                                player.doPlayOrPause()
                            }
                        }
                    )
                }
        }
    }

    suspend fun setActiveStatus(status: Boolean): FirebaseDatabaseStatus {
        val firebaseDatabaseStatus = adminSettingsRepository.updateMaintenance("active", status)
        maintenance.postValue(
            maintenance.value?.copy(
                activeStatus = if (firebaseDatabaseStatus is FirebaseDatabaseStatus.Failed) !status else status
            )
        )

        return firebaseDatabaseStatus
    }

    suspend fun setStrictMode(mode: Boolean): FirebaseDatabaseStatus {

        val firebaseDatabaseStatus = adminSettingsRepository.updateMaintenance("strict", mode)
        maintenance.postValue(
            maintenance.value?.copy(
                strictMode = if (firebaseDatabaseStatus is FirebaseDatabaseStatus.Failed) !mode else mode
            )
        )

        return firebaseDatabaseStatus
    }

    fun getMaintenance(): LiveData<Maintenance?> {
        return maintenance
    }

    private fun inCoroutine(block: suspend CoroutineScope.() -> Unit) {
        CoroutineScope(dispatcher).launch {
            block()
        }
    }
}