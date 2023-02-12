package pk.sufiishq.app.ui.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import pk.sufiishq.app.helpers.ALL_SCREENS
import pk.sufiishq.app.helpers.ScreenType
import pk.sufiishq.aurora.layout.SIColumn
import pk.sufiishq.aurora.layout.SIScaffold
import pk.sufiishq.aurora.theme.AuroraColor
import pk.sufiishq.aurora.theme.getForegroundColor

@Composable
fun NavigationHost(
    navController: NavHostController
) {

    val scaffoldState = rememberScaffoldState()

    SIScaffold(scaffoldState = scaffoldState, snackbarHost = {
        SnackbarHost(it) { data ->
            Snackbar(
                snackbarData = data,
                backgroundColor = AuroraColor.SecondaryVariant.color(),
                contentColor = AuroraColor.SecondaryVariant.getForegroundColor().color()
            )
        }
    }) {
        SIColumn(modifier = Modifier.fillMaxSize()) {
            NavHost(
                navController = navController, startDestination = ScreenType.Dashboard.buildRoute()
            ) {

                ALL_SCREENS.onEach { screen ->
                    composable(screen.buildRoute(), screen.arguments(), screen.deepLinks()) {
                        screen.Compose(navController, it, scaffoldState)
                    }
                }
            }
        }
    }

}