package pk.sufiishq.app.ui.screen.dashboard

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import pk.sufiishq.app.feature.events.model.Event
import pk.sufiishq.app.utils.TextRes
import pk.sufiishq.app.utils.extention.optString
import pk.sufiishq.aurora.components.SIText
import pk.sufiishq.aurora.components.TextSize
import pk.sufiishq.aurora.layout.SIBox
import pk.sufiishq.aurora.layout.SIRow
import pk.sufiishq.aurora.theme.AuroraColor

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun UpcomingEventTicker(
    modifier: Modifier,
    upcomingEvents: List<Event>?
) {

    SIBox(
        modifier = modifier
    ) {

        if (upcomingEvents?.isNotEmpty() == true) {

            val target = produceState(initialValue = upcomingEvents.first()) {
                upcomingEvents.run {
                    if (size >= 2) {
                        var index = 1
                        while (true) {
                            delay(3000)
                            value = get(index)
                            if (++index == size) {
                                index = 0
                            }
                        }
                    }
                }
            }

            val density = LocalDensity.current
            AnimatedContent(

                targetState = target.value,
                transitionSpec = {
                    slideInVertically(
                        animationSpec = tween(1000),
                        initialOffsetY = {
                            with(density) { -40.dp.roundToPx() }
                        }
                    ) with slideOutVertically(
                        animationSpec = tween(1000),
                        targetOffsetY = {
                            with(density) { 40.dp.roundToPx() }
                        })
                }
            ) { event ->
                SIBox(modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp, 0.dp)) {
                    SIRow(
                        modifier = Modifier
                            .padding(12.dp, 8.dp),
                        bgColor = AuroraColor.SecondaryVariant,
                        radius = 20
                    ) {
                        SIText(
                            text = "${event.title} - ${
                                if (event.remainingDays == 0) {
                                    optString(TextRes.label_now)
                                } else {
                                    optString(
                                        TextRes.dynamic_event_days_remaining,
                                        event.remainingDays
                                    )
                                }
                            }",
                            textColor = it,
                            textSize = TextSize.Small,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}