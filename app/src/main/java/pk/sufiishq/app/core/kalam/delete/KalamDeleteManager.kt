package pk.sufiishq.app.core.kalam.delete

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import pk.sufiishq.app.R
import pk.sufiishq.app.core.player.AudioPlayer
import pk.sufiishq.app.data.repository.KalamRepository
import pk.sufiishq.app.di.qualifier.AndroidMediaPlayer
import pk.sufiishq.app.di.qualifier.IoDispatcher
import pk.sufiishq.app.helpers.TrackListType
import pk.sufiishq.app.models.Kalam
import pk.sufiishq.app.models.KalamDeleteItem
import pk.sufiishq.app.utils.offlineFile
import pk.sufiishq.app.utils.quickToast
import kotlin.coroutines.CoroutineContext

class KalamDeleteManager @Inject constructor(
    @AndroidMediaPlayer private val player: AudioPlayer,
    @ApplicationContext private val appContext: Context,
    @IoDispatcher private val dispatcher: CoroutineContext,
    private val kalamRepository: KalamRepository
) {

    private val showKalamConfirmDeleteDialog = MutableLiveData<KalamDeleteItem?>(null)

    fun delete(kalamDeleteItem: KalamDeleteItem) {
        showKalamConfirmDeleteDialog(null)
        if (canDelete(kalamDeleteItem.kalam)) {
            deleteKalam(kalamDeleteItem)
        } else {
            quickToast(R.string.dynamic_delete_on_playing_error, kalamDeleteItem.kalam.title)
        }
    }

    fun showKalamConfirmDeleteDialog(): LiveData<KalamDeleteItem?> {
        return showKalamConfirmDeleteDialog
    }

    fun showKalamConfirmDeleteDialog(kalamDeleteItem: KalamDeleteItem?) {
        showKalamConfirmDeleteDialog.postValue(kalamDeleteItem)
    }

    private fun deleteKalam(kalamDeleteItem: KalamDeleteItem) {
        when (kalamDeleteItem.trackListType) {
            is TrackListType.Playlist -> deleteFromPlaylist(kalamDeleteItem.kalam)
            else -> deleteFromDownloads(kalamDeleteItem.kalam)
        }
    }

    private fun deleteFromDownloads(kalam: Kalam) {
        CoroutineScope(dispatcher).launch {
            kalam.apply {
                offlineFile()?.delete()
                offlineSource = ""
                kalamRepository.update(this)

                if (onlineSource.isEmpty()) {
                    kalamRepository.delete(this)
                }
            }
        }
    }

    private fun deleteFromPlaylist(kalam: Kalam) {
        CoroutineScope(dispatcher).launch {
            kalamRepository.update(
                kalam.apply {
                    playlistId = 0
                }
            )
        }
    }

    private fun canDelete(kalam: Kalam): Boolean {

        // can't be deleted if kalam is on active play
        return player.getActiveTrack().id != kalam.id
    }

}