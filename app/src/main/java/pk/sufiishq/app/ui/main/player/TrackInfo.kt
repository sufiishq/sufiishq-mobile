package pk.sufiishq.app.ui.main.player

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.Dimension
import io.github.esentsov.PackagePrivate
import pk.sufiishq.app.models.KalamInfo
import pk.sufiishq.app.utils.formatDateAs
import pk.sufiishq.app.utils.formatTime
import pk.sufiishq.aurora.components.SIMarqueeText
import pk.sufiishq.aurora.components.SIText
import pk.sufiishq.aurora.components.TextSize
import pk.sufiishq.aurora.layout.SICard
import pk.sufiishq.aurora.layout.SIConstraintLayout
import pk.sufiishq.aurora.theme.AuroraColor

@PackagePrivate
@Composable
fun TrackInfo(
    kalamInfo: KalamInfo?
) {

    SICard(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 30.dp, top = 12.dp, end = 12.dp, bottom = 12.dp),
        elevation = 0.dp,
        bgColor = AuroraColor.Background,
    ) { textColor ->

        SIConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 8.dp, start = 66.dp, end = 10.dp, bottom = 8.dp)
        ) {

            val (titleRef, recordedDateRef, currentProgressRef, totalDurationRef) = createRefs()

            SIMarqueeText(text = (kalamInfo?.kalam?.title ?: ""),
                textColor = textColor,
                backgroundColor = AuroraColor.Background,
                modifier = Modifier.constrainAs(titleRef) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    width = Dimension.fillToConstraints
                })

            SIText(
                modifier = Modifier.constrainAs(recordedDateRef) {
                    top.linkTo(titleRef.top)
                    end.linkTo(parent.end)
                    bottom.linkTo(titleRef.bottom)
                },
                text = (kalamInfo?.kalam?.recordeDate?.formatDateAs() ?: ""),
                textSize = TextSize.Small,
                textColor = textColor
            )

            SIText(
                modifier = Modifier.constrainAs(currentProgressRef) {
                    start.linkTo(titleRef.start)
                    bottom.linkTo(parent.bottom)
                },
                text = kalamInfo?.currentProgress?.formatTime ?: 0.formatTime,
                textColor = textColor,
                textSize = TextSize.ExtraSmall
            )

            SIText(
                modifier = Modifier.constrainAs(totalDurationRef) {
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                },
                text = kalamInfo?.totalDuration?.formatTime ?: 0.formatTime,
                textColor = textColor,
                textSize = TextSize.ExtraSmall
            )
        }
    }

}