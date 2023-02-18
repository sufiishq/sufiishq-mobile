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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import io.github.esentsov.PackagePrivate
import kotlinx.coroutines.launch
import pk.sufiishq.app.R
import pk.sufiishq.app.feature.admin.model.Highlight
import pk.sufiishq.app.feature.app.controller.DashboardController
import pk.sufiishq.app.utils.extention.shake
import pk.sufiishq.aurora.components.SIIcon
import pk.sufiishq.aurora.layout.SICard
import pk.sufiishq.aurora.theme.AuroraColor

@PackagePrivate
@Composable
fun HighlightAvailableButton(
    modifier: Modifier = Modifier,
    highlightDialogControl: MutableState<Highlight?>,
    dashboardController: DashboardController,
) {
    val coroutineScope = rememberCoroutineScope()

    dashboardController.getHighlightAvailable().observeAsState().value?.apply {
        SICard(
            modifier =
            modifier
                .clip(CircleShape)
                .clickable {
                    coroutineScope.launch { highlightDialogControl.value = this@apply }
                },
            bgColor = AuroraColor.SecondaryVariant,
        ) { contentColor ->
            SIIcon(
                modifier = Modifier
                    .padding(12.dp)
                    .shake(true),
                resId = R.drawable.round_notifications_active_24,
                tint = contentColor,
            )
        }

        LaunchedEffect(Unit) {
            if (AutoShowDialog.AUTO_SHOW) {
                highlightDialogControl.value = this@apply
                AutoShowDialog.AUTO_SHOW = false
            }
        }
    }
}

object AutoShowDialog {
    var AUTO_SHOW = true
}
