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

package pk.sufiishq.app.feature.player.controller

import pk.sufiishq.app.feature.kalam.helper.TrackListType
import pk.sufiishq.app.feature.kalam.model.Kalam
import pk.sufiishq.app.feature.player.listener.PlayerStateListener

interface AudioPlayer {

    fun getActiveTrack(): Kalam
    fun setSource(source: Kalam, trackListType: TrackListType)
    fun doPlayOrPause()
    fun isPlaying(): Boolean
    fun seekTo(msec: Int)
    fun release()
    fun getTrackListType(): TrackListType
    fun registerListener(listener: PlayerStateListener): Boolean
}