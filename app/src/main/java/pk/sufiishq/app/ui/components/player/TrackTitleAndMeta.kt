package pk.sufiishq.app.ui.components.player

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayoutScope
import androidx.constraintlayout.compose.Dimension
import pk.sufiishq.app.models.KalamInfo
import pk.sufiishq.app.utils.formatDateAs
import pk.sufiishq.aurora.components.SIMarqueeText
import pk.sufiishq.aurora.components.SIText
import pk.sufiishq.aurora.components.TextSize
import pk.sufiishq.aurora.theme.AuroraColor

@Composable
fun TrackTitleAndMeta(
    scope: ConstraintLayoutScope, onColor: AuroraColor, kalamInfo: KalamInfo?
) {

    with(scope) {
        val (titleRef, metaInfoRef) = createRefs()

        SIMarqueeText(text = (kalamInfo?.kalam?.title ?: ""),
            textColor = onColor,
            backgroundColor = AuroraColor.Background,
            modifier = Modifier.constrainAs(titleRef) {
                start.linkTo(parent.start)
                top.linkTo(parent.top)
                end.linkTo(metaInfoRef.start, 12.dp)
                width = Dimension.fillToConstraints
            })

        SIText(
            modifier = Modifier.constrainAs(metaInfoRef) {
                top.linkTo(titleRef.top)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            },
            text = (kalamInfo?.kalam?.location
                ?: "") + (kalamInfo?.kalam?.recordeDate?.formatDateAs(prefix = " - ") ?: ""),
            textSize = TextSize.Small,
            textColor = onColor
        )
    }

}