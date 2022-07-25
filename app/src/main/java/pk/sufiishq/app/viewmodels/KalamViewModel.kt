package pk.sufiishq.app.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow
import pk.sufiishq.app.utils.KALAM_DIR
import pk.sufiishq.app.data.providers.KalamDataProvider
import pk.sufiishq.app.data.repository.KalamRepository
import pk.sufiishq.app.helpers.KalamSource
import pk.sufiishq.app.helpers.Screen
import pk.sufiishq.app.models.Kalam
import pk.sufiishq.app.utils.toast
import pk.sufiishq.app.utils.copyAsNew
import pk.sufiishq.app.utils.moveTo
import java.io.File
import javax.inject.Inject

@HiltViewModel
class KalamViewModel @Inject constructor(
    @ApplicationContext private val appContext: Context,
    val kalamRepository: KalamRepository
) : ViewModel(), KalamDataProvider {

    private var kalamSource = KalamSource(kalamRepository, Screen.Tracks.ALL)

    private var kalams: Flow<PagingData<Kalam>> = Pager(PagingConfig(pageSize = 10)) {
        kalamSource
    }.flow

    override fun init(trackType: String, playlistId: Int) {
        kalamSource = KalamSource(kalamRepository, trackType, playlistId)
    }

    override fun getKalamDataFlow(): Flow<PagingData<Kalam>> {
        return kalams
    }

    override fun searchKalam(keyword: String, trackType: String, playlistId: Int) {
        kalamSource = KalamSource(kalamRepository, trackType, playlistId, keyword)
    }

    override fun update(kalam: Kalam) {
        kalamRepository.update(kalam)
    }

    override fun delete(kalam: Kalam, trackType: String) {
        if (trackType == Screen.Tracks.PLAYLIST) {
            kalam.playlistId = 0
            kalamRepository.update(kalam)
        } else {
            val downloadedFile = File("${appContext.filesDir.absolutePath}/${kalam.offlineSource}")
            downloadedFile.delete()
            kalam.offlineSource = ""
            if (kalam.onlineSource.isEmpty()) kalamRepository.delete(kalam) else kalamRepository.update(
                kalam
            )
        }
    }

    override fun save(sourceKalam: Kalam, splitFile: File, kalamTitle: String) {

        val fileName = KALAM_DIR + "/" + kalamTitle.lowercase().replace(" ", "_")
            .plus("_${System.currentTimeMillis()}.mp3")

        val kalam = sourceKalam.copyAsNew(
            id = 0,
            title = kalamTitle,
            onlineSource = "",
            offlineSource = fileName,
            isFavorite = 0,
            playlistId = 0
        )

        kalamRepository.insert(kalam)

        val destination = File(appContext.filesDir, fileName)
        splitFile.moveTo(destination)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()

        appContext.toast("$kalamTitle saved")
    }
}