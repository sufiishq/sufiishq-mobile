package pk.sufiishq.app.ui.main

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import pk.sufiishq.app.R
import pk.sufiishq.app.core.applock.AppLockManager
import pk.sufiishq.app.core.firebase.MaintenanceManager
import pk.sufiishq.app.core.player.AudioPlayer
import pk.sufiishq.app.ui.components.ContentBackground
import pk.sufiishq.app.ui.main.player.PlayerView
import pk.sufiishq.app.ui.main.topbar.AboutIconButton
import pk.sufiishq.app.ui.main.topbar.AppBarOverflowMenu
import pk.sufiishq.aurora.layout.SIScaffold
import pk.sufiishq.aurora.widgets.SITopAppBar

@Composable
fun MainHostView(
    maintenanceManager: MaintenanceManager,
    appLockManager: AppLockManager,
    audioPlayer: AudioPlayer
) {

    val navController = rememberNavController()

    SIScaffold(
        topBar = {
            SITopAppBar(
                navigationIcon = { fgColor ->
                    AboutIconButton(fgColor)
                },
                actions = { fgColor ->
                    AppBarOverflowMenu(navController, fgColor)
                },
                centerDrawable = R.drawable.hp_logo
            )
        },
        bottomBar = {
            PlayerView()
        }
    ) {
        ContentBackground {
            NavigationHost(navController)
        }
    }

    AppLockAndMaintenance(maintenanceManager, appLockManager, audioPlayer)
}
