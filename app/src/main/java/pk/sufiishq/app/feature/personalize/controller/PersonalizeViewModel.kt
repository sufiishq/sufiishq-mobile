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

package pk.sufiishq.app.feature.personalize.controller

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.launch
import org.apache.commons.io.FilenameUtils
import pk.sufiishq.app.di.qualifier.IoDispatcher
import pk.sufiishq.app.feature.kalam.downloader.FileDownloader
import pk.sufiishq.app.feature.kalam.downloader.FileInfo
import pk.sufiishq.app.feature.personalize.data.repository.PersonalizeRepository
import pk.sufiishq.app.feature.personalize.model.LogoPath
import pk.sufiishq.app.feature.personalize.model.Personalize
import pk.sufiishq.app.feature.theme.controller.ThemeViewModel
import pk.sufiishq.app.feature.theme.model.AutoChangeColorDuration
import pk.sufiishq.app.feature.theme.worker.AutoColorChangeWorker
import pk.sufiishq.app.utils.extention.appendPath
import pk.sufiishq.app.utils.extention.getFromStorage
import pk.sufiishq.app.utils.extention.moveTo
import pk.sufiishq.app.utils.extention.offlineFileExists
import pk.sufiishq.app.utils.extention.putInStorage
import java.io.File
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class PersonalizeViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    @IoDispatcher private val dispatcher: CoroutineContext,
    private val personalizeRepository: PersonalizeRepository,
    private val fileDownloader: FileDownloader,
) : ViewModel(), PersonalizeController {

    init {
        if (!getPersonalizeDir().exists()) {
            getPersonalizeDir().mkdir()
        }
    }

    override suspend fun isAutoDownloadKalam(): Boolean {
        return AUTO_DOWNLOAD_KALAM.getFromStorage(true)
    }

    override fun setAutoDownloadKalam(isEnable: Boolean) {
        launchInScope {
            AUTO_DOWNLOAD_KALAM.putInStorage(isEnable)
        }
    }

    override suspend fun resolveLogo(logoPath: LogoPath): LogoPath? {
        var result = logoPath

        if (!logoPath.offlineFileExists(context)) {
            fileDownloader.download(
                logoPath.onlinePath,
                getCacheFile(logoPath),
            )
                .cancellable()
                .collect { fileInfo ->
                    if (fileInfo is FileInfo.Finished) {
                        getCacheFile(logoPath).moveTo(
                            context.filesDir.appendPath(logoPath.offlinePath),
                        )
                        result = logoPath
                    }
                }
        }

        return result
    }

    override fun get(): LiveData<Personalize?> {
        return personalizeRepository.getPersonalize()
    }

    override fun reset() {
        launchInScope {
            personalizeRepository.clearPersonalize()
        }
    }

    override fun update(personalize: Personalize) {
        launchInScope {
            personalizeRepository.setPersonalize(personalize)
        }
    }

    private fun getPersonalizeDir(): File {
        return context.filesDir.appendPath(PERSONALIZE_DIR)
    }

    private fun getCacheFile(logoPath: LogoPath): File {
        return context.cacheDir.appendPath(FilenameUtils.getName(logoPath.offlinePath))
    }

    private fun launchInScope(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch(dispatcher, block = block)
    }

    companion object {
        const val PERSONALIZE_DIR = "personalize"
        const val AUTO_DOWNLOAD_KALAM = "si_auto_download_kalam"
    }
}
