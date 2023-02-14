package pk.sufiishq.app.ui.screen.applock

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.esentsov.PackagePrivate
import pk.sufiishq.app.R
import pk.sufiishq.aurora.components.SIButton
import pk.sufiishq.aurora.components.SIImage
import pk.sufiishq.aurora.components.SIText
import pk.sufiishq.aurora.components.SIWidthSpace
import pk.sufiishq.aurora.layout.SIRow
import pk.sufiishq.aurora.theme.AuroraColor

@PackagePrivate
@Composable
fun AppLockHeader(
    modifier: Modifier = Modifier,
    title: String = "App Lock",
    buttonTitle: String = "Cancel",
    onButtonClick: () -> Unit
) {

    SIRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp, 6.dp),
        bgColor = AuroraColor.Background,
        radius = 4,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SIRow(
            verticalAlignment = Alignment.CenterVertically
        ) {
            SIImage(
                modifier = Modifier.height(30.dp),
                resId = R.drawable.shield
            )
            SIWidthSpace(value = 8)
            SIText(
                text = title,
                textColor = it,
            )
        }
        SIButton(text = buttonTitle, onClick = onButtonClick)
    }
}