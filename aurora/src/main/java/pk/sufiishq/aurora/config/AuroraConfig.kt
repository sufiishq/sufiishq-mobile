package pk.sufiishq.aurora.config

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import pk.sufiishq.aurora.models.ColorPalette
import pk.sufiishq.aurora.theme.AuroraColorPalettes

object AuroraConfig {

    private val colorPaletteStateFlow = MutableStateFlow<ColorPalette>(ColorPalette.Clover)
    private var sharedPreferences: SharedPreferences? = null
    private const val AURORA_THEME_COLOR_KEY = "aurora_theme_color_key"

    internal fun getDefaultColorPalette(appContext: Context): StateFlow<ColorPalette> {
        colorPaletteStateFlow.tryEmit(
            getActiveColorPalette(appContext)
        )
        return colorPaletteStateFlow
    }

    fun updatePalette(colorPalette: ColorPalette, appContext: Context) {
        colorPaletteStateFlow.tryEmit(colorPalette)
        updateColorPalette(colorPalette, appContext)
    }

    fun getAvailableColorPalettes(): List<ColorPalette> {
        return AuroraColorPalettes.getColorPallets()
    }

    fun getActiveColorPalette(appContext: Context): ColorPalette {
        return getSharedPreference(appContext)
            .getString(AURORA_THEME_COLOR_KEY, null)
            ?.let { colorName ->
                ColorPalette::class
                    .nestedClasses
                    .toList()
                    .map {
                        it.objectInstance as ColorPalette
                    }.firstOrNull { it.name == colorName }
            } ?: ColorPalette.Clover
    }

    private fun updateColorPalette(colorPalette: ColorPalette, appContext: Context) {
        val colorName = colorPalette.name
        getSharedPreference(appContext).edit().putString(AURORA_THEME_COLOR_KEY, colorName).apply()
    }

    private fun getSharedPreference(appContext: Context): SharedPreferences {
        return sharedPreferences ?: appContext.getSharedPreferences("Aurora", Context.MODE_PRIVATE)
            .also {
                sharedPreferences = it
            }
    }
}