package pk.sufiishq.app.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import pk.sufiishq.app.R
import pk.sufiishq.app.data.providers.HomeDataProvider
import pk.sufiishq.app.ui.components.AboutIconButton
import pk.sufiishq.app.ui.components.NavigationHost
import pk.sufiishq.app.ui.components.Player
import pk.sufiishq.app.ui.components.ShareIconButton
import pk.sufiishq.app.utils.rem
import pk.sufiishq.app.viewmodels.KalamViewModel
import pk.sufiishq.app.viewmodels.PlayerViewModel
import pk.sufiishq.app.viewmodels.PlaylistViewModel

@Composable
fun MainView(
    homeDataProvider: HomeDataProvider
) {
    val matColors = MaterialTheme.colors
    val navController = rememberNavController()
    val kalamDataProvider by rem(hiltViewModel<KalamViewModel>())
    val playlistDataProvider by rem(hiltViewModel<PlaylistViewModel>())
    val playerDataProvider by rem(hiltViewModel<PlayerViewModel>())

    Surface(color = matColors.background) {
        Scaffold(
            topBar = {
                TopAppBar(
                    backgroundColor = matColors.background,
                    title = {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.hp_logo),
                                contentDescription = null
                            )
                        }
                    },
                    navigationIcon = {
                        AboutIconButton()
                    },
                    actions = {
                        ShareIconButton()
                    }
                )
            },
            bottomBar = {
                Player(
                    matColors = matColors,
                    playerDataProvider = playerDataProvider,
                    kalamDataProvider,
                    playlistDataProvider
                )
            },
            content = { innerPadding ->
                Box(modifier = Modifier.padding(innerPadding)) {
                    NavigationHost(
                        playerDataProvider,
                        kalamDataProvider,
                        playlistDataProvider,
                        homeDataProvider,
                        navController
                    )
                }
            }
        )
    }
}