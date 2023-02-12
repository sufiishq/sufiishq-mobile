package pk.sufiishq.app.ui.screen.dashboard

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import io.github.esentsov.PackagePrivate
import pk.sufiishq.aurora.components.SIIcon
import pk.sufiishq.aurora.components.SIText
import pk.sufiishq.aurora.components.TextSize
import pk.sufiishq.aurora.layout.SIBox
import pk.sufiishq.aurora.layout.SIColumn
import pk.sufiishq.aurora.layout.SIRow
import pk.sufiishq.aurora.theme.AuroraColor

@PackagePrivate
@SuppressLint("ModifierParameter")
@Composable
fun DashboardButton(
    title: String,
    count: Int,
    icon: Int,
    paddingModifier: Modifier,
    navigate: () -> Unit
) {

    SIBox(
        modifier = paddingModifier
            .clip(RoundedCornerShape(5.dp))
            .clickable {
                navigate()
            },
        bgColor = AuroraColor.Background
    ) { textColor ->
        SIBox(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
                .height(60.dp),
            contentAlignment = Alignment.Center
        ) {

            SIRow(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {

                SIIcon(
                    modifier = Modifier.padding(10.dp),
                    resId = icon,
                )

                SIColumn(
                    verticalArrangement = Arrangement.Center
                ) {
                    SIText(
                        text = "$count",
                        textSize = TextSize.ExtraLarge,
                        textColor = textColor
                    )
                    SIText(
                        modifier = Modifier.padding(start = 2.dp),
                        text = title,
                        textSize = TextSize.Small,
                        textColor = textColor,
                    )
                }
            }
        }
    }
}