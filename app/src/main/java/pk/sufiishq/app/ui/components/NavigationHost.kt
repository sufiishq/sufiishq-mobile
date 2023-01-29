package pk.sufiishq.app.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import pk.sufiishq.app.helpers.ALL_SCREENS
import pk.sufiishq.app.helpers.ScreenType
import pk.sufiishq.aurora.layout.SIColumn

@Composable
fun NavigationHost(
    navController: NavHostController
) {

    SIColumn(modifier = Modifier.fillMaxSize()) {
        NavHost(navController = navController, startDestination = ScreenType.Dashboard.route()) {

            ALL_SCREENS.onEach { screen ->
                composable(screen.route(), screen.arguments(), screen.deepLinks()) {
                    screen.Compose(navController, it)
                }
            }
        }
    }

}