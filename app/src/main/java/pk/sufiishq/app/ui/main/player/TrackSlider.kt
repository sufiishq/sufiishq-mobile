package pk.sufiishq.app.ui.main.player

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.esentsov.PackagePrivate
import pk.sufiishq.app.data.providers.PlayerDataProvider
import pk.sufiishq.app.models.KalamInfo
import pk.sufiishq.aurora.components.SISlider

@PackagePrivate
@Composable
fun TrackSlider(
    playerDataProvider: PlayerDataProvider,
    modifier: Modifier,
    kalamInfo: KalamInfo?
) {
    SISlider(
        modifier = modifier,
        value = kalamInfo?.currentProgress?.toFloat() ?: 0f,
        valueRange = 0f..(kalamInfo?.totalDuration?.toFloat() ?: 0f),
        enabled = kalamInfo?.enableSeekbar ?: false,
        onValueChange = {
            playerDataProvider.updateSeekbarValue(it)
        },
        onValueChangeFinished = {
            playerDataProvider.onSeekbarChanged(kalamInfo?.currentProgress ?: 0)
        }
    )
}