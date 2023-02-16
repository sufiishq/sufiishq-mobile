/*
 * Copyright 2022-2023 SufiIshq
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pk.sufiishq.app.ui.screen.theme

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import pk.sufiishq.app.R
import pk.sufiishq.app.utils.maxLength
import pk.sufiishq.app.utils.quickToast
import pk.sufiishq.app.utils.rem
import pk.sufiishq.aurora.config.AuroraConfig
import pk.sufiishq.aurora.layout.SILazyVerticalGrid
import pk.sufiishq.aurora.models.ColorPalette
import pk.sufiishq.aurora.theme.Theme
import pk.sufiishq.aurora.widgets.SIColorPaletteView

@Composable
fun ThemeScreen() {
    val context = LocalContext.current
    val colorPalettes = rem(listOf<ColorPalette>())

    LaunchedEffect(key1 = Unit) { colorPalettes.value = AuroraConfig.getAvailableColorPalettes() }

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
        state = lazyListState,
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            DayNightThemeControl(
                firstItemTranslationY = firstItemTranslationY,
                scaleAndVisibility = scaleAndVisibility,
                onLightModeCLick = {
                    AuroraConfig.updateTheme(Theme.Light, context)
                    quickToast(R.string.msg_light_mode_on)
                },
                onDarkModeClick = {
                    AuroraConfig.updateTheme(Theme.Dark, context)
                    quickToast(R.string.msg_dark_mode_on)
                },
                onAutoModeClick = {
                    AuroraConfig.updateTheme(Theme.Auto, context)
                    quickToast(R.string.msg_auto_mode_on)
                },
            )
        }

        items(colorPalettes.value) {
            SIColorPaletteView(
                colorPalette = it,
                nameFilter = { name -> name.maxLength(6, "...") },
                onClick = { selectedColorPalette ->
                    AuroraConfig.updatePalette(selectedColorPalette, context)
                },
            )
        }
    }
}
