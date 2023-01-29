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
    White(
        {
            Color(0xFFFFFFFF)
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

fun AuroraColor.getForegroundColor() = when (this) {
    AuroraColor.Primary, AuroraColor.PrimaryVariant -> AuroraColor.OnPrimary
    AuroraColor.Secondary, AuroraColor.SecondaryVariant -> AuroraColor.OnSecondary
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