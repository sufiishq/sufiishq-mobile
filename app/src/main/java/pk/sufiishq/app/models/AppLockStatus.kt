package pk.sufiishq.app.models

data class AppLockStatus(
    val isBiometricEnable: Boolean,
    val savedPin: String,
    val securityQuestion: SecurityQuestion,
)
