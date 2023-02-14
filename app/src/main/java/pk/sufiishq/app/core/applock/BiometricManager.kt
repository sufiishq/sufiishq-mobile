package pk.sufiishq.app.core.applock

import android.content.Context
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import pk.sufiishq.app.R
import pk.sufiishq.app.core.applock.BiometricStatus
import pk.sufiishq.app.utils.getString
import pk.sufiishq.app.utils.hasBiometricCapability
import timber.log.Timber

class BiometricManager @Inject constructor(
    @ApplicationContext private val appContext: Context
) {

    private var biometricVerified = false

    fun userHasBiometricCapability(): Boolean {
        return appContext.hasBiometricCapability()
    }

    fun prompt(
        activity: FragmentActivity,
        allowUsePin: Boolean = true,
        callback: (biometricStatus: BiometricStatus) -> Unit
    ) {

        val biometricCallback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Timber.d("$errorCode :: $errString")
                biometricVerified = false
                if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                    callback(BiometricStatus.UsePin)
                } else {
                    callback(BiometricStatus.Error(errorCode, errString))
                }
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                biometricVerified = false
                Timber.d("Authentication failed for an unknown reason")
                callback(BiometricStatus.Failed)
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                Timber.d("Authentication was successful")
                biometricVerified = true
                callback(BiometricStatus.Success)
            }
        }

        val executor = ContextCompat.getMainExecutor(activity)
        val biometricPrompt = BiometricPrompt(activity, executor, biometricCallback)
        biometricPrompt.authenticate(createPromptInfo(allowUsePin))
    }

    fun isBiometricVerified(): Boolean {
        return biometricVerified
    }

    private fun createPromptInfo(allowUsePin: Boolean): BiometricPrompt.PromptInfo {
        return BiometricPrompt.PromptInfo.Builder()
            .setTitle(getString(R.string.app_name))
            .setSubtitle("setup fingerprint")
            //.setDescription(getString(R.string.prompt_info_description))
            // Authenticate without requiring the user to press a "confirm"
            // button after satisfying the biometric check
            .setConfirmationRequired(false)
            .setNegativeButtonText(if (allowUsePin) "Use PIN" else "Cancel")
            .build()
    }
}