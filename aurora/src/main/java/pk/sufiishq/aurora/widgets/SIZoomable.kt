package pk.sufiishq.aurora.widgets

import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import pk.sufiishq.aurora.layout.SIBox

@Composable
fun SIZoomable(
    rotationEnable: Boolean = false,
    content: @Composable () -> Unit
) {

    // set up all transformation states
    val scale = remember { mutableStateOf(1f) }
    val rotation = remember { mutableStateOf(0f) }
    val offset = remember { mutableStateOf(Offset.Zero) }

    SIBox(
        modifier = Modifier.fillMaxSize()
    ) {
        SIBox(
            modifier = Modifier
                .graphicsLayer {
                    scaleX = scale.value
                    scaleY = scale.value
                    rotationZ = rotation.value
                    translationX = offset.value.x
                    translationY = offset.value.y

                }
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, rot ->
                        scale.value = (scale.value * zoom).coerceIn(1f, 3f)
                        offset.value += pan
                        if (rotationEnable) rotation.value += rot
                    }
                }
        ) {
            content()
        }
    }

}