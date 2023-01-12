package pk.sufiishq.app.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.util.*
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import pk.sufiishq.app.R
import pk.sufiishq.app.core.event.dispatcher.EventDispatcher
import pk.sufiishq.app.core.event.events.Event
import pk.sufiishq.app.core.event.events.KalamEvents
import pk.sufiishq.app.core.event.events.KalamSplitManagerEvents
import pk.sufiishq.app.core.event.exception.UnhandledEventException
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
import pk.sufiishq.app.models.KalamDeleteItem
import pk.sufiishq.app.utils.KALAM_DIR
import pk.sufiishq.app.utils.copyWithDefaults
import pk.sufiishq.app.utils.moveTo
import pk.sufiishq.app.utils.toast

@HiltViewModel
class KalamViewModel @Inject constructor(
    private val app: Application,
    private val kalamRepository: KalamRepository,
    private val kalamSplitManager: KalamSplitManager,
    @AndroidMediaPlayer private val player: AudioPlayer,
    private val kalamDeleteStrategyFactory: KalamDeleteStrategyFactory,
    private val favoriteChangeFactory: FavoriteChangeFactory
) : BaseViewModel(app), KalamDataProvider {

    private val showKalamRenameDialog = MutableLiveData<Kalam?>(null)
    private val showKalamDeleteConfirmDialog = MutableLiveData<KalamDeleteItem?>(null)
    private val showKalamSplitManagerDialog = MutableLiveData<KalamSplitManager?>(null)
    private val eventDispatcher = EventDispatcher.getInstance()

    init {
        eventDispatcher.registerEventHandler(this)
    }

    private var kalams: Flow<PagingData<Kalam>> =
        Pager(PagingConfig(pageSize = 10), pagingSourceFactory = pagingSource()).flow

    override fun getKalamDeleteConfirmDialog(): LiveData<KalamDeleteItem?> {
        return showKalamDeleteConfirmDialog
    }

    override fun getKalamDataFlow(): Flow<PagingData<Kalam>> {
        return kalams
    }

    override fun getKalamSplitManagerDialog(): LiveData<KalamSplitManager?> {
        return showKalamSplitManagerDialog
    }

    override fun getKalamRenameDialog(): LiveData<Kalam?> {
        return showKalamRenameDialog
    }

    private fun setKalamRenameDialog(kalam: Kalam?) {
        showKalamRenameDialog.postValue(kalam)
    }

    private fun searchKalam(keyword: String, trackListType: TrackListType) {
        kalamRepository.setSearchKeyword(keyword)
        kalamRepository.setTrackListType(trackListType)
    }

    private fun update(kalam: Kalam) {
        viewModelScope.launch {
            kalamRepository.update(kalam)
        }
    }

    private fun setKalamSplitManagerDialog(kalam: Kalam?) {

        var splitManager: KalamSplitManager? = null

        kalam?.apply {
            eventDispatcher.dispatch(KalamSplitManagerEvents.SetKalam(this))
            splitManager = kalamSplitManager
        }

        showKalamSplitManagerDialog.postValue(splitManager)
    }

    private fun setKalamConfirmDeleteDialog(kalamDeleteItem: KalamDeleteItem?) {
        showKalamDeleteConfirmDialog.postValue(kalamDeleteItem)
    }

    private fun delete(kalamDeleteItem: KalamDeleteItem) {

        viewModelScope.launch {

            if (canDelete(kalamDeleteItem.kalam)) {
                kalamDeleteStrategyFactory.create(kalamDeleteItem.trackListType.type)
                    .delete(kalamDeleteItem.kalam)
            } else {
                app.toast(
                    app.getString(R.string.error_kalam_delete_on_playing)
                        .format(kalamDeleteItem.kalam.title)
                )
            }
        }
    }

    private fun saveSplitKalam(sourceKalam: Kalam, splitFile: File, kalamTitle: String) {

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

        File(app.filesDir, fileName).apply {
            splitFile.moveTo(this)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        }

        app.toast(app.getString(R.string.kalam_saved_label).format(kalamTitle))
    }

    private fun markAsFavorite(kalam: Kalam) {
        viewModelScope.launch {
            favoriteChangeFactory.create(AddToFavoriteStrategy::class).change(kalam)
        }
    }

    private fun removeFavorite(kalam: Kalam) {
        viewModelScope.launch {
            favoriteChangeFactory.create(RemoveFromFavoriteStrategy::class).change(kalam)
        }
    }

    private fun pagingSource(): () -> PagingSource<Int, Kalam> {
        return {
            kalamRepository.load()
        }
    }

    private fun canDelete(kalam: Kalam): Boolean {

        // stop playing kalam if it match with deleted kalam
        return player.getActiveTrack().id != kalam.id
    }

    /*=======================================*/
    // HANDLE KALAM EVENTS
    /*=======================================*/

    override fun onEvent(event: Event) {

        when (event) {
            is KalamEvents.SearchKalam -> searchKalam(event.keyword, event.trackListType)
            is KalamEvents.UpdateKalam -> update(event.kalam)
            is KalamEvents.ShowKalamConfirmDeleteDialog -> setKalamConfirmDeleteDialog(event.kalamDeleteItem)
            is KalamEvents.DeleteKalam -> delete(event.kalamDeleteItem)
            is KalamEvents.SaveSplitKalam -> saveSplitKalam(
                event.sourceKalam,
                event.splitFile,
                event.kalamTitle
            )
            is KalamEvents.MarkAsFavoriteKalam -> markAsFavorite(event.kalam)
            is KalamEvents.RemoveFavoriteKalam -> removeFavorite(event.kalam)
            is KalamEvents.ShowKalamSplitManagerDialog -> setKalamSplitManagerDialog(event.kalam)
            is KalamEvents.ShowKalamRenameDialog -> setKalamRenameDialog(event.kalam)
            else -> throw UnhandledEventException(event, this)
        }
    }
}