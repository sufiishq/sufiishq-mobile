package pk.sufiishq.app.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import pk.sufiishq.app.R
import pk.sufiishq.app.SufiIshqApp
import pk.sufiishq.app.data.providers.KalamDataProvider
import pk.sufiishq.app.data.repository.KalamRepository
import pk.sufiishq.app.helpers.KalamSplitManager
import pk.sufiishq.app.helpers.Screen
import pk.sufiishq.app.models.Kalam
import pk.sufiishq.app.models.KalamItemParam
import pk.sufiishq.app.utils.KALAM_DIR
import pk.sufiishq.app.utils.copyWithDefaults
import pk.sufiishq.app.utils.moveTo
import pk.sufiishq.app.utils.toast
import java.io.File
import java.util.*
import javax.inject.Inject

@HiltViewModel
class KalamViewModel @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val kalamRepository: KalamRepository,
    private val kalamSplitManager: KalamSplitManager
) : ViewModel(), KalamDataProvider {

    private val showKalamRenameDialog = MutableLiveData(false)
    private var kalams: Flow<PagingData<Kalam>> =
        Pager(PagingConfig(pageSize = 10), pagingSourceFactory = pagingSource()).flow

    fun pagingSource(): () -> PagingSource<Int, Kalam> {
        return {
            kalamRepository.load()
        }
    }

    fun canDelete(kalam: Kalam): Boolean {

        // stop playing kalam if it match with deleted kalam
        return SufiIshqApp.getInstance().getPlayerController()?.let { playerController ->
            playerController.getActiveTrack()?.let { activeTrack ->
                activeTrack.id != kalam.id
            } ?: true
        } ?: true
    }

    override fun init(trackType: String, playlistId: Int) {
        kalamRepository.setTrackType(trackType)
        kalamRepository.setPlaylistId(playlistId)
        kalamRepository.setSearchKeyword("")
    }

    override fun getKalamDataFlow(): Flow<PagingData<Kalam>> {
        return kalams
    }

    override fun searchKalam(keyword: String, trackType: String, playlistId: Int) {
        kalamRepository.setSearchKeyword(keyword)
        kalamRepository.setTrackType(trackType)
        kalamRepository.setPlaylistId(playlistId)
    }

    override fun update(kalam: Kalam) {
        viewModelScope.launch {
            kalamRepository.update(kalam)
        }
    }

    override fun delete(kalam: Kalam, trackType: String) {
        viewModelScope.launch {
            if (trackType == Screen.Tracks.PLAYLIST) {
                kalam.playlistId = 0
                kalamRepository.update(kalam)
            } else {
                if (canDelete(kalam)) {
                    val downloadedFile =
                        File("${appContext.filesDir.absolutePath}/${kalam.offlineSource}")
                    downloadedFile.delete()
                    kalam.offlineSource = ""
                    if (kalam.onlineSource.isEmpty()) kalamRepository.delete(kalam) else kalamRepository.update(
                        kalam
                    )
                } else {
                    appContext.toast(
                        appContext.getString(R.string.error_kalam_delete_on_playing)
                            .format(kalam.title)
                    )
                }
            }
        }
    }

    override fun save(sourceKalam: Kalam, splitFile: File, kalamTitle: String) {

        val fileName = "$KALAM_DIR/" + kalamTitle.lowercase().replace(" ", "_")
            .plus("_${Calendar.getInstance().timeInMillis}.mp3")

        val kalam = sourceKalam.copyWithDefaults(
            id = 0,
            title = kalamTitle,
            onlineSource = "",
            offlineSource = fileName,
            isFavorite = 0,
            playlistId = 0
        )

        viewModelScope.launch {
            kalamRepository.insert(kalam)
        }

        val destination = File(appContext.filesDir, fileName)
        splitFile.moveTo(destination)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()

        appContext.toast(appContext.getString(R.string.kalam_saved_label).format(kalamTitle))
    }

    override fun countAll(): LiveData<Int> {
        return kalamRepository.countAll()
    }

    override fun countFavorites(): LiveData<Int> {
        return kalamRepository.countFavorites()
    }

    override fun countDownloads(): LiveData<Int> {
        return kalamRepository.countDownloads()
    }

    override fun markAsFavorite(kalam: Kalam) {
        appContext.toast(appContext.getString(R.string.favorite_added).format(kalam.title))
        kalam.isFavorite = 1
        update(kalam)
    }

    override fun removeFavorite(kalamItemParam: KalamItemParam) {
        appContext.toast(
            appContext.getString(R.string.favorite_removed).format(kalamItemParam.kalam.title)
        )
        kalamItemParam.kalam.isFavorite = 0
        update(kalamItemParam.kalam)

        // refresh list in case you are on favorites screen
        searchKalam(
            kalamItemParam.searchText.value,
            kalamItemParam.trackType,
            kalamItemParam.playlistId
        )
        kalamItemParam.lazyKalamItems.refresh()
    }

    override fun getKalamSplitManager(): KalamSplitManager {
        return kalamSplitManager
    }

    override fun getActiveTrackType(): String {
        return kalamRepository.getTrackType()
    }

    override fun getActivePlaylistId(): Int {
        return kalamRepository.getPlaylistId()
    }

    override fun getActiveSearchKeyword(): String {
        return kalamRepository.getSearchKeyword()
    }

    override fun getShowKalamRenameDialog(): LiveData<Boolean> {
        return showKalamRenameDialog
    }

    override fun setShowKalamRenameDialog(value: Boolean) {
        showKalamRenameDialog.postValue(value)
    }
}