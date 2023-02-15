package pk.sufiishq.aurora.widgets

import androidx.annotation.DrawableRes
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.toSize
import pk.sufiishq.aurora.components.SIImage
import pk.sufiishq.aurora.layout.SIBox

@Composable
fun SITransformableImage(
    @DrawableRes resId: Int,
    rotationEnable: Boolean = false
) {

    // set up all transformation states
    val scale = remember { mutableStateOf(1f) }
    val rotation = remember { mutableStateOf(0f) }
    val offset = remember { mutableStateOf(Offset.Zero) }

    val size = remember { mutableStateOf(Size(100f, 100f)) }

    SIBox(
        modifier = Modifier
            .fillMaxSize()
            .onGloballyPositioned {
                size.value = it.size.toSize()
            }
    ) {

        SIImage(
            resId = resId,
            modifier = Modifier
                .graphicsLayer {
                    scaleX = scale.value
                    scaleY = scale.value
                    rotationZ = rotation.value
                    translationX = offset.value.x
                    translationY = offset.value.y
                }
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, rot ->
                        scale.value = (scale.value * zoom).coerceIn(1f, 5f)
                        val newOffset = offset.value + pan * scale.value

                        val x = ((scale.value * size.value.width) - size.value.width) / 2
                        val y = ((scale.value * size.value.height) - size.value.height) / 2

                        offset.value = Offset(
                            x = newOffset.x.coerceIn(x * -1, x),
                            y = newOffset.y.coerceIn(y * -1, y)
                        )

                        if (rotationEnable) rotation.value += rot
                    }
                }
        )
    }

}