package pk.sufiishq.app.core.applock

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
    object ForgotPin : AppLockState
    class Setting(val message: String? = null, val biometricEnable: Boolean) : AppLockState
    object ChangeSecurityQuestion : AppLockState
    object ChangePinPrompt : AppLockState
    class ConfirmChangePinPrompt(val previousPin: String) : AppLockState

}
