package pk.sufiishq.app.ui.screen

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import pk.sufiishq.app.ui.components.DayNightThemeControl
import pk.sufiishq.app.utils.maxLength
import pk.sufiishq.app.utils.rem
import pk.sufiishq.app.utils.toastShort
import pk.sufiishq.aurora.config.AuroraConfig
import pk.sufiishq.aurora.layout.SILazyVerticalGrid
import pk.sufiishq.aurora.models.ColorPalette
import pk.sufiishq.aurora.theme.Theme
import pk.sufiishq.aurora.widgets.SIColorPaletteView

@Composable
fun ThemeView() {

    val context = LocalContext.current
    val colorPalettes = rem(listOf<ColorPalette>())

    LaunchedEffect(key1 = true) {
        colorPalettes.value = AuroraConfig.getAvailableColorPalettes()
    }

    val lazyListState = rememberLazyGridState()

    val firstItemTranslationY = remember {
        derivedStateOf {
            if (lazyListState.firstVisibleItemIndex == 0) {
                lazyListState.firstVisibleItemScrollOffset.toFloat()
            } else {
                0f
            }
        }
    }

    val scaleAndVisibility = remember {
        derivedStateOf {
            if (lazyListState.firstVisibleItemIndex == 0) {
                val imageSize = lazyListState.layoutInfo.visibleItemsInfo[0].size
                val scrollOffset = lazyListState.firstVisibleItemScrollOffset
                scrollOffset / imageSize.height.toFloat()
            } else {
                0f
            }
        }
    }

    SILazyVerticalGrid(
        columns = GridCells.Fixed(4),
        state = lazyListState
    ) {

        item(span = {
            GridItemSpan(maxLineSpan)
        }) {
            DayNightThemeControl(
                firstItemTranslationY = firstItemTranslationY,
                scaleAndVisibility = scaleAndVisibility,
                onLightModeCLick = {
                    AuroraConfig.updateTheme(Theme.Light, context)
                    context.toastShort("Light Mode On")
                },
                onDarkModeClick = {
                    AuroraConfig.updateTheme(Theme.Dark, context)
                    context.toastShort("Dark Mode On")
                },
                onAutoModeClick = {
                    AuroraConfig.updateTheme(Theme.Auto, context)
                    context.toastShort("Auto Mode On")
                }
            )
        }

        items(colorPalettes.value) {
            SIColorPaletteView(
                colorPalette = it,
                nameFilter = { name ->
                    name.maxLength(6, "...")
                },
                onClick = { selectedColorPalette ->
                    AuroraConfig.updatePalette(selectedColorPalette, context)
                }
            )
        }
    }
}