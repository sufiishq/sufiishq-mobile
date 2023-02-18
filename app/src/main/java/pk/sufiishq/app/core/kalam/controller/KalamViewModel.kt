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

package pk.sufiishq.app.core.kalam.controller

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import pk.sufiishq.app.core.app.AppManager
import pk.sufiishq.app.core.kalam.delete.KalamDeleteManager
import pk.sufiishq.app.core.kalam.downloader.KalamDownloadManager
import pk.sufiishq.app.core.kalam.downloader.KalamDownloadState
import pk.sufiishq.app.core.kalam.favorite.FavoriteManager
import pk.sufiishq.app.core.kalam.splitter.KalamSplitManager
import pk.sufiishq.app.core.kalam.splitter.SplitKalamInfo
import pk.sufiishq.app.core.kalam.splitter.SplitStatus
import pk.sufiishq.app.core.player.PlayerManager
import pk.sufiishq.app.core.playlist.PlaylistManager
import pk.sufiishq.app.core.kalam.data.repository.KalamRepository
import pk.sufiishq.app.di.qualifier.KalamItemPopupMenuItems
import pk.sufiishq.app.helpers.popupmenu.PopupMenu
import pk.sufiishq.app.core.kalam.model.Kalam
import pk.sufiishq.app.core.kalam.model.KalamDeleteItem
import pk.sufiishq.app.core.playlist.model.Playlist
import pk.sufiishq.app.utils.filterItems
import pk.sufiishq.aurora.models.DataMenuItem
import java.util.*
import javax.inject.Inject
import pk.sufiishq.app.core.kalam.helper.TrackListType

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class KalamViewModel
@Inject
constructor(
    @KalamItemPopupMenuItems private val popupMenu: PopupMenu,
    private val kalamDownloadManager: KalamDownloadManager,
    private val kalamDeleteManager: KalamDeleteManager,
    private val kalamSplitManager: KalamSplitManager,
    private val kalamRepository: KalamRepository,
    private val playlistManager: PlaylistManager,
    private val favoriteManager: FavoriteManager,
    private val playerManager: PlayerManager,
    private val appManager: AppManager,
) : ViewModel(), KalamController {

    private var kalams: Flow<PagingData<Kalam>> =
        Pager(PagingConfig(pageSize = 10), pagingSourceFactory = pagingSource()).flow

    private fun pagingSource(): () -> PagingSource<Int, Kalam> {
        return { kalamRepository.load() }
    }

    // -------------------------------------------------------------------- //
    // kalam base functionality
    // -------------------------------------------------------------------- //

    override fun getKalamDataFlow(): Flow<PagingData<Kalam>> {
        return kalams
    }

    override fun searchKalam(keyword: String, trackListType: TrackListType) {
        kalamRepository.setSearchKeyword(keyword)
        kalamRepository.setTrackListType(trackListType)
    }

    override fun popupMenuItems(kalam: Kalam, trackType: String): List<DataMenuItem> {
        return popupMenu.getPopupMenuItems().filterItems(kalam, trackType)
    }

    // -------------------------------------------------------------------- //
    // kalam change functionality
    // -------------------------------------------------------------------- //

    override fun changeTrack(kalam: Kalam, trackListType: TrackListType) {
        playerManager.changeTrack(kalam, trackListType)
    }

    override fun shareKalam(kalam: Kalam, componentActivity: ComponentActivity) {
        appManager.shareKalam(kalam, componentActivity)
    }

    // -------------------------------------------------------------------- //
    // kalam favorite/remove-favorite functionality
    // -------------------------------------------------------------------- //

    override fun markAsFavorite(kalam: Kalam) {
        favoriteManager.markAsFavorite(kalam)
    }

    override fun removeFavorite(kalam: Kalam) {
        favoriteManager.removeFavorite(kalam)
    }

    // -------------------------------------------------------------------- //
    // kalam delete functionality
    // -------------------------------------------------------------------- //

    override fun delete(kalamDeleteItem: KalamDeleteItem) {
        kalamDeleteManager.delete(kalamDeleteItem)
    }

    override fun showKalamConfirmDeleteDialog(): LiveData<KalamDeleteItem?> {
        return kalamDeleteManager.showKalamConfirmDeleteDialog()
    }

    override fun showKalamConfirmDeleteDialog(kalamDeleteItem: KalamDeleteItem?) {
        return kalamDeleteManager.showKalamConfirmDeleteDialog(kalamDeleteItem)
    }

    // -------------------------------------------------------------------- //
    // kalam playlist functionality
    // -------------------------------------------------------------------- //

    override fun addToPlaylist(kalam: Kalam, playlist: Playlist) {
        playlistManager.addToPlaylist(kalam, playlist)
    }

    override fun showPlaylistDialog(): LiveData<Pair<Kalam, List<Playlist>>?> {
        return playlistManager.showPlaylistDialog()
    }

    override fun showPlaylistDialog(kalam: Kalam) {
        playlistManager.showPlaylistDialog(kalam)
    }

    override fun dismissPlaylistDialog() {
        playlistManager.dismissPlaylistDialog()
    }

    // -------------------------------------------------------------------- //
    // kalam download functionality
    // -------------------------------------------------------------------- //

    override fun getKalamDownloadState(): LiveData<KalamDownloadState> {
        return kalamDownloadManager.getKalamDownloadState()
    }

    override fun startDownload(kalam: Kalam) {
        kalamDownloadManager.startDownload(kalam)
    }

    override fun dismissDownload() {
        kalamDownloadManager.dismissDownload()
    }

    // -------------------------------------------------------------------- //
    // kalam split functionality
    // -------------------------------------------------------------------- //

    override fun showKalamSplitDialog(kalam: Kalam) {
        kalamSplitManager.showKalamSplitDialog(kalam)
    }

    override fun showKalamSplitDialog(): LiveData<SplitKalamInfo?> {
        return kalamSplitManager.showKalamSplitDialog()
    }

    override fun startSplitting() {
        kalamSplitManager.startSplitting()
    }

    override fun playSplitKalamPreview() {
        kalamSplitManager.playSplitKalamPreview()
    }

    override fun setSplitStart(start: Int) {
        kalamSplitManager.setSplitStart(start)
    }

    override fun setSplitEnd(end: Int) {
        kalamSplitManager.setSplitEnd(end)
    }

    override fun setSplitStatus(status: SplitStatus) {
        kalamSplitManager.setSplitStatus(status)
    }

    override fun updateSplitSeekbarValue(value: Float) {
        kalamSplitManager.updateSplitSeekbarValue(value)
    }

    override fun saveSplitKalam(sourceKalam: Kalam, kalamTitle: String) {
        kalamSplitManager.saveSplitKalam(sourceKalam, kalamTitle)
    }

    override fun dismissKalamSplitDialog() {
        kalamSplitManager.dismissKalamSplitDialog()
    }
}
