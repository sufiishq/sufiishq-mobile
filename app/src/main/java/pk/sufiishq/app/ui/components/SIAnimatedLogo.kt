package pk.sufiishq.app.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import pk.sufiishq.app.R
import pk.sufiishq.aurora.components.SIImage

@Composable
fun SIAnimatedLog(
    modifier: Modifier = Modifier
) {

    val infiniteTransition = rememberInfiniteTransition()
    val angle by infiniteTransition.animateFloat(
        initialValue = 0F,
        targetValue = 360F,
        animationSpec = infiniteRepeatable(
            animation = tween(15000, easing = LinearEasing)
        )
    )

    SIImage(
        modifier = modifier
            .testTag("logo")
            .graphicsLayer {
                rotationY = angle
            },
        resId = R.drawable.logo,
    )
}