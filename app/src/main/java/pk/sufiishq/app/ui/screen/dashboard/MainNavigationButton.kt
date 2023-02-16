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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import io.github.esentsov.PackagePrivate
import kotlinx.coroutines.launch
import pk.sufiishq.app.R
import pk.sufiishq.app.ui.components.ContentBackground
import pk.sufiishq.aurora.components.SIIcon
import pk.sufiishq.aurora.layout.SICard
import pk.sufiishq.aurora.theme.AuroraColor

@PackagePrivate
@Composable
fun MainNavigationButton(
    scaffoldState: ScaffoldState,
) {
    val coroutineScope = rememberCoroutineScope()

    SICard(
        modifier = Modifier.size(80.dp).clip(CircleShape),
    ) {
        ContentBackground {
            SICard(
                modifier =
                Modifier.fillMaxSize(0.73f).clip(CircleShape).clickable {
                    coroutineScope.launch { scaffoldState.drawerState.open() }
                },
                bgColor = AuroraColor.SecondaryVariant,
            ) { contentColor ->
                SIIcon(
                    modifier = Modifier.padding(12.dp),
                    resId = R.drawable.ic_round_menu_24,
                    tint = contentColor,
                )
            }
        }
    }
}
