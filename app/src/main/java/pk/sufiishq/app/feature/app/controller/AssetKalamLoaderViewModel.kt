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

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import pk.sufiishq.app.feature.kalam.data.repository.KalamRepository
import pk.sufiishq.app.feature.kalam.helper.TrackListType
import pk.sufiishq.app.feature.kalam.model.Kalam
import pk.sufiishq.app.feature.kalam.model.KalamInfo
import pk.sufiishq.app.feature.player.PlayerState
import pk.sufiishq.app.utils.Constants.LAST_PLAY_KALAM
import pk.sufiishq.app.utils.extention.getFromStorage
import pk.sufiishq.app.utils.extention.putInStorage
import javax.inject.Inject

@HiltViewModel
class AssetKalamLoaderViewModel
@Inject
constructor(
    @ApplicationContext private val appContext: Context,
    private val gson: Gson,
    private val kalamRepository: KalamRepository,
) : ViewModel() {

    private var runOnlyOnce = true

    fun countAll(): LiveData<Int> = kalamRepository.countAll()

    fun loadAllKalam() {
        viewModelScope.launch {
            kalamRepository.countAllWithSuspend().let { count ->
                if (count <= 0 && runOnlyOnce) {
                    runOnlyOnce = false
                    val allKalam = kalamRepository.loadAllFromAssets(appContext)
                    initDefaultKalam(allKalam[allKalam.size.minus(1)].copy(id = allKalam.size))
                    kalamRepository.insertAll(allKalam)
                } else if (LAST_PLAY_KALAM.getFromStorage("").isEmpty()) {
                    kalamRepository.getDefaultKalam()?.let { initDefaultKalam(it) }
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
