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

package pk.sufiishq.app.ui.screen.dashboard

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import pk.sufiishq.app.feature.events.model.Event
import pk.sufiishq.app.helpers.ScreenType
import pk.sufiishq.app.ui.screen.events.EventListScreen
import pk.sufiishq.app.utils.TextRes
import pk.sufiishq.app.utils.extention.optString
import pk.sufiishq.app.utils.extention.parseRemainingDays
import pk.sufiishq.app.utils.rem
import pk.sufiishq.aurora.components.SIHeightSpace
import pk.sufiishq.aurora.components.SIText
import pk.sufiishq.aurora.components.TextSize
import pk.sufiishq.aurora.layout.SIBox
import pk.sufiishq.aurora.layout.SIColumn
import pk.sufiishq.aurora.layout.SIRow
import pk.sufiishq.aurora.theme.AuroraColor

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun UpcomingEventTicker(
    modifier: Modifier,
    upcomingEvents: List<Event>?,
    navController: NavController
) {
    SIBox(
        modifier = modifier,
    ) {
        if (upcomingEvents?.isNotEmpty() == true) {
            val target = produceState(initialValue = upcomingEvents.first()) {
                upcomingEvents.run {
                    if (size >= 2) {
                        var index = 1
                        while (true) {
                            delay(5000)
                            value = get(index)
                            if (++index == size) {
                                index = 0
                            }
                        }
                    }
                }
            }

            val density = LocalDensity.current
            val animatedHeight = rem(0)
            AnimatedContent(

                targetState = target.value,
                transitionSpec = {
                    slideInVertically(
                        animationSpec = tween(1000, easing = {
                            OvershootInterpolator().getInterpolation(it)
                        }),
                        initialOffsetY = {
                            -animatedHeight.value
                        },
                    ) with slideOutVertically(
                        animationSpec = tween(1000, easing = {
                            OvershootInterpolator().getInterpolation(it)
                        }),
                        targetOffsetY = {
                            animatedHeight.value
                        },
                    )
                },
            ) { event ->
                SIBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp, 0.dp)
                        .clickable {
                            navController.navigate(ScreenType.EventList.buildRoute())
                        }
                        .onGloballyPositioned { layoutCoordinate ->
                            animatedHeight.value =
                                layoutCoordinate.size.height + with(density) { 20.dp.roundToPx() }
                        },
                ) {
                    SIRow(
                        modifier = Modifier
                            .padding(12.dp, 8.dp),
                        bgColor = AuroraColor.SecondaryVariant,
                        radius = 4,
                    ) { onColor ->
                        SIColumn(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            SIText(
                                text = event.title,
                                textColor = onColor,
                                textSize = TextSize.Small,
                                textAlign = TextAlign.Center,
                            )
                            SIHeightSpace(value = 4)
                            SIText(
                                text = event.parseRemainingDays(),
                                textColor = onColor,
                                textSize = TextSize.Small,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}
