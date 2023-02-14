package pk.sufiishq.app.ui.main.player

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import pk.sufiishq.app.annotations.ExcludeFromJacocoGeneratedReport
import pk.sufiishq.app.data.providers.PlayerDataProvider
import pk.sufiishq.app.ui.components.ContentBackground
import pk.sufiishq.app.utils.fakePlayerDataProvider
import pk.sufiishq.app.viewmodels.PlayerViewModel
import pk.sufiishq.aurora.layout.SIBox
import pk.sufiishq.aurora.theme.AuroraDark
import pk.sufiishq.aurora.theme.AuroraLight

@Composable
fun PlayerView(
    playerDataProvider: PlayerDataProvider = hiltViewModel<PlayerViewModel>(),
) {

    val kalamInfo = playerDataProvider.getKalamInfo().observeAsState()

    ContentBackground(modifier = Modifier.height(90.dp)) {
        SIBox(modifier = Modifier.fillMaxSize()) {
            SIBox(modifier = Modifier.padding(top = 6.dp)) {

                TrackInfo(kalamInfo = kalamInfo.value)

                PlayPauseButton(
                    playerDataProvider = playerDataProvider,
                    kalamInfo = kalamInfo,
                    boxScope = this
                )
            }

            TrackSlider(
                playerDataProvider = playerDataProvider,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .height(0.dp),
                kalamInfo = kalamInfo.value
            )
        }
    }

}

@ExcludeFromJacocoGeneratedReport
@Preview(showBackground = true)
@Composable
fun PlayerPreviewLight() {
    AuroraLight {
        PlayerView(
            playerDataProvider = fakePlayerDataProvider()
        )
    }
}

@ExcludeFromJacocoGeneratedReport
@Preview(showBackground = true)
@Composable
fun PlayerPreviewDark() {
    AuroraDark {
        PlayerView(
            playerDataProvider = fakePlayerDataProvider()
        )
    }
}