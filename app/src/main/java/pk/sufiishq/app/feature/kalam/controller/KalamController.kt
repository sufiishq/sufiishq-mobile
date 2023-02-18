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

package pk.sufiishq.app.feature.kalam.controller

import androidx.activity.ComponentActivity
import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import pk.sufiishq.app.feature.kalam.downloader.KalamDownloadState
import pk.sufiishq.app.feature.kalam.helper.TrackListType
import pk.sufiishq.app.feature.kalam.model.Kalam
import pk.sufiishq.app.feature.kalam.model.KalamDeleteItem
import pk.sufiishq.app.feature.kalam.splitter.SplitKalamInfo
import pk.sufiishq.app.feature.kalam.splitter.SplitStatus
import pk.sufiishq.app.feature.playlist.model.Playlist
import pk.sufiishq.aurora.models.DataMenuItem

interface KalamController {

    // -------------------------------------------------------------------- //
    // signature for kalam view model
    // -------------------------------------------------------------------- //

    fun getKalamDataFlow(): Flow<PagingData<Kalam>>
    fun searchKalam(keyword: String, trackListType: TrackListType)
    fun popupMenuItems(kalam: Kalam, trackType: String): List<DataMenuItem>

    // -------------------------------------------------------------------- //
    // signatures for change kalam
    // -------------------------------------------------------------------- //

    fun changeTrack(kalam: Kalam, trackListType: TrackListType)

    // -------------------------------------------------------------------- //
    // signatures for share kalam
    // -------------------------------------------------------------------- //

    fun shareKalam(kalam: Kalam, componentActivity: ComponentActivity)

    // -------------------------------------------------------------------- //
    // signatures for favorite/remove-favorite kalam
    // -------------------------------------------------------------------- //

    fun markAsFavorite(kalam: Kalam)
    fun removeFavorite(kalam: Kalam)

    // -------------------------------------------------------------------- //
    // signatures for delete kalam
    // -------------------------------------------------------------------- //

    fun delete(kalamDeleteItem: KalamDeleteItem)
    fun showKalamConfirmDeleteDialog(): LiveData<KalamDeleteItem?>
    fun showKalamConfirmDeleteDialog(kalamDeleteItem: KalamDeleteItem?)

    // -------------------------------------------------------------------- //
    // signatures for add to playlist kalam
    // -------------------------------------------------------------------- //

    fun addToPlaylist(kalam: Kalam, playlist: Playlist)
    fun showPlaylistDialog(): LiveData<Pair<Kalam, List<Playlist>>?>
    fun showPlaylistDialog(kalam: Kalam)
    fun dismissPlaylistDialog()

    // -------------------------------------------------------------------- //
    // signatures for download kalam
    // -------------------------------------------------------------------- //

    fun getKalamDownloadState(): LiveData<KalamDownloadState>
    fun startDownload(kalam: Kalam)
    fun dismissDownload()

    // -------------------------------------------------------------------- //
    // signatures for split kalam
    // -------------------------------------------------------------------- //

    fun showKalamSplitDialog(kalam: Kalam)
    fun showKalamSplitDialog(): LiveData<SplitKalamInfo?>
    fun startSplitting()
    fun playSplitKalamPreview()
    fun setSplitStart(start: Int)
    fun setSplitEnd(end: Int)
    fun setSplitStatus(status: SplitStatus)
    fun updateSplitSeekbarValue(value: Float)
    fun saveSplitKalam(sourceKalam: Kalam, kalamTitle: String)
    fun dismissKalamSplitDialog()
}
