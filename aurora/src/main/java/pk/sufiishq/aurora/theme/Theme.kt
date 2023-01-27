package pk.sufiishq.aurora.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import pk.sufiishq.app.ui.theme.Shapes
import pk.sufiishq.aurora.config.AuroraConfig

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

    val activeSecondaryColorPalette =
        AuroraConfig.getDefaultColorPalette(LocalContext.current).collectAsState()

    val colors = (if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }).copy(
        secondary = activeSecondaryColorPalette.value.color.first,
        secondaryVariant = activeSecondaryColorPalette.value.color.second
    )

    val systemUiController = rememberSystemUiController()

    SideEffect {
        // Update all of the system bar colors to be transparent, and use
        // dark icons if we're in light theme
        systemUiController.setSystemBarsColor(
            color = colors.primary,
            darkIcons = !darkTheme
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