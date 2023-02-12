package pk.sufiishq.aurora.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import pk.sufiishq.app.ui.theme.Shapes
import pk.sufiishq.aurora.config.AuroraConfig

enum class Theme {
    Light, Dark, Auto
}

@Composable
fun AuroraLight(content: @Composable () -> Unit) {
    Aurora(
        darkTheme = false,
        content = {
            Surface(
                color = MaterialTheme.colors.surface
            ) {
                content()
            }
        }
    )
}

@Composable
fun AuroraDark(content: @Composable () -> Unit) {
    Aurora(
        darkTheme = true,
        content = {
            Surface(
                color = MaterialTheme.colors.surface
            ) {
                content()
            }
        }
    )
}

@Composable
fun Aurora(content: @Composable () -> Unit) {
    Aurora(
        darkTheme = isSystemInDarkTheme(),
        content = content
    )
}

@Composable
private fun Aurora(darkTheme: Boolean, content: @Composable () -> Unit) {

    val activeTheme = AuroraConfig.getDefaultTheme(LocalContext.current).collectAsState().value

    val activeSecondaryColorPalette =
        AuroraConfig.getDefaultColorPalette(LocalContext.current).collectAsState().value

    val isDark = when (activeTheme) {
        Theme.Auto -> darkTheme
        Theme.Light -> false
        Theme.Dark -> true
    }

    val colors = (if (isDark) {
        DarkColorPalette
    } else {
        LightColorPalette
    }).copy(
        secondary = activeSecondaryColorPalette.color.first,
        secondaryVariant = activeSecondaryColorPalette.color.second
    )

    val systemUiController = rememberSystemUiController()

    SideEffect {
        // Update all of the system bar colors to be transparent, and use
        // dark icons if we're in light theme
        systemUiController.setSystemBarsColor(
            color = colors.background,
            darkIcons = !isDark
        )

        // setStatusBarColor() and setNavigationBarColor() also exist
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )

}