package pk.sufiishq.app.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import pk.sufiishq.app.R
import pk.sufiishq.app.core.player.AudioPlayer
import pk.sufiishq.app.data.providers.KalamDataProvider
import pk.sufiishq.app.data.repository.KalamRepository
import pk.sufiishq.app.di.qualifier.AndroidMediaPlayer
import pk.sufiishq.app.helpers.KalamSplitManager
import pk.sufiishq.app.helpers.TrackListType
import pk.sufiishq.app.helpers.factory.FavoriteChangeFactory
import pk.sufiishq.app.helpers.factory.KalamDeleteStrategyFactory
import pk.sufiishq.app.helpers.strategies.kalam.favorite.AddToFavoriteStrategy
import pk.sufiishq.app.helpers.strategies.kalam.favorite.RemoveFromFavoriteStrategy
import pk.sufiishq.app.models.Kalam
import pk.sufiishq.app.utils.*
import java.io.File
import java.util.*
import javax.inject.Inject

@HiltViewModel
class KalamViewModel @Inject constructor(
    private val kalamRepository: KalamRepository,
    private val kalamSplitManager: KalamSplitManager,
    @AndroidMediaPlayer private val player: AudioPlayer,
    private val kalamDeleteStrategyFactory: KalamDeleteStrategyFactory,
    private val favoriteChangeFactory: FavoriteChangeFactory
) : ViewModel(), KalamDataProvider {

    private val appContext = app

    private var kalams: Flow<PagingData<Kalam>> =
        Pager(PagingConfig(pageSize = 10), pagingSourceFactory = pagingSource()).flow

    private fun pagingSource(): () -> PagingSource<Int, Kalam> {
        return {
            kalamRepository.load()
        }
    }

    private fun canDelete(kalam: Kalam): Boolean {

        // stop playing kalam if it match with deleted kalam
        return player.getActiveTrack().id != kalam.id
    }

    override fun init(trackListType: TrackListType) {
        kalamRepository.setTrackListType(trackListType)
        kalamRepository.setSearchKeyword("")
    }

    override fun getKalamDataFlow(): Flow<PagingData<Kalam>> {
        return kalams
    }

    override fun searchKalam(keyword: String, trackListType: TrackListType) {
        kalamRepository.setSearchKeyword(keyword)
        kalamRepository.setTrackListType(trackListType)
    }

    override fun update(kalam: Kalam) {
        viewModelScope.launch {
            kalamRepository.update(kalam)
        }
    }

    override fun delete(kalam: Kalam, trackType: String) {

        viewModelScope.launch {

            if(canDelete(kalam)) {
                kalamDeleteStrategyFactory.create(trackType).delete(kalam)
            } else {
                appContext.toast(
                    appContext.getString(R.string.error_kalam_delete_on_playing)
                        .format(kalam.title)
                )
            }
        }
    }

    override fun saveSplitKalam(sourceKalam: Kalam, splitFile: File, kalamTitle: String) {

        val fileName = buildString {
            append(KALAM_DIR)
            append(File.separator)
            append(kalamTitle.lowercase().replace(" ", "_"))
            append("_${Calendar.getInstance().timeInMillis}.mp3")
        }

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

        File(appContext.filesDir, fileName).apply {
            splitFile.moveTo(this)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        }

        appContext.toast(appContext.getString(R.string.kalam_saved_label).format(kalamTitle))
    }

    override fun markAsFavorite(kalam: Kalam) {
        viewModelScope.launch {
            favoriteChangeFactory.create(AddToFavoriteStrategy::class).change(kalam)
        }
    }

    override fun removeFavorite(kalam: Kalam) {
        viewModelScope.launch {
            favoriteChangeFactory.create(RemoveFromFavoriteStrategy::class).change(kalam)
        }
    }

    override fun getKalamSplitManager(): KalamSplitManager {
        return kalamSplitManager
    }

    override fun getActiveSearchKeyword(): String {
        return kalamRepository.getSearchKeyword()
    }
}