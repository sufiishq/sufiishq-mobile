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
    primary = Color(0xFF383838),
    primaryVariant = Color(0xFF444444),
    secondary = Color(0xFF0F76C4),
    secondaryVariant = Color(0xFF005493),
    onBackground = Color(0xFFDDDDDD),
    onPrimary = Color(0xFFEEEEEE),
    onSecondary = Color(0xFFF4FFE5),
    onError = Color(0xFFE2425F)
)

@SuppressLint("ConflictingOnColor")
internal val LightColorPalette = lightColors(
    surface = Color(0xFFEFF2F3),
    background = Color(0xFFFCFCFC),
    primary = Color(0xFFE7E7E7),
    primaryVariant = Color(0xFFCACACA),
    secondary = Color(0xFF0F76C4),
    secondaryVariant = Color(0xFF005493),
    onBackground = Color(0xFF2B2C2E),
    onPrimary = Color(0xFF424242),
    onSecondary = Color(0xFFF4FFE5),
    onError = Color(0xFFE2425F)
)

internal object AuroraColorPalettes {
    private var colorPallets: List<ColorPalette>? = null

    fun getColorPallets(): List<ColorPalette> {
        return colorPallets ?: listOf(
            ColorPalette.Ocean,
            ColorPalette.Clover,
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
            ColorPalette.Grape,
            ColorPalette.Dolphin,
            ColorPalette.Granite,
            ColorPalette.Pigeons,
            ColorPalette.Slate,
            ColorPalette.Pearl,
            ColorPalette.Alabaster,
            ColorPalette.Snow,
            ColorPalette.Ivory,
            ColorPalette.Cream,
            ColorPalette.Salt,
            ColorPalette.Tan,
            ColorPalette.Beige,
            ColorPalette.Macaroon,
            ColorPalette.Sepia,
            ColorPalette.Yellow,
            ColorPalette.Gold,
            ColorPalette.Lemon,
            ColorPalette.Orange,
            ColorPalette.Tangerine,
            ColorPalette.Cider,
            ColorPalette.Rust,
            ColorPalette.Tiger,
            ColorPalette.Fire,
            ColorPalette.Cantaloupe,
            ColorPalette.Apricot,
            ColorPalette.Honey,
            ColorPalette.Amber,
            ColorPalette.Red,
            ColorPalette.Rose,
            ColorPalette.Jam,
            ColorPalette.Merlot,
            ColorPalette.Crimson,
            ColorPalette.Ruby,
            ColorPalette.Wine,
            ColorPalette.Blush,
            ColorPalette.Candy,
            ColorPalette.Pink,
            ColorPalette.Fuchsia,
            ColorPalette.Punch,
            ColorPalette.Magenta,
            ColorPalette.HotPink,
            ColorPalette.Purple,
            ColorPalette.Mauve,
            ColorPalette.Violet,
            ColorPalette.Plum,
            ColorPalette.Raisin,
            ColorPalette.Blue,
            ColorPalette.Sky,
            ColorPalette.Navy,
            ColorPalette.Teal,
            ColorPalette.Spruce,
            ColorPalette.Stone,
            ColorPalette.Aegean,
            ColorPalette.Denim,
            ColorPalette.Green,
            ColorPalette.Lime,
            ColorPalette.Juniper,
            ColorPalette.Sage,
            ColorPalette.Fern,
            ColorPalette.Seafoam,
            ColorPalette.Brown,
            ColorPalette.Coffee,
            ColorPalette.Peanut,
            ColorPalette.Caramel,
            ColorPalette.Shadow,
            ColorPalette.Flint,
            ColorPalette.Pebble,
            ColorPalette.Azure,
        ).also {
            colorPallets = it
        }
    }
}