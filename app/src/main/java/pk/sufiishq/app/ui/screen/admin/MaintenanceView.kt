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
import pk.sufiishq.app.data.providers.AdminSettingsDataProvider
import pk.sufiishq.app.utils.optValue
import pk.sufiishq.app.utils.rem
import pk.sufiishq.aurora.components.SIHeightSpace
import pk.sufiishq.aurora.components.SIText
import pk.sufiishq.aurora.components.SIWidthSpace
import pk.sufiishq.aurora.layout.SIRow
import pk.sufiishq.aurora.theme.AuroraColor

@PackagePrivate
@Composable
fun MaintenanceView(
    adminSettingsDataProvider: AdminSettingsDataProvider
) {

    val active = rem(false)
    val strict = rem(false)
    active.value = adminSettingsDataProvider.maintenanceStatus().observeAsState().optValue(false)
    strict.value =
        adminSettingsDataProvider.maintenanceStrictMode().observeAsState().optValue(false)

    SIHeightSpace(value = 12)
    AdminHeader("Maintenance")

    SIHeightSpace(value = 12)
    SIRow(
        modifier = Modifier.fillMaxWidth(),
    ) {
        MaintenanceSwitch(
            label = "On",
            status = active,
            scope = this,
            onStatusChanged = adminSettingsDataProvider::setMaintenanceStatus
        )
        SIWidthSpace(value = 8)
        MaintenanceSwitch(
            label = "Strict",
            status = strict,
            scope = this,
            onStatusChanged = adminSettingsDataProvider::setMaintenanceStrict
        )
    }
}

@Composable
fun MaintenanceSwitch(
    label: String,
    status: MutableState<Boolean>,
    scope: RowScope,
    onStatusChanged: (status: Boolean) -> Unit
) {
    with(scope) {
        SIRow(
            modifier = Modifier
                .weight(1f)
                .padding(12.dp, 6.dp),
            bgColor = AuroraColor.Background,
            radius = 4,
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SIText(
                text = label,
                textColor = it
            )
            Switch(
                checked = status.value,
                onCheckedChange = { newStatus ->
                    status.value = newStatus
                    onStatusChanged(newStatus)
                }
            )
        }
    }
}