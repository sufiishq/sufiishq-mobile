package pk.sufiishq.app.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pk.sufiishq.app.R
import pk.sufiishq.app.utils.rem
import pk.sufiishq.aurora.components.SIButton
import pk.sufiishq.aurora.components.SIImage
import pk.sufiishq.aurora.components.SIText
import pk.sufiishq.aurora.components.SIWidthSpace
import pk.sufiishq.aurora.config.AuroraConfig
import pk.sufiishq.aurora.layout.SIBox
import pk.sufiishq.aurora.layout.SICard
import pk.sufiishq.aurora.layout.SIColumn
import pk.sufiishq.aurora.layout.SIRow
import pk.sufiishq.aurora.models.ColorPalette
import pk.sufiishq.aurora.theme.AuroraColor
import pk.sufiishq.aurora.widgets.SIColorPaletteView

@Composable
fun ThemeView() {

    val colorPalettes = rem(listOf<ColorPalette>())

    LaunchedEffect(key1 = true) {
        colorPalettes.value = AuroraConfig.getAvailableColorPalettes()
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        item(span = {
            GridItemSpan(maxLineSpan)
        }) {
            SIBox (modifier = Modifier.height(100.dp)) {
                SIRow (modifier = Modifier.fillMaxSize()) {
                    SICard(
                        bgColor = AuroraColor.Light,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable { }
                    ) {
                        SIBox(modifier = Modifier.padding(36.dp, 16.dp)) {
                            SIColumn(
                                modifier = Modifier.fillMaxHeight().align(Alignment.CenterStart),
                                verticalArrangement = Arrangement.SpaceBetween,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                SIImage(
                                    modifier = Modifier.width(40.dp),
                                    resId = R.drawable.day,
                                    tintColor = AuroraColor.Dark
                                )
                                SIText(text = "Light", textColor = AuroraColor.Dark)
                            }
                        }
                    }
                    SIWidthSpace(value = 12)
                    SICard(
                        bgColor = AuroraColor.Dark,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable { }
                    ) {
                        SIBox(modifier = Modifier.padding(36.dp, 16.dp)) {
                            SIColumn(
                                modifier = Modifier.fillMaxHeight().align(Alignment.CenterEnd),
                                verticalArrangement = Arrangement.SpaceBetween,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                SIImage(
                                    modifier = Modifier.width(36.dp),
                                    resId = R.drawable.night,
                                    tintColor = AuroraColor.Light
                                )
                                SIText(text = "Dark", textColor = AuroraColor.Light)
                            }
                        }
                    }
                }
            }
        }

        items(colorPalettes.value) {
            SIColorPaletteView(colorPalette = it, onClick = {})
        }
    }
}