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
import pk.sufiishq.app.data.providers.DashboardDataProvider
import pk.sufiishq.app.models.Highlight
import pk.sufiishq.app.utils.shake
import pk.sufiishq.aurora.components.SIIcon
import pk.sufiishq.aurora.layout.SICard
import pk.sufiishq.aurora.theme.AuroraColor

@PackagePrivate
@Composable
fun HighlightAvailableButton(
    modifier: Modifier = Modifier,
    highlightDialogControl: MutableState<Highlight?>,
    dashboardDataProvider: DashboardDataProvider
) {

    val coroutineScope = rememberCoroutineScope()

    dashboardDataProvider.getHighlightAvailable()
        .observeAsState()
        .value
        ?.apply {

            SICard(
                modifier = modifier
                    .clip(CircleShape)
                    .clickable {
                        coroutineScope.launch {
                            highlightDialogControl.value = this@apply
                        }
                    },
                bgColor = AuroraColor.SecondaryVariant
            ) { contentColor ->
                SIIcon(
                    modifier = Modifier.padding(12.dp).shake(true),
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