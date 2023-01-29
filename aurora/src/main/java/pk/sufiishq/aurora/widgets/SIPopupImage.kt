package pk.sufiishq.aurora.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import pk.sufiishq.aurora.components.SIButton
import pk.sufiishq.aurora.layout.SIBox
import pk.sufiishq.aurora.layout.SICard
import pk.sufiishq.aurora.layout.SIDialog
import pk.sufiishq.aurora.theme.AuroraColor

@Composable
fun SIPopupImage(
    enableScaling: Boolean = false,
    enablePanning: Boolean = false,
    enableRotating: Boolean = false,
    enableBackground: Boolean = true,
    content: @Composable () -> Unit
) {

    val isPop = remember {
        mutableStateOf(false)
    }

    SIBox {
        if (enableBackground) {
            SICard(
                modifier = Modifier.clickable {
                    isPop.value = true
                },
                bgColor = AuroraColor.Surface,
                elevation = 2.dp,
                content = {
                    content()
                }
            )
        } else {
            content()
        }
    }

    if (isPop.value) {

        // set up all transformation states
        val scale = remember { mutableStateOf(1f) }
        val rotation = remember { mutableStateOf(0f) }
        val offset = remember { mutableStateOf(Offset.Zero) }

        SIDialog(
            modifier = Modifier.fillMaxHeight(0.8f),
            onDismissRequest = {
                isPop.value = false
            }
        ) {
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
                                if (enableScaling) scale.value =
                                    (scale.value * zoom).coerceIn(1f, 2f)
                                if (enablePanning) offset.value += pan
                                if (enableRotating) rotation.value += rot
                            }
                        }
                ) {
                    content()
                }

                SIButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter),
                    text = "OK",
                    onClick = {
                        isPop.value = false
                    }
                )
            }
        }
    }
}