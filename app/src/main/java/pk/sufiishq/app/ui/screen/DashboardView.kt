package pk.sufiishq.app.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import pk.sufiishq.app.R
import pk.sufiishq.app.data.providers.KalamDataProvider
import pk.sufiishq.app.data.providers.PlaylistDataProvider
import pk.sufiishq.app.helpers.Screen
import pk.sufiishq.app.ui.theme.SufiIshqTheme
import pk.sufiishq.app.utils.dummyKalamDataProvider
import pk.sufiishq.app.utils.dummyPlaylistDataProvider
import pk.sufiishq.app.utils.optValue

@Composable
fun DashboardView(
    navController: NavController,
    kalamDataProvider: KalamDataProvider,
    playlistDataProvider: PlaylistDataProvider
) {
    val matColors = MaterialTheme.colors

    val all = stringResource(R.string.all)
    val favorites = stringResource(R.string.favorites)
    val downloads = stringResource(R.string.downloads)
    val playlist = stringResource(R.string.playlist)

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(matColors.secondaryVariant)
    ) {

        val (logo, buttonBox) = createRefs()

        Box(
            modifier = Modifier
                .constrainAs(logo) {
                    start.linkTo(parent.start, 12.dp)
                    top.linkTo(parent.top, 12.dp)
                    end.linkTo(parent.end, 12.dp)
                    bottom.linkTo(buttonBox.top, 12.dp)
                },
            contentAlignment = Alignment.Center
        ) {

            Image(
                modifier = Modifier.heightIn(200.dp, 300.dp),
                colorFilter = ColorFilter.tint(matColors.primary),
                contentScale = ContentScale.Fit,
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null
            )
        }

        Box(
            modifier = Modifier
                .constrainAs(buttonBox) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom, 12.dp)
                }
        ) {
            Row {
                Column(
                    modifier = Modifier
                        .weight(1f)
                ) {


                    TrackButton(
                        title = all,
                        count = kalamDataProvider.countAll().observeAsState().optValue(0),
                        icon = R.drawable.ic_outline_check_circle_24,
                        bgColor = Color(226, 83, 72, 255)
                    ) {
                        navController.navigate(Screen.Tracks.withArgs(Screen.Tracks.ALL, all, "0"))
                    }

                    TrackButton(
                        title = favorites,
                        count = kalamDataProvider.countFavorites().observeAsState().optValue(0),
                        icon = R.drawable.ic_outline_favorite_border_24,
                        bgColor = Color(226, 182, 72, 255)
                    ) {
                        navController.navigate(
                            Screen.Tracks.withArgs(
                                Screen.Tracks.FAVORITES,
                                favorites,
                                "0"
                            )
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                ) {

                    TrackButton(
                        title = downloads,
                        count = kalamDataProvider.countDownloads().observeAsState().optValue(0),
                        icon = R.drawable.ic_outline_cloud_download_24,
                        bgColor = Color(154, 226, 72, 255)
                    ) {
                        navController.navigate(
                            Screen.Tracks.withArgs(
                                Screen.Tracks.DOWNLOADS,
                                downloads,
                                "0"
                            )
                        )
                    }

                    TrackButton(
                        title = playlist,
                        count = playlistDataProvider.countAll().observeAsState().optValue(0),
                        icon = R.drawable.ic_outline_playlist_play_24,
                        bgColor = Color(72, 190, 226, 255)
                    ) {
                        navController.navigate(Screen.Playlist.route)
                    }
                }
            }
        }
    }
}

@Composable
fun TrackButton(title: String, count: Int, icon: Int, bgColor: Color, navigate: () -> Unit) {
    Box(modifier = Modifier
        .padding(6.dp)
        .clip(RoundedCornerShape(5.dp))
        .clickable {
            navigate()
        }
    ) {
        Box(
            modifier = Modifier
                .background(bgColor)
                .padding(20.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {

                Image(
                    modifier = Modifier.padding(10.dp),
                    painter = painterResource(id = icon),
                    contentDescription = null
                )

                Column(
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "$count",
                        fontSize = 30.sp,
                        color = Color.White,
                        fontFamily = FontFamily.Serif
                    )
                    Text(
                        modifier = Modifier.padding(start = 2.dp),
                        text = title,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardPreviewLight() {
    SufiIshqTheme(darkTheme = false) {
        DashboardView(
            navController = rememberNavController(),
            dummyKalamDataProvider(),
            dummyPlaylistDataProvider()
        )
    }
}