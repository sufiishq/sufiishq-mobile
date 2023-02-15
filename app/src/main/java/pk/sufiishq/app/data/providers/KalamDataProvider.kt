package pk.sufiishq.app.data.providers

import androidx.activity.ComponentActivity
import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import pk.sufiishq.app.core.kalam.downloader.KalamDownloadState
import pk.sufiishq.app.core.kalam.splitter.SplitKalamInfo
import pk.sufiishq.app.core.kalam.splitter.SplitStatus
import pk.sufiishq.app.helpers.TrackListType
import pk.sufiishq.app.models.Kalam
import pk.sufiishq.app.models.KalamDeleteItem
import pk.sufiishq.app.models.Playlist
import pk.sufiishq.aurora.models.DataMenuItem

interface KalamDataProvider {

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