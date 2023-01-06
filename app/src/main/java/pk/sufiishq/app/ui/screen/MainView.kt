package pk.sufiishq.app.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.TopAppBar
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import pk.sufiishq.app.R
import pk.sufiishq.app.data.providers.HomeDataProvider
import pk.sufiishq.app.helpers.GlobalEventHandler
import pk.sufiishq.app.ui.components.AppBarOverflowMenu
import pk.sufiishq.app.ui.components.NavigationHost
import pk.sufiishq.app.ui.components.buttons.AboutIconButton
import pk.sufiishq.app.ui.components.dialogs.DialogHolder
import pk.sufiishq.app.ui.components.player.Player
import pk.sufiishq.app.viewmodels.KalamViewModel
import pk.sufiishq.app.viewmodels.PlayerViewModel
import pk.sufiishq.app.viewmodels.PlaylistViewModel

@Composable
fun MainView(
    homeDataProvider: HomeDataProvider,
    globalEventHandler: GlobalEventHandler
) {
    val matColors = MaterialTheme.colors
    val navController = rememberNavController()
    val kalamDataProvider = hiltViewModel<KalamViewModel>()
    val playlistDataProvider = hiltViewModel<PlaylistViewModel>()
    val playerDataProvider = hiltViewModel<PlayerViewModel>()

    Surface(color = matColors.background) {
        Scaffold(
            scaffoldState = rememberScaffoldState(),
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
                        AppBarOverflowMenu()
                    }
                )
            },
            bottomBar = {
                Player(
                    matColors,
                    playerDataProvider
                )
            },
            content = { innerPadding ->
                Box(modifier = Modifier.padding(innerPadding)) {
                    NavigationHost(
                        kalamDataProvider,
                        playlistDataProvider,
                        homeDataProvider,
                        globalEventHandler,
                        navController
                    )
                }
            }
        )
    }

    DialogHolder(
        playerDataProvider = playerDataProvider,
        playlistDataProvider = playlistDataProvider,
        kalamDataProvider = kalamDataProvider,
        globalEventHandler = globalEventHandler
    )
}