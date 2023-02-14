package pk.sufiishq.app.ui.screen.applock

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.github.esentsov.PackagePrivate
import kotlinx.coroutines.launch
import pk.sufiishq.app.core.applock.AppLockState
import pk.sufiishq.app.data.providers.AppLockController
import pk.sufiishq.app.models.SecurityQuestion
import pk.sufiishq.app.ui.components.OutlinedTextField
import pk.sufiishq.app.utils.rem
import pk.sufiishq.app.viewmodels.AppLockViewModel
import pk.sufiishq.aurora.components.SIButton
import pk.sufiishq.aurora.components.SIHeightSpace
import pk.sufiishq.aurora.components.SIText
import pk.sufiishq.aurora.layout.SIBox
import pk.sufiishq.aurora.layout.SIColumn
import pk.sufiishq.aurora.layout.SIRow
import pk.sufiishq.aurora.theme.AuroraColor

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
            is AppLockState.ChangePinPrompt -> ChangePinPrompt(
                appLockController,
                state.cameFromForgotPin
            )
            is AppLockState.ConfirmChangePinPrompt -> ConfirmChangePinPrompt(
                appLockController,
                state.previousPin,
                state.cameFromForgotPin
            )
            is AppLockState.ChangeSecurityQuestion -> ChangeSecurityQuestion(
                appLockController = appLockController,
                scaffoldState = scaffoldState
            )
            is AppLockState.ForgotPin -> ForgotPin(
                appLockController,
                scaffoldState,
                state.securityQuestion
            )
            else -> throw IllegalStateException("${activeState.value} is not a valid state")
        }
    }
}