package pk.sufiishq.app.feature.theme.controller

import pk.sufiishq.app.feature.theme.model.AutoChangeColorDuration
import pk.sufiishq.aurora.models.ColorPalette

interface ThemeController {

    suspend fun isAutoChangeColorEnable(): Boolean
    fun setAutoChangeColor(isEnable: Boolean, activeDuration: AutoChangeColorDuration)
    suspend fun getActiveAutoColorChangeDuration(): AutoChangeColorDuration
    suspend fun getAutoColorChangeDurationList(): List<AutoChangeColorDuration>
    fun updateAutoColorChangeDuration(autoChangeColorDuration: AutoChangeColorDuration)
    suspend fun getAvailableColorPalettes(): List<ColorPalette>
}