package pk.sufiishq.app.ui.components.dialogs

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pk.sufiishq.app.R
import pk.sufiishq.app.models.NavigationItem
import pk.sufiishq.aurora.components.SIDivider
import pk.sufiishq.aurora.components.SIText
import pk.sufiishq.aurora.layout.SIBox
import pk.sufiishq.aurora.layout.SIColumn
import pk.sufiishq.aurora.theme.AuroraColor

@Composable
fun MainNavigationDrawer(
    navigationItems: List<NavigationItem>
) {

    SIBox(
        bgColor = AuroraColor.SecondaryVariant,
        padding = 12,
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
    ) {

    }

    SIColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp, 6.dp)
            .verticalScroll(rememberScrollState())
    ) { textColor ->
        navigationItems.forEach {
            SIText(
                modifier = Modifier.fillMaxWidth(),
                text = it.title,
                leadingIcon = it.resId,
                textColor = textColor,
                onClick = {}
            )
        }
        SIDivider(
            color = AuroraColor.PrimaryVariant
        )
        SIText(
            modifier = Modifier.fillMaxWidth(),
            text = "Setting",
            leadingIcon = R.drawable.setting,
            textColor = textColor,
            onClick = {}
        )
    }
}