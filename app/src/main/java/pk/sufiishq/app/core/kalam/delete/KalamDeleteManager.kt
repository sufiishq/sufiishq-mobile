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

package pk.sufiishq.app.core.kalam.delete

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import pk.sufiishq.app.R
import pk.sufiishq.app.core.kalam.data.repository.KalamRepository
import pk.sufiishq.app.core.kalam.helper.TrackListType
import pk.sufiishq.app.core.kalam.model.Kalam
import pk.sufiishq.app.core.kalam.model.KalamDeleteItem
import pk.sufiishq.app.core.player.controller.AudioPlayer
import pk.sufiishq.app.core.player.di.qualifier.AndroidMediaPlayer
import pk.sufiishq.app.di.qualifier.IoDispatcher
import pk.sufiishq.app.utils.offlineFile
import pk.sufiishq.app.utils.quickToast
import kotlin.coroutines.CoroutineContext

class KalamDeleteManager
@Inject
constructor(
    @AndroidMediaPlayer private val player: AudioPlayer,
    @IoDispatcher private val dispatcher: CoroutineContext,
    private val kalamRepository: KalamRepository,
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
                kalam.apply { playlistId = 0 },
            )
        }
    }

    private fun canDelete(kalam: Kalam): Boolean {
        // can't be deleted if kalam is on active play
        return player.getActiveTrack().id != kalam.id
    }
}
