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

package pk.sufiishq.app.feature.app.controller

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import pk.sufiishq.app.feature.kalam.data.repository.KalamRepository
import pk.sufiishq.app.feature.kalam.helper.TrackListType
import pk.sufiishq.app.feature.kalam.model.Kalam
import pk.sufiishq.app.feature.kalam.model.KalamInfo
import pk.sufiishq.app.feature.player.PlayerState
import pk.sufiishq.app.utils.LAST_PLAY_KALAM
import javax.inject.Inject
import pk.sufiishq.app.utils.extention.getFromStorage
import pk.sufiishq.app.utils.extention.putInStorage

@HiltViewModel
class AssetKalamLoaderViewModel
@Inject
constructor(
    private val app: Application,
    private val gson: Gson,
    private val kalamRepository: KalamRepository,
) : ViewModel() {

    fun countAll(): LiveData<Int> = kalamRepository.countAll()

    fun loadAllKalam() {
        viewModelScope.launch {
            kalamRepository.countAll().asFlow().collectLatest { count ->
                if (count <= 0) {
                    val allKalam = kalamRepository.loadAllFromAssets(app)
                    kalamRepository.insertAll(allKalam)
                    initDefaultKalam(allKalam[allKalam.size.minus(1)].copy(id = allKalam.size))
                } else if (LAST_PLAY_KALAM.getFromStorage("").isEmpty()) {
                    kalamRepository.getDefaultKalam().asFlow().collectLatest { initDefaultKalam(it) }
                }
            }
        }
    }

    private fun initDefaultKalam(kalam: Kalam) {
        LAST_PLAY_KALAM.putInStorage(
            gson.toJson(
                KalamInfo(
                    playerState = PlayerState.IDLE,
                    kalam = kalam,
                    currentProgress = 0,
                    totalDuration = 0,
                    enableSeekbar = false,
                    TrackListType.All(),
                ),
            ),
        )
    }
}
