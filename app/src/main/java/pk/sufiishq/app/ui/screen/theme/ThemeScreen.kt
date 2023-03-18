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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import pk.sufiishq.app.feature.theme.controller.ThemeController
import pk.sufiishq.app.feature.theme.controller.ThemeViewModel
import pk.sufiishq.app.feature.theme.model.AutoChangeColorDuration
import pk.sufiishq.app.utils.ImageRes
import pk.sufiishq.app.utils.TextRes
import pk.sufiishq.app.utils.maxLength
import pk.sufiishq.app.utils.quickToast
import pk.sufiishq.app.utils.rem
import pk.sufiishq.aurora.components.SIHeightSpace
import pk.sufiishq.aurora.components.SIText
import pk.sufiishq.aurora.config.AuroraConfig
import pk.sufiishq.aurora.layout.SIColumn
import pk.sufiishq.aurora.layout.SILazyVerticalGrid
import pk.sufiishq.aurora.layout.SIRow
import pk.sufiishq.aurora.models.ColorPalette
import pk.sufiishq.aurora.theme.AuroraColor
import pk.sufiishq.aurora.theme.Theme
import pk.sufiishq.aurora.widgets.SIColorPaletteView
import pk.sufiishq.aurora.widgets.SIDataRow
import pk.sufiishq.aurora.widgets.SIPopupMenu

@Composable
fun ThemeScreen(
    themeController: ThemeController = hiltViewModel<ThemeViewModel>(),
) {
    val context = LocalContext.current
    val colorPalettes = rem(listOf<ColorPalette>())
    val autoColorChanged = remember {
        MutableTransitionState(false).apply {
            targetState = false
        }
    }
    val listOfAutoChangeColorDuration = rem(listOf<AutoChangeColorDuration>())
    val activeDuration = rem(AutoChangeColorDuration.every1Hour())
    val isExpanded = rem(false)

    LaunchedEffect(key1 = Unit) {
        colorPalettes.value = themeController.getAvailableColorPalettes()
        autoColorChanged.targetState = themeController.isAutoChangeColorEnable()
        listOfAutoChangeColorDuration.value = themeController.getAutoColorChangeDurationList()
        activeDuration.value = themeController.getActiveAutoColorChangeDuration() ?: AutoChangeColorDuration.every1Hour()
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

    SIColumn(
        modifier =
        Modifier
            .padding(start = 12.dp, top = 12.dp, end = 12.dp, bottom = 18.dp),
    ) {
        SIColumn(modifier = Modifier.fillMaxWidth()) { onColor ->
            SIRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp, 6.dp),
                radius = 4,
                bgColor = AuroraColor.Background,
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                SIText(
                    text = "Auto Color Change",
                    textColor = onColor,
                    fontWeight = FontWeight.Bold,
                )
                Switch(
                    checked = autoColorChanged.targetState,
                    onCheckedChange = { isChecked ->
                        autoColorChanged.targetState = isChecked
                        themeController.setAutoChangeColor(isChecked, activeDuration.value)
                    },
                )
            }

            AnimatedVisibility(autoColorChanged.targetState) {
                SIColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize(),
                ) {
                    SIHeightSpace(value = 12)

                    SIDataRow(
                        modifier = Modifier.fillMaxWidth(),
                        title = activeDuration.value.label,
                        trailingIcon = ImageRes.baseline_arrow_drop_down_24,
                        trailingIconColor = onColor,
                        onClick = { isExpanded.value = true },
                    )

                    SIPopupMenu(
                        isExpanded = isExpanded,
                        modifier = Modifier
                            .fillMaxWidth(),
                        data = listOfAutoChangeColorDuration.value,
                        onClick = {
                            activeDuration.value = it as AutoChangeColorDuration
                            themeController.updateAutoColorChangeDuration(activeDuration.value)
                        },
                    )
                }
            }
        }

        SILazyVerticalGrid(
            columns = GridCells.Fixed(4),
            state = lazyListState,
            contentPadding = PaddingValues(top = 12.dp),
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                DayNightThemeControl(
                    firstItemTranslationY = firstItemTranslationY,
                    scaleAndVisibility = scaleAndVisibility,
                    onLightModeCLick = {
                        AuroraConfig.updateTheme(Theme.Light, context)
                        quickToast(TextRes.msg_light_mode_on)
                    },
                    onDarkModeClick = {
                        AuroraConfig.updateTheme(Theme.Dark, context)
                        quickToast(TextRes.msg_dark_mode_on)
                    },
                    onAutoModeClick = {
                        AuroraConfig.updateTheme(Theme.Auto, context)
                        quickToast(TextRes.msg_auto_mode_on)
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
}
