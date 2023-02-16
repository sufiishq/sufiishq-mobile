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

package pk.sufiishq.app.core.firebase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import pk.sufiishq.app.core.player.AudioPlayer
import pk.sufiishq.app.data.repository.AdminSettingsRepository
import pk.sufiishq.app.di.qualifier.AndroidMediaPlayer
import pk.sufiishq.app.di.qualifier.IoDispatcher
import pk.sufiishq.app.models.Maintenance
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class MaintenanceManager
@Inject
constructor(
    private val adminSettingsRepository: AdminSettingsRepository,
    @AndroidMediaPlayer private val player: AudioPlayer,
    @IoDispatcher private val dispatcher: CoroutineContext,
) {

    private val maintenance = MutableLiveData<Maintenance?>()

    init {
        fetch()
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
                        },
                    )
                }
        }
    }

    suspend fun setActiveStatus(status: Boolean): FirebaseDatabaseStatus {
        val firebaseDatabaseStatus =
            adminSettingsRepository.updateMaintenance(FirebaseDatabaseReference.ACTIVE, status)
        maintenance.postValue(
            maintenance.value?.copy(
                activeStatus =
                if (firebaseDatabaseStatus is FirebaseDatabaseStatus.Failed) !status else status,
            ),
        )

        return firebaseDatabaseStatus
    }

    suspend fun setStrictMode(mode: Boolean): FirebaseDatabaseStatus {
        val firebaseDatabaseStatus =
            adminSettingsRepository.updateMaintenance(FirebaseDatabaseReference.STRICT, mode)
        maintenance.postValue(
            maintenance.value?.copy(
                strictMode =
                if (firebaseDatabaseStatus is FirebaseDatabaseStatus.Failed) !mode else mode,
            ),
        )

        return firebaseDatabaseStatus
    }

    fun getMaintenance(): LiveData<Maintenance?> {
        return maintenance
    }

    private fun inCoroutine(block: suspend CoroutineScope.() -> Unit) {
        CoroutineScope(dispatcher).launch { block() }
    }
}
