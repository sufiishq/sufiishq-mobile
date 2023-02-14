package pk.sufiishq.app.core.applock

sealed interface BiometricStatus {
    object Success : BiometricStatus
    object UsePin : BiometricStatus
    object Failed : BiometricStatus
    class Error(errorCode: Int, errString: CharSequence) : BiometricStatus
}