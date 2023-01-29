package pk.sufiishq.app.ui.components.player

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import pk.sufiishq.app.core.event.events.PlayerEvents
import pk.sufiishq.app.models.KalamInfo
import pk.sufiishq.app.utils.dispatch
import pk.sufiishq.aurora.components.SISlider

@Composable
fun TrackSlider(
    modifier: Modifier,
    kalamInfo: KalamInfo?
) {
    SISlider(
        modifier = modifier,
        value = kalamInfo?.currentProgress?.toFloat() ?: 0f,
        valueRange = 0f..(kalamInfo?.totalDuration?.toFloat() ?: 0f),
        enabled = kalamInfo?.enableSeekbar ?: false,
        onValueChange = { PlayerEvents.UpdateSeekbar(it).dispatch() },
        onValueChangeFinished = {
            PlayerEvents.SeekbarChanged(
                kalamInfo?.currentProgress ?: 0
            ).dispatch()
        }
    )
}