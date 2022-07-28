package pk.sufiishq.app.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import pk.sufiishq.app.SufiIshqApp
import pk.sufiishq.app.data.providers.KalamDataProvider
import pk.sufiishq.app.data.repository.KalamRepository
import pk.sufiishq.app.helpers.Screen
import pk.sufiishq.app.models.Kalam
import pk.sufiishq.app.models.KalamItemParam
import pk.sufiishq.app.utils.KALAM_DIR
import pk.sufiishq.app.utils.copyAsNew
import pk.sufiishq.app.utils.moveTo
import pk.sufiishq.app.utils.toast
import java.io.File
import javax.inject.Inject

@HiltViewModel
class KalamViewModel @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val kalamRepository: KalamRepository
) : ViewModel(), KalamDataProvider {

    private var kalams: Flow<PagingData<Kalam>> = Pager(PagingConfig(pageSize = 10)) {
        kalamRepository.load()
    }.flow

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
                    appContext.toast("${kalam.title} is playing. Can't be deleted")
                }
            }
        }
    }

    private fun canDelete(kalam: Kalam): Boolean {

        // stop playing kalam if it match with deleted kalam
        return SufiIshqApp.getInstance().getPlayerController()?.let { playerController ->
            playerController.getActiveTrack()?.let { activeTrack ->
                activeTrack.id != kalam.id
            } ?: true
        } ?: true
    }

    override fun save(sourceKalam: Kalam, splitFile: File, kalamTitle: String) {

        val fileName = "$KALAM_DIR/" + kalamTitle.lowercase().replace(" ", "_")
            .plus("_${System.currentTimeMillis()}.mp3")

        val kalam = sourceKalam.copyAsNew(
            id = 0,
            title = kalamTitle,
            onlineSource = ""
        )
        kalam.offlineSource = fileName

        viewModelScope.launch {
            kalamRepository.insert(kalam)
        }

        val destination = File(appContext.filesDir, fileName)
        splitFile.moveTo(destination)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()

        appContext.toast("$kalamTitle saved")
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
        appContext.toast("${kalam.title} added in favorite")
        kalam.isFavorite = 1
        update(kalam)
    }

    override fun removeFavorite(kalamItemParam: KalamItemParam) {
        appContext.toast("${kalamItemParam.kalam.title} removed in favorite")
        kalamItemParam.kalam.isFavorite = 0
        update(kalamItemParam.kalam)
        searchKalam(
            kalamItemParam.searchText.value,
            kalamItemParam.trackType,
            kalamItemParam.playlistId
        )
        kalamItemParam.lazyKalamItems.refresh()
    }
}