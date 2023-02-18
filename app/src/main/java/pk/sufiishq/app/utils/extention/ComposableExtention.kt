/*
 * Copyright 2022-2023 SufiIshq
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pk.sufiishq.app.utils

import androidx.annotation.StringRes
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
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

/** Returns whether the lazy list is currently scrolling up. */
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
            }
                .also {
                    previousIndex.value = firstVisibleItemIndex
                    previousScrollOffset.value = firstVisibleItemScrollOffset
                }
        }
    }
        .value
}

@Composable
fun optString(@StringRes resId: Int, vararg args: Any?): String {
    val context = LocalContext.current
    return context.getString(resId).format(*args)
}

fun Modifier.shake(enabled: Boolean) =
    composed(
        factory = {
            val scale =
                rememberInfiniteTransition()
                    .animateFloat(
                        initialValue = -20f,
                        targetValue = 20f,
                        animationSpec =
                        infiniteRepeatable(
                            animation = tween(durationMillis = 300, easing = LinearEasing),
                            repeatMode = RepeatMode.Reverse,
                        ),
                    )

            Modifier.graphicsLayer { rotationZ = if (enabled) scale.value else 1f }
        },
        inspectorInfo =
        debugInspectorInfo {
            name = "shake"
            properties["enabled"] = enabled
        },
    )
