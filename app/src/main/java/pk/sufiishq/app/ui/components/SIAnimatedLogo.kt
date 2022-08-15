package pk.sufiishq.app.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.material.Colors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import pk.sufiishq.app.R

@Composable
fun SIAnimatedLog(
    modifier: Modifier
) {

    val infiniteTransition = rememberInfiniteTransition()
    val angle by infiniteTransition.animateFloat(
        initialValue = 0F,
        targetValue = 360F,
        animationSpec = infiniteRepeatable(
            animation = tween(15000, easing = LinearEasing)
        )
    )

    Image(
        modifier = modifier.graphicsLayer {
            rotationY = angle
        },
        contentScale = ContentScale.Fit,
        painter = painterResource(id = R.drawable.logo),
        contentDescription = null
    )
}