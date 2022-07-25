package pk.sufiishq.app.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pk.sufiishq.app.R
import pk.sufiishq.app.data.providers.PlayerDataProvider
import pk.sufiishq.app.helpers.PlayerState
import pk.sufiishq.app.ui.theme.SufiIshqTheme
import pk.sufiishq.app.utils.dummyPlayerDataProvider

@Composable
fun Player(matColors: Colors, playerDataProvider: PlayerDataProvider) {

    val sliderValue by playerDataProvider.getSeekbarValue().observeAsState()

    Box(Modifier.background(matColors.primaryVariant)) {

        Slider(
            modifier = Modifier
                .fillMaxWidth()
                .height(0.dp),
            colors = SliderDefaults.colors(
                thumbColor = matColors.secondary
            ),
            value = sliderValue!!,
            valueRange = 0f..100f,
            enabled = playerDataProvider.getSeekbarAccess().observeAsState().value!!,
            onValueChange = { playerDataProvider.updateSeekbarValue(it) },
            onValueChangeFinished = {
                playerDataProvider.onSeekbarChanged(sliderValue!!)
            })

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            val playerState = playerDataProvider.getPlayerState().observeAsState().value

            if (playerState == PlayerState.LOADING) {
                CircularProgressIndicator()
            } else {
                Image(
                    modifier = Modifier
                        .size(36.dp)
                        .clickable(indication = null, interactionSource = remember {
                            MutableInteractionSource()
                        }) {
                            playerDataProvider.doPlayOrPause()
                        },
                    painter = painterResource(id = if (playerState == PlayerState.PAUSE || playerState == PlayerState.IDLE) R.drawable.ic_play else R.drawable.ic_pause),
                    colorFilter = ColorFilter.tint(matColors.primary),
                    contentDescription = null
                )
            }

            val activeKalam by playerDataProvider.getActiveKalam().observeAsState()
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.padding(start = 12.dp)
            ) {

                // kalam title
                Text(
                    color = matColors.primary,
                    fontSize = 18.sp,
                    text = activeKalam?.title ?: "",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                // kalam meta info
                Text(
                    color = matColors.primary,
                    fontSize = 14.sp,
                    text = "${activeKalam?.location} ${activeKalam?.year}"
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PlayerPreviewLight() {
    SufiIshqTheme(darkTheme = false) {
        Player(matColors = MaterialTheme.colors, playerDataProvider = dummyPlayerDataProvider())
    }
}

@Preview(showBackground = true)
@Composable
fun PlayerPreviewDark() {
    SufiIshqTheme(darkTheme = true) {
        Player(matColors = MaterialTheme.colors, playerDataProvider = dummyPlayerDataProvider())
    }
}