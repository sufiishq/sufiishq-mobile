package pk.sufiishq.app.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import pk.sufiishq.app.R
import pk.sufiishq.aurora.components.SIImage
import pk.sufiishq.aurora.layout.SIBox
import pk.sufiishq.aurora.layout.SIRow

@Composable
fun HelpHeader(
    firstItemTranslationY: State<Float>,
    scaleAndVisibility: State<Float>,
) {
    SIBox(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
    ) {

        SIRow(
            modifier = Modifier.fillMaxSize()
        ) {
            SIBox(
                padding = 6,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                SIImage(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .graphicsLayer {
                            translationY = firstItemTranslationY.value * 0.25f
                            translationX = (firstItemTranslationY.value * 0.67f) * -1f
                            scaleX = (1f - scaleAndVisibility.value).coerceIn(0f, 1f)
                            scaleY = (1f - scaleAndVisibility.value).coerceIn(0f, 1f)
                            alpha = scaleX
                        },
                    resId = R.drawable.help_left_hand
                )
            }
            SIBox(
                padding = 6,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                SIImage(
                    modifier = Modifier
                        .graphicsLayer {
                            translationY = firstItemTranslationY.value * 0.25f
                            translationX = firstItemTranslationY.value * 0.67f
                            scaleX = (1f - scaleAndVisibility.value).coerceIn(0f, 1f)
                            scaleY = (1f - scaleAndVisibility.value).coerceIn(0f, 1f)
                            alpha = scaleX
                        }
                        .align(Alignment.BottomStart),
                    resId = R.drawable.help_right_hand
                )
            }
        }

        SIImage(
            modifier = Modifier
                .graphicsLayer {
                    translationY = firstItemTranslationY.value * 0.6f
                    scaleX = (1f - scaleAndVisibility.value).coerceIn(0f, 1f)
                    scaleY = (1f - scaleAndVisibility.value).coerceIn(0f, 1f)
                    alpha = scaleX
                }
                .align(Alignment.TopCenter)
                .padding(30.dp),
            resId = R.drawable.help_main
        )
    }
}