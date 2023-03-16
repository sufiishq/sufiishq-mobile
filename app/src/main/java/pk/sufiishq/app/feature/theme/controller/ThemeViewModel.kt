package pk.sufiishq.app.feature.theme.controller

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import pk.sufiishq.app.di.qualifier.IoDispatcher
import pk.sufiishq.app.feature.theme.model.AutoChangeColorDuration
import pk.sufiishq.app.feature.theme.worker.AutoColorChangeWorker
import pk.sufiishq.app.utils.extention.getFromStorage
import pk.sufiishq.app.utils.extention.putInStorage
import pk.sufiishq.aurora.config.AuroraConfig
import pk.sufiishq.aurora.models.ColorPalette
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class ThemeViewModel @Inject constructor(
    @ApplicationContext private val appContext: Context,
    @IoDispatcher private val dispatcher: CoroutineContext,
    private val gson: Gson
) : ViewModel(), ThemeController {

    override suspend fun isAutoChangeColorEnable(): Boolean {
        return AUTO_COLOR_CHANGED_KEY.getFromStorage(false)
    }

    override fun setAutoChangeColor(isEnable: Boolean, activeDuration: AutoChangeColorDuration) {
        launchInScope {
            AUTO_COLOR_CHANGED_KEY.putInStorage(isEnable)
            if (isEnable) {
                AutoColorChangeWorker.init(appContext, activeDuration)
            } else {
                AutoColorChangeWorker.cancel(appContext)
            }
        }
    }

    override suspend fun getActiveAutoColorChangeDuration(): AutoChangeColorDuration {
        return AUTO_COLOR_CHANGED_DURATION
            .getFromStorage("")
            .takeIf { it.isNotEmpty() }
            ?.let {
                gson.fromJson(it, AutoChangeColorDuration::class.java)
            } ?: AutoChangeColorDuration.every1Hour()
    }

    override suspend fun getAutoColorChangeDurationList(): List<AutoChangeColorDuration> {
        return AutoChangeColorDuration.getList()
    }

    override fun updateAutoColorChangeDuration(autoChangeColorDuration: AutoChangeColorDuration) {
        launchInScope {
            AUTO_COLOR_CHANGED_DURATION.putInStorage(
                gson.toJson(autoChangeColorDuration)
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