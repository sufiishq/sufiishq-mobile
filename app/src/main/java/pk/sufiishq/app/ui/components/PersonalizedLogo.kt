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

package pk.sufiishq.app.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import pk.sufiishq.app.feature.personalize.controller.PersonalizeController
import pk.sufiishq.app.utils.ImageRes
import pk.sufiishq.app.utils.extention.appendPath
import pk.sufiishq.app.utils.rem
import pk.sufiishq.aurora.components.SIImage
import pk.sufiishq.aurora.layout.SIBox
import kotlin.math.abs

@Composable
fun PersonalizedLogo(
    modifier: Modifier = Modifier,
    personalizeController: PersonalizeController,
) {
    val personalize = personalizeController.get().observeAsState()
    val context = LocalContext.current

    val infiniteTransition = rememberInfiniteTransition()
    val angle by
        infiniteTransition.animateFloat(
            initialValue = 0F,
            targetValue = 360F,
            animationSpec =
            infiniteRepeatable(
                animation = tween(20000, easing = LinearEasing),
            ),
        )

    personalize.value?.let { p ->

        val width = rem(1.dp)
        val density = LocalDensity.current
        val currentState = rem(true)
        val showFrontFace = remember {
            derivedStateOf { currentState.value }
        }

        SIBox(
            modifier = modifier.padding(24.dp),
        ) {
            SIBox(
                modifier = Modifier
                    .graphicsLayer {
                        rotationY = angle
                        currentState.value = angle <= 90 || angle > 270
                        scaleX = 1f - abs(90f - ((90f + angle) % 180f)) * 0.005f
                    }
                    .fillMaxHeight()
                    .width(width.value)
                    .clip(CircleShape)
                    .onGloballyPositioned { layoutCoordinate ->
                        width.value = with(density) { layoutCoordinate.size.height.toDp() }
                    },
                padding = 1,
            ) {
                SIBox(
                    modifier = Modifier
                        .wrapContentSize()
                        .clip(CircleShape),
                ) {
                    if (showFrontFace.value) {
                        SIImage(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(width.value),
                            painter = rememberAsyncImagePainter(context.filesDir.appendPath(p.frontPath)),
                            contentScale = ContentScale.Crop,
                        )
                    } else {
                        SIImage(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(width.value)
                                .graphicsLayer {
                                    rotationY = 180f
                                },
                            painter = rememberAsyncImagePainter(context.filesDir.appendPath(p.backPath)),
                            contentScale = ContentScale.Crop,
                        )
                    }

                    SIImage(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(width.value),
                        resId = ImageRes.logo_ring,
                    )
                }
            }
        }
    } ?: run {
        SIImage(
            modifier = modifier
                .testTag("logo")
                .graphicsLayer { rotationY = angle },
            resId = ImageRes.logo,
        )
    }
}
