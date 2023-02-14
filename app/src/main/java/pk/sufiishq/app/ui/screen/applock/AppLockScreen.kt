package pk.sufiishq.app.ui.screen.applock

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.github.esentsov.PackagePrivate
import pk.sufiishq.app.core.applock.AppLockState
import pk.sufiishq.app.data.providers.AppLockController
import pk.sufiishq.app.viewmodels.AppLockViewModel
import pk.sufiishq.aurora.layout.SIColumn

@Composable
fun AppLockScreen(
    scaffoldState: ScaffoldState,
    appLockController: AppLockController = hiltViewModel<AppLockViewModel>()
) {

    val activeState = appLockController.getActiveState().observeAsState()

    SIColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp, top = 12.dp, end = 12.dp, bottom = 18.dp)
    ) {

        when (val state = activeState.value) {
            is AppLockState.Setup -> AppLockSetup(appLockController)
            is AppLockState.NewPinPrompt -> PinPrompt(appLockController)
            is AppLockState.ConfirmNewPinPrompt -> ConfirmPinPrompt(
                appLockController,
                state.previousPin,
                scaffoldState
            )
            is AppLockState.SecurityQuestionPrompt -> SecurityQuestion(
                appLockController,
                scaffoldState,
                state.generatedPin
            )
            is AppLockState.AuthenticateWithPinOrBiometric -> AuthenticateWithPinOrBiometric(
                appLockController,
                state.savedPin
            )
            is AppLockState.Setting -> AppLockSetting(
                appLockController,
                scaffoldState,
                state.message,
                state.biometricEnable
            )
            is AppLockState.ChangePinPrompt -> ChangePinPrompt(appLockController)
            is AppLockState.ConfirmChangePinPrompt -> ConfirmChangePinPrompt(
                appLockController,
                state.previousPin
            )
            is AppLockState.ChangeSecurityQuestion -> ChangeSecurityQuestion(
                appLockController = appLockController,
                scaffoldState = scaffoldState
            )
            is AppLockState.ForgotPin -> ForgotPin(appLockController)
            else -> throw IllegalStateException("${activeState.value} is not a valid state")
        }
    }
}

@PackagePrivate
@Composable
fun ForgotPin(
    appLockController: AppLockController
) {

}