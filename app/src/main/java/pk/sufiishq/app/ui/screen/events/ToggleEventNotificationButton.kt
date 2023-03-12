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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import io.github.esentsov.PackagePrivate
import pk.sufiishq.app.feature.events.model.Event
import pk.sufiishq.app.utils.ImageRes
import pk.sufiishq.aurora.components.SIIcon
import pk.sufiishq.aurora.layout.SIAuroraSurface
import pk.sufiishq.aurora.layout.SIBox
import pk.sufiishq.aurora.layout.SICard
import pk.sufiishq.aurora.theme.AuroraColor

@PackagePrivate
@Composable
fun ToggleEventNotificationButton(
    event: Event,
    scope: BoxScope,
    onToggleNotification: () -> Unit,
) {
    with(scope) {
        SIBox(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 24.dp),
        ) {
            SICard(
                modifier = Modifier
                    .size(70.dp)
                    .clip(CircleShape),
                elevation = 0.dp,
            ) {
                SIAuroraSurface {
                    SICard(
                        modifier =
                        Modifier
                            .fillMaxSize(0.75f)
                            .clip(CircleShape)
                            .clickable {
                                onToggleNotification()
                            },
                        elevation = 0.dp,
                        bgColor = AuroraColor.SecondaryVariant,
                    ) { contentColor ->
                        SIIcon(
                            modifier = Modifier.padding(12.dp),
                            resId = if (event.enableAlert) ImageRes.outline_notifications_active_24 else ImageRes.outline_notifications_off_24,
                            tint = contentColor,
                        )
                    }
                }
            }
        }
    }
}
