package pk.sufiishq.app.ui.screen.photo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pk.sufiishq.app.R
import pk.sufiishq.app.utils.optString
import pk.sufiishq.aurora.components.SIIcon
import pk.sufiishq.aurora.components.SIImage
import pk.sufiishq.aurora.components.SIText
import pk.sufiishq.aurora.components.SIWidthSpace
import pk.sufiishq.aurora.components.TextSize
import pk.sufiishq.aurora.layout.SIBox
import pk.sufiishq.aurora.layout.SIRow
import pk.sufiishq.aurora.theme.AuroraColor
import pk.sufiishq.aurora.widgets.SIZoomable

@Composable
fun PhotoScreen(
    photoId: Int
) {
    SIBox(modifier = Modifier.padding(start = 12.dp, top = 12.dp, end = 12.dp, bottom = 18.dp)) {
        SIZoomable {
            SIImage(
                resId = photoId
            )
        }

        SIRow(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .align(Alignment.BottomCenter),
            bgColor = AuroraColor.SecondaryVariant,
            padding = 6,
            radius = 4,
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) { tint ->
            SIIcon(
                modifier = Modifier.size(30.dp),
                resId = R.drawable.zoom_in,
                tint = tint
            )
            SIWidthSpace(value = 6)
            SIText(
                text = optString(R.string.label_zoom_in_out),
                textColor = tint,
                textSize = TextSize.Small
            )
            SIWidthSpace(value = 30)
            SIIcon(
                modifier = Modifier.size(34.dp),
                resId = R.drawable.drag,
                tint = tint
            )
            SIWidthSpace(value = 2)
            SIText(
                text = optString(R.string.label_drag),
                textColor = tint,
                textSize = TextSize.Small
            )
        }
    }
}