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

package pk.sufiishq.app.ui.screen.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.esentsov.PackagePrivate
import pk.sufiishq.app.R
import pk.sufiishq.app.feature.admin.controller.AdminController
import pk.sufiishq.app.utils.extention.optString
import pk.sufiishq.app.utils.rem
import pk.sufiishq.aurora.components.SIHeightSpace
import pk.sufiishq.aurora.components.SIText
import pk.sufiishq.aurora.components.SIWidthSpace
import pk.sufiishq.aurora.layout.SIRow
import pk.sufiishq.aurora.theme.AuroraColor

@PackagePrivate
@Composable
fun MaintenanceView(
    adminController: AdminController,
) {
    val active = rem(false)
    val strict = rem(false)
    val maintenance = adminController.getMaintenance().observeAsState().value
    active.value = maintenance?.activeStatus ?: false
    strict.value = maintenance?.strictMode ?: false

    SIHeightSpace(value = 12)
    AdminHeader(optString(R.string.label_maintenance))

    SIHeightSpace(value = 12)
    SIRow(
        modifier = Modifier.fillMaxWidth(),
    ) {
        MaintenanceSwitch(
            label = optString(R.string.label_on),
            status = active,
            scope = this,
            onStatusChanged = adminController::setMaintenanceStatus,
        )
        SIWidthSpace(value = 8)
        MaintenanceSwitch(
            label = optString(R.string.label_strict),
            status = strict,
            scope = this,
            onStatusChanged = adminController::setMaintenanceStrict,
        )
    }
}

@Composable
fun MaintenanceSwitch(
    label: String,
    status: MutableState<Boolean>,
    scope: RowScope,
    onStatusChanged: (status: Boolean) -> Unit,
) {
    with(scope) {
        SIRow(
            modifier = Modifier.weight(1f).padding(12.dp, 6.dp),
            bgColor = AuroraColor.Background,
            radius = 4,
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SIText(
                text = label,
                textColor = it,
            )
            Switch(
                checked = status.value,
                onCheckedChange = { newStatus ->
                    status.value = newStatus
                    onStatusChanged(newStatus)
                },
            )
        }
    }
}
