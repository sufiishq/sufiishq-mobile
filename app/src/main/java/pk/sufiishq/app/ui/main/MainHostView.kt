package pk.sufiishq.app.ui.main

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import pk.sufiishq.app.R
import pk.sufiishq.app.ui.components.ContentBackground
import pk.sufiishq.app.ui.main.player.Player
import pk.sufiishq.app.ui.main.topbar.AboutIconButton
import pk.sufiishq.app.ui.main.topbar.AppBarOverflowMenu
import pk.sufiishq.aurora.layout.SIScaffold
import pk.sufiishq.aurora.widgets.SITopAppBar

@Composable
fun MainHostView() {

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