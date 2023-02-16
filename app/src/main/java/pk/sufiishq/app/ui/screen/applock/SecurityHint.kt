package pk.sufiishq.app.ui.screen.applock

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.esentsov.PackagePrivate
import pk.sufiishq.app.R
import pk.sufiishq.app.utils.optString
import pk.sufiishq.aurora.components.SIHeightSpace
import pk.sufiishq.aurora.components.SIImage
import pk.sufiishq.aurora.components.SIText
import pk.sufiishq.aurora.components.SIWidthSpace
import pk.sufiishq.aurora.components.TextSize
import pk.sufiishq.aurora.layout.SIColumn
import pk.sufiishq.aurora.layout.SIRow
import pk.sufiishq.aurora.theme.AuroraColor

@PackagePrivate
@Composable
fun SecurityHint(
    modifier: Modifier = Modifier
) {
    SIRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        bgColor = AuroraColor.Background,
        radius = 4
    ) { textColor ->
        SIImage(resId = R.drawable.guarantee)
        SIWidthSpace(value = 12)
        SIColumn(modifier = Modifier.fillMaxWidth()) {
            SIText(
                text = optString(R.string.title_hint),
                textColor = textColor,
                fontWeight = FontWeight.Bold
            )
            SIHeightSpace(value = 8)
            SIText(
                text = optString(R.string.detail_hint),
                textColor = textColor,
                textSize = TextSize.Small
            )
        }
    }
}