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

package pk.sufiishq.app.ui.screen.events

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import pk.sufiishq.app.feature.events.EventController
import pk.sufiishq.app.feature.events.controller.EventViewModel
import pk.sufiishq.app.utils.ImageRes
import pk.sufiishq.app.utils.TextRes
import pk.sufiishq.app.utils.extention.format
import pk.sufiishq.app.utils.extention.optString
import pk.sufiishq.aurora.components.SIHeightSpace
import pk.sufiishq.aurora.components.SIText
import pk.sufiishq.aurora.components.TextSize
import pk.sufiishq.aurora.layout.SIBox
import pk.sufiishq.aurora.layout.SICard
import pk.sufiishq.aurora.layout.SIColumn
import pk.sufiishq.aurora.layout.SIParallaxLazyColumn
import pk.sufiishq.aurora.layout.SIRow
import pk.sufiishq.aurora.theme.AuroraColor

@Composable
fun EventListScreen(
    eventController: EventController = hiltViewModel<EventViewModel>(),
) {
    SIParallaxLazyColumn(
        leadingIcon = ImageRes.event_full,
        title = optString(TextRes.title_events),
        noItemText = optString(TextRes.msg_no_videos),
        data = eventController.loadAllEvents()
            .collectAsLazyPagingItems(),
    ) { _, item ->

        SIBox(
            modifier = Modifier
                .fillMaxWidth()
                .height(265.dp),
        ) {
            SICard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp)
                    .align(Alignment.TopCenter),
            ) {
                SIBox(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                ) {
                    SIColumn(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) { textColor ->
                        SIHeightSpace(value = 12)
                        SIRow(
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(14.dp, 8.dp),
                            bgColor = AuroraColor.Secondary,
                            radius = 24,
                        ) { dateColor ->
                            SIText(
                                text = item.date.format(),
                                textColor = dateColor,
                                textSize = TextSize.Small,
                            )
                        }
                        SIHeightSpace(value = 24)
                        SIText(
                            modifier = Modifier.padding(20.dp, 0.dp),
                            text = item.title,
                            textColor = textColor,
                            textSize = TextSize.ExtraLarge,
                            fontWeight = FontWeight.Light,
                            textAlign = TextAlign.Center,
                        )
                        SIHeightSpace(value = 8)
                        SIText(
                            text = item.hijriDate,
                            textColor = it,
                            textSize = TextSize.Small,
                        )
                        SIHeightSpace(value = 36)
                    }

                    SIText(
                        modifier = Modifier.align(Alignment.BottomStart),
                        text = optString(TextRes.dynamic_event_days_remaining, item.remainingDays),
                        textColor = it,
                        textSize = TextSize.ExtraSmall,
                    )
                }
            }
            ToggleEventNotificationButton(
                event = item,
                scope = this,
                onToggleNotification = {
                    eventController.toggleNotification(item)
                },
            )
        }
    }
}
