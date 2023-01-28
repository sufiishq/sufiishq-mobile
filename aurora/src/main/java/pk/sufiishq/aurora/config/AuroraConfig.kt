package pk.sufiishq.aurora.config

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import pk.sufiishq.aurora.models.ColorPalette
import pk.sufiishq.aurora.theme.AuroraColorPalettes
import pk.sufiishq.aurora.theme.Theme

object AuroraConfig {

    private const val AURORA_THEME_MODE_KEY = "aurora_theme_mode_key"
    private const val AURORA_THEME_COLOR_KEY = "aurora_theme_color_key"

    private val colorPaletteStateFlow = MutableStateFlow<ColorPalette>(ColorPalette.Clover)
    private val themeModeStateFlow = MutableStateFlow(Theme.Auto)
    private var sharedPreferences: SharedPreferences? = null

    internal fun getDefaultTheme(appContext: Context): StateFlow<Theme> {
        themeModeStateFlow.tryEmit(getActiveTheme(appContext))
        return themeModeStateFlow
    }

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

    fun getActiveTheme(appContext: Context): Theme {
        return getSharedPreference(appContext)
            .getString(AURORA_THEME_MODE_KEY, null)
            ?.let { mode ->
                when (mode) {
                    Theme.Light.name -> Theme.Light
                    Theme.Dark.name -> Theme.Dark
                    else -> Theme.Auto
                }
            } ?: Theme.Auto
    }

    fun updateTheme(themeMode: Theme, appContext: Context) {
        themeModeStateFlow.tryEmit(themeMode)
        updateThemeMode(themeMode, appContext)
    }

    private fun updateThemeMode(themeMode: Theme, appContext: Context) {
        getSharedPreference(appContext).edit().putString(AURORA_THEME_MODE_KEY, themeMode.name)
            .apply()
    }

    private fun getSharedPreference(appContext: Context): SharedPreferences {
        return sharedPreferences ?: appContext.getSharedPreferences("Aurora", Context.MODE_PRIVATE)
            .also {
                sharedPreferences = it
            }
    }
}