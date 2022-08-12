package pk.sufiishq.app.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import pk.sufiishq.app.utils.isDarkThem

private val DarkColorPalette = darkColors(
    primary = DarkPrimary,
    primaryVariant = DarkPrimaryVariant,
    secondary = DarkSecondary,
    background = DarkBackground,
    secondaryVariant = DarkSecondaryVariant
)

private val LightColorPalette = lightColors(
    primary = LightPrimary,
    primaryVariant = LightPrimaryVariant,
    secondary = LightSecondary,
    background = LightBackground,
    secondaryVariant = LightSecondaryVariant
)

@Composable
fun SufiIshqTheme(darkTheme: Boolean = isDarkThem(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    val systemUiController = rememberSystemUiController()

    SideEffect {
        // Update all of the system bar colors to be transparent, and use
        // dark icons if we're in light theme
        systemUiController.setSystemBarsColor(
            color = colors.background,
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