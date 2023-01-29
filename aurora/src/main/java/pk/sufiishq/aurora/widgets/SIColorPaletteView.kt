package pk.sufiishq.aurora.widgets

import androidx.annotation.IntRange
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pk.sufiishq.aurora.annotations.ExcludeFromJacocoGeneratedReport
import pk.sufiishq.aurora.components.SIText
import pk.sufiishq.aurora.components.TextSize
import pk.sufiishq.aurora.layout.SIBox
import pk.sufiishq.aurora.layout.SIColumn
import pk.sufiishq.aurora.models.ColorPalette
import pk.sufiishq.aurora.theme.AuroraColor
import pk.sufiishq.aurora.theme.AuroraDark
import pk.sufiishq.aurora.theme.AuroraLight

@Composable
fun SIColorPaletteView(
    modifier: Modifier = Modifier,
    bgColor: AuroraColor = AuroraColor.Background,
    colorPalette: ColorPalette,
    @IntRange(from = 50, to = 65) size: Int = 50,
    nameFilter: ((String) -> String)? = null,
    onClick: (ColorPalette) -> Unit
) {

    Card(
        modifier = modifier
            .width(100.dp)
            .height(100.dp)
            .clickable {
                onClick(colorPalette)
            },
        backgroundColor = bgColor.color()
    ) {
        SIColumn(
            modifier = Modifier.fillMaxSize(),
            padding = 8,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) { textColor ->
            SIBox(
                modifier = Modifier.size(size.coerceIn(50, 65).dp)
            ) {

                Canvas(
                    modifier = Modifier.fillMaxSize(),
                ) {

                    val canvasWidth = this.size.width
                    val canvasHeight = this.size.height

                    drawArc(
                        colorPalette.color.first,
                        startAngle = 90f,
                        sweepAngle = 180f,
                        useCenter = true,
                        size = Size(canvasWidth, canvasHeight)
                    )

                    drawArc(
                        colorPalette.color.second,
                        startAngle = 270f,
                        sweepAngle = 180f,
                        useCenter = true,
                        size = Size(canvasWidth, canvasHeight)
                    )
                }
            }

            SIText(
                text = nameFilter?.invoke(colorPalette.name) ?: colorPalette.name,
                textColor = textColor,
                textSize = TextSize.Small,
                textAlign = TextAlign.Center
            )
        }
    }
}

@ExcludeFromJacocoGeneratedReport
@Preview(showBackground = true)
@Composable
fun LightPreview() {
    AuroraLight {
        SIColorPaletteView(colorPalette = ColorPalette.Clover, onClick = {})
    }
}

@ExcludeFromJacocoGeneratedReport
@Preview(showBackground = true)
@Composable
fun DarkPreview() {
    AuroraDark {
        SIColorPaletteView(colorPalette = ColorPalette.Clover, onClick = {})
    }
}