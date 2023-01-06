package pk.sufiishq.app.ui.components.buttons

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pk.sufiishq.app.utils.rem

@Composable
fun UpdateButton(
    show: State<Boolean?>,
    modifier: Modifier,
    onClick: () -> Unit
) {

    if (show.value == true) {
        val matColor by rem(MaterialTheme.colors)

        val backgroundColorTransition by rememberInfiniteTransition().animateColor(
            initialValue = matColor.background,
            targetValue = matColor.primary,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 1500,
                    delayMillis = 1000,
                    easing = LinearOutSlowInEasing
                ),
                repeatMode = RepeatMode.Reverse
            )
        )

        val textColorTransition by rememberInfiniteTransition().animateColor(
            initialValue = matColor.primary,
            targetValue = matColor.background,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 1500,
                    delayMillis = 1000,
                    easing = LinearOutSlowInEasing
                ),
                repeatMode = RepeatMode.Reverse
            )
        )

        Box(
            modifier = modifier
                .background(
                    backgroundColorTransition,
                    shape = RoundedCornerShape(6.dp)
                )
                .clickable {
                    onClick()
                },

            ) {
            Text(
                modifier = Modifier.padding(10.dp, 8.dp),
                text = "Update Available",
                fontSize = 13.sp,
                color = textColorTransition
            )
        }
    }
}