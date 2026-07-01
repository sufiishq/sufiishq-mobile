package pk.sufiishq.aurora.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

enum class AuroraColor(val color: @Composable () -> Color) {

    Primary(
        {
            MaterialTheme.colorScheme.primary
        }
    ),
    PrimaryContainer(
        {
            MaterialTheme.colorScheme.primaryContainer
        }
    ),
    OnPrimary(
        {
            MaterialTheme.colorScheme.onPrimary
        }
    ),
    Secondary(
        {
            MaterialTheme.colorScheme.secondary
        }
    ),
    SecondaryContainer(
        {
            MaterialTheme.colorScheme.secondaryContainer
        }
    ),
    OnSecondary(
        {
            MaterialTheme.colorScheme.onSecondary
        }
    ),
    Background(
        {
            MaterialTheme.colorScheme.background
        }
    ),
    OnBackground(
        {
            MaterialTheme.colorScheme.onBackground
        }
    ),
    Surface(
        {
            MaterialTheme.colorScheme.surface
        }
    ),
    OnSurface(
        {
            MaterialTheme.colorScheme.onSurface
        }
    ),
    OnError(
        {
            MaterialTheme.colorScheme.onError
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
    AuroraColor.Primary, AuroraColor.PrimaryContainer -> AuroraColor.OnPrimary
    AuroraColor.Secondary, AuroraColor.SecondaryContainer -> calculateForegroundColor(bgColor)
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
    AuroraColor.PrimaryContainer,
    AuroraColor.Secondary,
    AuroraColor.SecondaryContainer,
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
    return if(yiq >= 170) AuroraColor.OnSecondaryDarkVariant else AuroraColor.OnSecondaryLightVariant
}