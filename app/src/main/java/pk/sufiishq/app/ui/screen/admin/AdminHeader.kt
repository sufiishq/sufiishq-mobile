package pk.sufiishq.app.ui.screen.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.esentsov.PackagePrivate
import pk.sufiishq.aurora.components.SIText
import pk.sufiishq.aurora.layout.SICard
import pk.sufiishq.aurora.layout.SIRow

@PackagePrivate
@Composable
fun AdminHeader(
    heading: String
) {

    SICard(
        modifier = Modifier.fillMaxWidth(),
    ) {
        SIRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SIText(
                text = heading,
                textColor = it,
                fontWeight = FontWeight.Bold
            )
        }
    }
}