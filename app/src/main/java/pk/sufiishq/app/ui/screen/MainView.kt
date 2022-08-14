package pk.sufiishq.app.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import pk.sufiishq.app.R
import pk.sufiishq.app.core.event.dispatcher.EventDispatcher
import pk.sufiishq.app.data.providers.HomeDataProvider
import pk.sufiishq.app.helpers.GlobalEventHandler
import pk.sufiishq.app.ui.components.NavigationHost
import pk.sufiishq.app.ui.components.buttons.AboutIconButton
import pk.sufiishq.app.ui.components.buttons.ShareIconButton
import pk.sufiishq.app.ui.components.dialogs.*
import pk.sufiishq.app.ui.components.player.Player
import pk.sufiishq.app.utils.rem
import pk.sufiishq.app.viewmodels.KalamViewModel
import pk.sufiishq.app.viewmodels.PlayerViewModel
import pk.sufiishq.app.viewmodels.PlaylistViewModel

@Composable
fun MainView(
    homeDataProvider: HomeDataProvider,
    eventDispatcher: EventDispatcher,
    globalEventHandler: GlobalEventHandler
) {
    val matColors = MaterialTheme.colors
    val navController = rememberNavController()
    val kalamDataProvider by rem(hiltViewModel<KalamViewModel>())
    val playlistDataProvider by rem(hiltViewModel<PlaylistViewModel>())
    val playerDataProvider by rem(hiltViewModel<PlayerViewModel>())

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
                        ShareIconButton(eventDispatcher = eventDispatcher)
                    }
                )
            },
            bottomBar = {
                Player(
                    eventDispatcher,
                    matColors,
                    playerDataProvider
                )
            },
            content = { innerPadding ->
                Box(modifier = Modifier.padding(innerPadding)) {
                    NavigationHost(
                        eventDispatcher,
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

    val kalamDownloadState = playerDataProvider.getKalamDownloadState().observeAsState()
    val playlistState = playlistDataProvider.getAll().observeAsState()
    val showPlaylistDialog = playlistDataProvider.getShowPlaylistDialog().observeAsState()
    val showKalamConfirmDeleteDialog =
        kalamDataProvider.getKalamDeleteConfirmDialog().observeAsState()
    val showKalamSplitManagerDialog =
        kalamDataProvider.getKalamSplitManagerDialog().observeAsState()
    val showKalamRenameDialog = kalamDataProvider.getKalamRenameDialog().observeAsState()
    val showCircularProgressDialog =
        globalEventHandler.getShowCircularProgressDialog().observeAsState()

    // kalam download start dialog
    KalamDownloadStartedDialog(
        eventDispatcher = eventDispatcher,
        kalamDownloadState = kalamDownloadState
    )

    // kalam download in-progress dialog
    KalamDownloadInProgressDialog(
        eventDispatcher = eventDispatcher,
        kalamDownloadState = kalamDownloadState
    )

    // kalam download completed dialog
    KalamDownloadCompletedDialog(
        eventDispatcher = eventDispatcher,
        kalamDownloadState = kalamDownloadState
    )

    // kalam download error dialog
    KalamDownloadErrorDialog(
        eventDispatcher = eventDispatcher,
        kalamDownloadState = kalamDownloadState
    )

    // playlist dialog
    PlaylistDialog(
        eventDispatcher = eventDispatcher,
        playlistState = playlistState,
        showPlaylistDialog = showPlaylistDialog
    )

    // kalam confirm delete dialog
    KalamConfirmDeleteDialog(
        eventDispatcher = eventDispatcher,
        kalamDeleteItem = showKalamConfirmDeleteDialog
    )

    // kalam split dialog
    KalamItemSplitDialog(
        kalamSplitManager = showKalamSplitManagerDialog,
        eventDispatcher = eventDispatcher
    )

    // kalam rename dialog
    KalamRenameDialog(
        kalamState = showKalamRenameDialog,
        eventDispatcher = eventDispatcher,
    )

    // show circular progress indicator dialog
    ShowCircularProgressDialog(showDialog = showCircularProgressDialog)
}