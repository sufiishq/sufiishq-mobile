package pk.sufiishq.aurora.theme

import android.annotation.SuppressLint
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color
import pk.sufiishq.aurora.models.ColorPalette

@SuppressLint("ConflictingOnColor")
internal val DarkColorPalette = darkColors(
    surface = Color(0xFF121313),
    background = Color(0xFF2C2C2C),
    primary = Color(0xFF2C2C2C),
    primaryVariant = Color(0xFF272727),
    secondary = Color(0xFF8BEE00),
    secondaryVariant = Color(0xFF68B300),
    onBackground = Color(0xFFDDDDDD),
    onPrimary = Color(0xFFEEEEEE),
    onSecondary = Color(0xFFF4FFE5),
    onError = Color(0xFFFD647F)
)

@SuppressLint("ConflictingOnColor")
internal val LightColorPalette = lightColors(
    surface = Color(0xFFEFF2F3),
    background = Color(0xFFFFFFFF),
    primary = Color(0xFFFFFFFF),
    primaryVariant = Color(0xFFF7F7F7),
    secondary = Color(0xFF8BEE00),
    secondaryVariant = Color(0xFF68B300),
    onBackground = Color(0xFF2B2C2E),
    onPrimary = Color(0xFF424242),
    onSecondary = Color(0xFFF4FFE5),
    onError = Color(0xFFDB2545)
)

internal object AuroraColorPalettes {
    private var colorPallets: List<ColorPalette>? = null

    fun getColorPallets(): List<ColorPalette> {
        return colorPallets ?: listOf(
            ColorPalette.Clover,
            ColorPalette.Ocean,
            ColorPalette.Blackberry,
            ColorPalette.Blueberry,
            ColorPalette.Mud,
            ColorPalette.Kiwi,
            ColorPalette.Mulberry,
            ColorPalette.Mint,
            ColorPalette.Antler,
            ColorPalette.Camel,
            ColorPalette.Lettuce,
            ColorPalette.Fox,
            ColorPalette.Cherry,
            ColorPalette.Koi,
            ColorPalette.Ice,
            ColorPalette.Sand,
            ColorPalette.Clementine,
            ColorPalette.Cashew,
            ColorPalette.Coral,
            ColorPalette.Marigold,
            ColorPalette.Carrot,
            ColorPalette.Canary,
            ColorPalette.Papaya,
            ColorPalette.Royal,
            ColorPalette.Olive,
            ColorPalette.Forest,
            ColorPalette.Berry,
            ColorPalette.Grape
        ).also {
            colorPallets = it
        }
    }
}