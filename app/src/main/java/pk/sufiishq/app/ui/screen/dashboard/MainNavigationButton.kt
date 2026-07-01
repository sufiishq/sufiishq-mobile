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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import io.github.esentsov.PackagePrivate
import kotlinx.coroutines.launch
import pk.sufiishq.app.utils.ImageRes
import pk.sufiishq.aurora.components.SIIcon
import pk.sufiishq.aurora.layout.SIAuroraSurface
import pk.sufiishq.aurora.layout.SIBox
import pk.sufiishq.aurora.layout.SICard
import pk.sufiishq.aurora.theme.AuroraColor

@PackagePrivate
@Composable
fun MainNavigationButton(
    drawerState: DrawerState,
) {
    val coroutineScope = rememberCoroutineScope()

    SIBox(
        modifier = Modifier
            .size(70.dp)
            .clip(CircleShape),
        bgColor = AuroraColor.Surface,
    ) {
        SIAuroraSurface {
            SICard(
                modifier =
                Modifier
                    .fillMaxSize(0.73f)
                    .clip(CircleShape)
                    .clickable {
                        coroutineScope.launch { drawerState.open() }
                    },
                bgColor = AuroraColor.SecondaryContainer,
            ) { contentColor ->
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    SIIcon(
                        modifier = Modifier.align(Alignment.Center),
                        resId = ImageRes.ic_round_menu_24,
                        tint = contentColor,
                    )
                }
            }
        }
    }
}
