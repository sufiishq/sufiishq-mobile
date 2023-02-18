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

package pk.sufiishq.app.core.player

import android.content.Context
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import pk.sufiishq.app.core.kalam.helper.TrackListType
import pk.sufiishq.app.core.kalam.model.Kalam
import pk.sufiishq.app.core.kalam.model.KalamInfo
import pk.sufiishq.app.utils.LAST_PLAY_KALAM
import pk.sufiishq.app.utils.canPlay
import pk.sufiishq.app.utils.putInStorage

class PlayerManager
@Inject
constructor(
    @ApplicationContext private val appContext: Context,
    private val gson: Gson,
) {

    fun changeTrack(kalam: Kalam, trackListType: TrackListType) {
        if (kalam.canPlay(appContext)) {
            putKalamInStorage(kalam, trackListType)
        }
    }

    private fun putKalamInStorage(kalam: Kalam, trackListType: TrackListType) {
        LAST_PLAY_KALAM.putInStorage(
            gson.toJson(
                KalamInfo(
                    playerState = PlayerState.IDLE,
                    kalam = kalam,
                    currentProgress = 0,
                    totalDuration = 0,
                    false,
                    trackListType,
                ),
            ),
        )
    }
}
