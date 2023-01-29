package pk.sufiishq.app.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import pk.sufiishq.app.R
import pk.sufiishq.aurora.components.SIIcon
import pk.sufiishq.aurora.components.SIImage
import pk.sufiishq.aurora.components.SIText
import pk.sufiishq.aurora.components.SIWidthSpace
import pk.sufiishq.aurora.components.TextSize
import pk.sufiishq.aurora.layout.SIBox
import pk.sufiishq.aurora.layout.SICard
import pk.sufiishq.aurora.layout.SIColumn
import pk.sufiishq.aurora.layout.SIRow
import pk.sufiishq.aurora.theme.AuroraColor

@Composable
fun DayNightThemeControl(
    firstItemTranslationY: State<Float>,
    scaleAndVisibility: State<Float>,
    onLightModeCLick: () -> Unit,
    onDarkModeClick: () -> Unit,
    onAutoModeClick: () -> Unit
) {
    SIBox(
        modifier = Modifier
            .height(220.dp)
            .graphicsLayer {
                translationY = firstItemTranslationY.value * 0.5f
                alpha = 1f - scaleAndVisibility.value
            }
    ) {
        SIRow(modifier = Modifier.fillMaxSize()) {
            SICard(
                bgColor = AuroraColor.Light,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable {
                        onLightModeCLick()
                    }
            ) {
                SIBox(modifier = Modifier.padding(60.dp)) {
                    SIColumn(
                        modifier = Modifier
                            .fillMaxHeight()
                            .align(Alignment.Center),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        SIImage(
                            modifier = Modifier
                                .width(60.dp)
                                .graphicsLayer {
                                    translationY = firstItemTranslationY.value * 0.12f
                                    scaleX = 1f - scaleAndVisibility.value * 0.5f
                                    scaleY = 1f - scaleAndVisibility.value * 0.5f
                                    rotationZ = firstItemTranslationY.value * 0.25f
                                    alpha = 0.8f
                                },
                            resId = R.drawable.day,
                            tintColor = AuroraColor.Dark
                        )
                        SIText(
                            text = "Light",
                            textColor = AuroraColor.Dark,
                            textSize = TextSize.Large
                        )
                    }
                }
            }
            SIWidthSpace(value = 12)
            SICard(
                bgColor = AuroraColor.Dark,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable {
                        onDarkModeClick()
                    }
            ) {
                SIBox(modifier = Modifier.padding(60.dp)) {
                    SIColumn(
                        modifier = Modifier
                            .fillMaxHeight()
                            .align(Alignment.Center),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        SIImage(
                            modifier = Modifier
                                .width(60.dp)
                                .graphicsLayer {
                                    translationY = firstItemTranslationY.value * 0.12f
                                    scaleX = 1f - scaleAndVisibility.value * 0.5f
                                    scaleY = 1f - scaleAndVisibility.value * 0.5f
                                    rotationZ = (firstItemTranslationY.value * 0.25f) * -1f
                                    alpha = 0.8f
                                },
                            resId = R.drawable.night,
                            tintColor = AuroraColor.Light
                        )
                        SIText(
                            text = "Dark",
                            textColor = AuroraColor.Light,
                            textSize = TextSize.Large
                        )
                    }
                }
            }
        }

        SICard(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape),
            bgColor = AuroraColor.Background
        ) {

            ContentBackground {
                SICard(
                    modifier = Modifier
                        .fillMaxSize(0.75f)
                        .clip(CircleShape)
                        .clickable {
                            onAutoModeClick()
                        },
                    bgColor = AuroraColor.SecondaryVariant
                ) { contentColor ->
                    SIIcon(
                        modifier = Modifier.padding(8.dp),
                        resId = R.drawable.day_night,
                        tint = contentColor,
                    )
                }
            }
        }
    }
}