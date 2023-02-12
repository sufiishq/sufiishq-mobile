package pk.sufiishq.app.utils

import androidx.annotation.StringRes
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.debugInspectorInfo

/**
 * Returns whether the lazy list is currently scrolling up.
 */
@Composable
fun LazyListState.isScrollingUp(): Boolean {
    val previousIndex = remember(this) { mutableStateOf(firstVisibleItemIndex) }
    val previousScrollOffset = remember(this) { mutableStateOf(firstVisibleItemScrollOffset) }
    return remember(this) {
        derivedStateOf {
            if (previousIndex.value != firstVisibleItemIndex) {
                previousIndex.value > firstVisibleItemIndex
            } else {
                previousScrollOffset.value >= firstVisibleItemScrollOffset
            }.also {
                previousIndex.value = firstVisibleItemIndex
                previousScrollOffset.value = firstVisibleItemScrollOffset
            }
        }
    }.value
}

@Composable
fun optString(@StringRes resId: Int, vararg args: Any?): String {
    val context = LocalContext.current
    return context.getString(resId).format(*args)
}


fun Modifier.shake(enabled: Boolean) = composed(

    factory = {

        val scale = rememberInfiniteTransition().animateFloat(
            initialValue = -20f,
            targetValue = 20f,
            animationSpec = infiniteRepeatable(

                animation = tween(durationMillis = 300, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            )
        )

        Modifier.graphicsLayer {
            rotationZ = if (enabled) scale.value else 1f
        }
    },
    inspectorInfo = debugInspectorInfo {
        name = "shake"
        properties["enabled"] = enabled
    }
)