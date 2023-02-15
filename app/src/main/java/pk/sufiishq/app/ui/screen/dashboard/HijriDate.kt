package pk.sufiishq.app.ui.components.widgets

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pk.sufiishq.app.R
import pk.sufiishq.app.data.controller.MainController
import pk.sufiishq.app.utils.optString
import pk.sufiishq.aurora.components.SIImage
import pk.sufiishq.aurora.components.SIText
import pk.sufiishq.aurora.components.SIWidthSpace
import pk.sufiishq.aurora.components.TextSize
import pk.sufiishq.aurora.layout.SIBox
import pk.sufiishq.aurora.layout.SIRow
import pk.sufiishq.aurora.theme.AuroraColor

@Composable
fun HijriDate(
    mainController: MainController
) {

    val hijriDate = mainController.getHijriDate().observeAsState()

    SIBox(
        bgColor = AuroraColor.SecondaryVariant,
        padding = 12,
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
    ) { textColor ->

        hijriDate.value?.apply {
            SIRow {
                SIText(
                    text = day,
                    textColor = textColor,
                    textSize = TextSize.Banner
                )
                SIWidthSpace(value = 12)
                SIText(
                    text = monthAr,
                    textColor = textColor,
                    textSize = TextSize.Banner
                )
            }
            SIText(
                modifier = Modifier.align(Alignment.BottomEnd),
                text = optString(R.string.dynamic_hijri_year, year),
                textColor = textColor,
                textSize = TextSize.Small
            )
        } ?: SIImage(resId = R.drawable.caligraphi)
    }
}