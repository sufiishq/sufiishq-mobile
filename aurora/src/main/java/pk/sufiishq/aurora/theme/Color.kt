package pk.sufiishq.aurora.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

enum class AuroraColor(val color: @Composable () -> Color) {

    Primary(
        {
            MaterialTheme.colors.primary
        }
    ),
    PrimaryVariant(
        {
            MaterialTheme.colors.primaryVariant
        }
    ),
    OnPrimary(
        {
            MaterialTheme.colors.onPrimary
        }
    ),
    Secondary(
        {
            MaterialTheme.colors.secondary
        }
    ),
    SecondaryVariant(
        {
            MaterialTheme.colors.secondaryVariant
        }
    ),
    OnSecondary(
        {
            MaterialTheme.colors.onSecondary
        }
    ),
    Background(
        {
            MaterialTheme.colors.background
        }
    ),
    OnBackground(
        {
            MaterialTheme.colors.onBackground
        }
    ),
    Surface(
        {
            MaterialTheme.colors.surface
        }
    ),
    OnSurface(
        {
            MaterialTheme.colors.onSurface
        }
    ),
    OnError(
        {
            MaterialTheme.colors.onError
        }
    ),
    OnSecondaryDarkVariant(
        {
            Color(0xFF181818)
        }
    ),
    OnSecondaryLightVariant(
        {
            Color(0xFFFDFDFD)
        }
    ),
    White(
        {
            Color(0xFFFFFFFF)
        }
    ),
    Green(
        {
            Color(0xFF66A528)
        }
    ),
    Disabled(
        {
            Color(0xFF919191)
        }
    ),
    Light(
        {
            Color(0xFFFFFFFF)
        }
    ),
    Dark(
        {
            Color(0xFF2C2C2C)
        }
    ),
    Transparent(
        {
            Color.Transparent
        }
    ),
}

fun AuroraColor.getForegroundColor(bgColor: Color) = when (this) {
    AuroraColor.Primary, AuroraColor.PrimaryVariant -> AuroraColor.OnPrimary
    AuroraColor.Secondary, AuroraColor.SecondaryVariant -> calculateForegroundColor(bgColor)
    AuroraColor.Surface -> AuroraColor.OnSurface
    AuroraColor.Background -> AuroraColor.OnBackground
    else -> AuroraColor.OnBackground
}

fun AuroraColor.getBackgroundColor() = when (this) {
    AuroraColor.OnPrimary -> AuroraColor.Primary
    AuroraColor.OnSecondary -> AuroraColor.Secondary
    AuroraColor.OnSurface -> AuroraColor.Surface
    AuroraColor.OnBackground -> AuroraColor.Background
    else -> AuroraColor.Background
}

fun AuroraColor.validateBackground() = when (this) {
    AuroraColor.Primary,
    AuroraColor.PrimaryVariant,
    AuroraColor.Secondary,
    AuroraColor.SecondaryVariant,
    AuroraColor.Background,
    AuroraColor.Surface,
    AuroraColor.Transparent -> this
    else -> throw IllegalArgumentException("${this.name} is not a valid background color")
}

fun AuroraColor.validateForeground() = when (this) {
    AuroraColor.OnPrimary,
    AuroraColor.OnSecondary,
    AuroraColor.OnBackground,
    AuroraColor.OnError,
    AuroraColor.OnSurface -> this
    else -> throw IllegalArgumentException("${this.name} is not a valid foreground color")
}

private fun calculateForegroundColor(bgColor: Color) : AuroraColor {
    val r = bgColor.red * 255
    val g = bgColor.green * 255
    val b = bgColor.blue * 255
    val yiq = (r * 299 + g * 587 + b * 114) / 1000
    return if(yiq >= 180) AuroraColor.OnSecondaryDarkVariant else AuroraColor.OnSecondaryLightVariant
}