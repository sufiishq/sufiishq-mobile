package pk.sufiishq.app.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import pk.sufiishq.app.R
import pk.sufiishq.app.helpers.Screen
import pk.sufiishq.app.ui.theme.SufiIshqTheme
import pk.sufiishq.app.viewmodels.KalamViewModel
import pk.sufiishq.app.viewmodels.PlaylistViewModel

@Composable
fun DashboardView(navController: NavController) {
    val matColors = MaterialTheme.colors

    val kalamRepository = hiltViewModel<KalamViewModel>().kalamRepository
    val playlistRepository = hiltViewModel<PlaylistViewModel>().playlistRepository

    val all = stringResource(R.string.all)
    val favorites = stringResource(R.string.favorites)
    val downloads = stringResource(R.string.downloads)
    val playlist = stringResource(R.string.playlist)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(matColors.secondaryVariant),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            modifier = Modifier.padding(top = 50.dp),
            colorFilter = ColorFilter.tint(matColors.primary),
            painter = painterResource(id = R.drawable.logo),
            contentDescription = null
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
        ) {
            Row {
                Column(
                    modifier = Modifier
                        .weight(1f)
                ) {


                    TrackButton(
                        title = all,
                        count = kalamRepository.countAll(),
                        icon = R.drawable.ic_outline_check_circle_24,
                        bgColor = Color(226, 83, 72, 255)
                    ) {
                        navController.navigate(Screen.Tracks.withArgs(Screen.Tracks.ALL, all, "0"))
                    }

                    TrackButton(
                        title = favorites,
                        count = kalamRepository.countFavorites(),
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
                        count = kalamRepository.countDownloads(),
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
                        count = playlistRepository.countAll(),
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
        DashboardView(navController = rememberNavController())
    }
}