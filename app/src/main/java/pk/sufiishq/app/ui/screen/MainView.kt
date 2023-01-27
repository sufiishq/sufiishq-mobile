package pk.sufiishq.app.ui.screen

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import pk.sufiishq.app.R
import pk.sufiishq.app.ui.components.AppBarOverflowMenu
import pk.sufiishq.app.ui.components.ContentBackground
import pk.sufiishq.app.ui.components.NavigationHost
import pk.sufiishq.app.ui.components.buttons.AboutIconButton
import pk.sufiishq.app.ui.components.player.Player
import pk.sufiishq.aurora.layout.SIScaffold
import pk.sufiishq.aurora.widgets.SITopAppBar

@Composable
fun MainView() {

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
            Player()
        }
    ) {
        ContentBackground {
            NavigationHost(navController)
        }
    }
}