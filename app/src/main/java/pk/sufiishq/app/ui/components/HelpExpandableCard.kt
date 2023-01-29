package pk.sufiishq.app.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pk.sufiishq.app.R
import pk.sufiishq.app.annotations.ExcludeFromJacocoGeneratedReport
import pk.sufiishq.app.core.help.HelpData
import pk.sufiishq.app.models.HelpContent
import pk.sufiishq.app.utils.rem
import pk.sufiishq.aurora.components.SIIcon
import pk.sufiishq.aurora.components.SIText
import pk.sufiishq.aurora.components.SIWidthSpace
import pk.sufiishq.aurora.components.TextSize
import pk.sufiishq.aurora.theme.AuroraColor
import pk.sufiishq.aurora.theme.AuroraDark
import pk.sufiishq.aurora.theme.AuroraLight
import pk.sufiishq.aurora.widgets.SIExpandableCard
import pk.sufiishq.aurora.widgets.SIPopupImage

@Composable
fun HelpExpandableCard(
    expandedIndex: MutableState<Int>,
    index: Int,
    item: HelpContent,
) {

    SIExpandableCard(
        header = { textColor ->
            SIIcon(
                resId = R.drawable.ic_baseline_circle_24,
                tint = AuroraColor.SecondaryVariant,
                modifier = Modifier.size(12.dp)
            )
            SIWidthSpace(value = 12)
            SIText(
                text = item.title,
                textColor = textColor,
                fontWeight = FontWeight.Medium,
                textSize = TextSize.Regular
            )
        },
        isExpanded = expandedIndex.value == index,
        onHeaderClick = { expandedIndex.value = index }
    ) { textColor ->
        item.content.onEach {
            when (it) {
                is HelpData.Paragraph -> SIText(
                    text = it.text,
                    textColor = textColor,
                    textSize = TextSize.Small
                )
                is HelpData.Photo -> SIPopupImage(
                    enablePanning = true,
                    enableScaling = true
                ) {
                    NetworkImage(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        url = it.path,
                    )
                }
                is HelpData.Divider -> Divider(
                    thickness = it.height.dp,
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .testTag("divider_tag")
                )
                is HelpData.Spacer -> Spacer(
                    modifier = Modifier
                        .height(it.height.dp)
                        .testTag("spacer_tag")
                )
            }
        }
    }
}

@ExcludeFromJacocoGeneratedReport
@Preview
@Composable
fun LightExpandableCard() {
    AuroraLight {
        HelpExpandableCard(
            expandedIndex = rem(0),
            index = 0,
            item = HelpContent(
                "The Title", listOf(
                    HelpData.Paragraph(buildAnnotatedString { "This is some text" })
                )
            )
        )
    }
}

@ExcludeFromJacocoGeneratedReport
@Preview
@Composable
fun DarkExpandableCard() {
    AuroraDark {
        HelpExpandableCard(
            expandedIndex = rem(0),
            index = 0,
            item = HelpContent(
                "The Title", listOf(
                    HelpData.Paragraph(buildAnnotatedString { "This is some text" })
                )
            )
        )
    }
}