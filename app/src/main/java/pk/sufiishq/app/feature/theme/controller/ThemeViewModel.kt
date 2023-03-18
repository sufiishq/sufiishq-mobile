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

package pk.sufiishq.app.feature.theme.controller

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import pk.sufiishq.app.di.qualifier.IoDispatcher
import pk.sufiishq.app.feature.theme.model.AutoChangeColorDuration
import pk.sufiishq.app.feature.theme.worker.AutoColorChangeWorker
import pk.sufiishq.app.utils.extention.getFromStorage
import pk.sufiishq.app.utils.extention.putInStorage
import pk.sufiishq.aurora.config.AuroraConfig
import pk.sufiishq.aurora.models.ColorPalette
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class ThemeViewModel @Inject constructor(
    @ApplicationContext private val appContext: Context,
    @IoDispatcher private val dispatcher: CoroutineContext,
    private val gson: Gson,
) : ViewModel(), ThemeController {

    override suspend fun isAutoChangeColorEnable(): Boolean {
        return AUTO_COLOR_CHANGED_KEY.getFromStorage(false)
    }

    override fun setAutoChangeColor(isEnable: Boolean, activeDuration: AutoChangeColorDuration) {
        launchInScope {
            AUTO_COLOR_CHANGED_KEY.putInStorage(isEnable)
            if (isEnable) {
                updateAutoColorChangeDuration(activeDuration)
            } else {
                AutoColorChangeWorker.cancel(appContext)
            }
        }
    }

    override suspend fun getActiveAutoColorChangeDuration(): AutoChangeColorDuration? {
        return AUTO_COLOR_CHANGED_DURATION
            .getFromStorage("")
            .takeIf { it.isNotEmpty() }
            ?.let {
                gson.fromJson(it, AutoChangeColorDuration::class.java)
            }
    }

    override suspend fun getAutoColorChangeDurationList(): List<AutoChangeColorDuration> {
        return AutoChangeColorDuration.getList()
    }

    override fun updateAutoColorChangeDuration(autoChangeColorDuration: AutoChangeColorDuration) {
        launchInScope {
            AUTO_COLOR_CHANGED_DURATION.putInStorage(
                gson.toJson(autoChangeColorDuration),
            )
            AutoColorChangeWorker.init(appContext, autoChangeColorDuration)
        }
    }

    override suspend fun getAvailableColorPalettes(): List<ColorPalette> {
        return AuroraConfig.getAvailableColorPalettes()
    }

    private fun launchInScope(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch(dispatcher, block = block)
    }

    companion object {
        private const val AUTO_COLOR_CHANGED_KEY = "si_auto_color_changed"
        private const val AUTO_COLOR_CHANGED_DURATION = "si_auto_color_duration"
    }
}
