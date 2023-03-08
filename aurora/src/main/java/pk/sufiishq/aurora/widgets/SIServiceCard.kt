package pk.sufiishq.aurora.widgets


import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import pk.sufiishq.aurora.components.SIHeightSpace
import pk.sufiishq.aurora.components.SIImage
import pk.sufiishq.aurora.components.SIText
import pk.sufiishq.aurora.components.SIWidthSpace
import pk.sufiishq.aurora.components.TextSize
import pk.sufiishq.aurora.layout.SIBox
import pk.sufiishq.aurora.layout.SIColumn
import pk.sufiishq.aurora.layout.SIRow
import pk.sufiishq.aurora.theme.AuroraColor


@Composable
fun SIServiceCard(
    modifier: Modifier = Modifier,
    @DrawableRes infoDrawableId: Int,
    title: String,
    detail: String,
    onCardClick: (() -> Unit)? = null,
    content: @Composable (BoxScope.(fgColor: AuroraColor) -> Unit)? = null,
) {

    val clickableModifier: Modifier = onCardClick?.let {
        Modifier.clickable {
            it.invoke()
        }
    } ?: Modifier

    SIRow(
        modifier = modifier
            .fillMaxWidth()
            .then(clickableModifier),
        bgColor = AuroraColor.Background,
        radius = 4,
    ) { textColor ->
        SIRow(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            SIImage(resId = infoDrawableId)
            SIWidthSpace(value = 12)
            SIColumn(modifier = Modifier.fillMaxWidth()) {
                SIText(text = title, textColor = textColor, fontWeight = FontWeight.Bold)
                SIHeightSpace(value = 6)
                SIText(
                    text = detail,
                    textColor = textColor,
                    textSize = TextSize.Small,
                )
                content?.apply {
                    SIHeightSpace(value = 6)
                    SIBox(modifier = Modifier.fillMaxWidth()) { content(this, it) }
                }
            }
        }
    }
}