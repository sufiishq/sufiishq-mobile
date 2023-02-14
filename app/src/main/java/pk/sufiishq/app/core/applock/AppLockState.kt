package pk.sufiishq.app.core.applock

import pk.sufiishq.app.models.SecurityQuestion

sealed interface AppLockState {

    // -------------------------------------------------------------------- //
    // when user has no pin / biometric setup
    // -------------------------------------------------------------------- //

    object Setup : AppLockState
    object NewPinPrompt : AppLockState
    class ConfirmNewPinPrompt(val previousPin: String) : AppLockState
    class SecurityQuestionPrompt(val generatedPin: String) : AppLockState

    // -------------------------------------------------------------------- //
    // when user has already setup pin / biometric
    // -------------------------------------------------------------------- //

    class AuthenticateWithPinOrBiometric(val savedPin: String) : AppLockState
    class ForgotPin(val securityQuestion: SecurityQuestion) : AppLockState
    class Setting(val message: String? = null, val biometricEnable: Boolean) : AppLockState
    object ChangeSecurityQuestion : AppLockState
    class ChangePinPrompt(val cameFromForgotPin: Boolean = false) : AppLockState
    class ConfirmChangePinPrompt(val previousPin: String, val cameFromForgotPin: Boolean = false) : AppLockState

}
