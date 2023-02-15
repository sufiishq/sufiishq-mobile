package pk.sufiishq.app.ui.screen.admin

import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.asFlow
import kotlinx.coroutines.flow.collectLatest
import pk.sufiishq.app.core.firebase.AuthState
import pk.sufiishq.app.data.controller.AdminController
import pk.sufiishq.app.ui.components.dialogs.ShowIndicatorDialog
import pk.sufiishq.app.utils.optValue
import pk.sufiishq.app.viewmodels.AdminSettingsViewModel

@Composable
fun AdminSettingsScreen(
    scaffoldState: ScaffoldState,
    adminController: AdminController = hiltViewModel<AdminSettingsViewModel>()
) {

    val authState = adminController.checkAuthentication().observeAsState()
        .optValue(AuthState.Cancelled())
    val showLoader = adminController.showLoader().observeAsState().optValue(false)

    when (authState) {
        is AuthState.Success -> {
            HighlightAndMaintenanceForm(
                adminController = adminController,
                isDeveloper = authState.userIsDeveloper
            )
        }
        is AuthState.Error, is AuthState.Cancelled, is AuthState.InProgress -> {
            if (authState is AuthState.Error || authState is AuthState.Cancelled) {
                adminController.showSnackbar(authState.message)
            }
            AdminSignIn(
                adminController = adminController
            )
        }
    }

    if (showLoader) {
        ShowIndicatorDialog()
    }

    LaunchedEffect(Unit) {
        adminController.showSnackbar().asFlow().collectLatest {
            it?.let {
                scaffoldState.snackbarHostState.showSnackbar(it)
            }
        }
    }
}
