/*
 * Copyright 2022-2023 SufiIshq
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pk.sufiishq.app.feature.applock

import android.content.Context
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import pk.sufiishq.app.R
import pk.sufiishq.app.utils.extention.hasBiometricCapability
import pk.sufiishq.app.utils.getString
import timber.log.Timber
import javax.inject.Inject

class BiometricManager
@Inject
constructor(
    @ApplicationContext private val appContext: Context,
) {

    private var biometricVerified = false

    fun userHasBiometricCapability(): Boolean {
        return appContext.hasBiometricCapability()
    }

    fun prompt(
        activity: FragmentActivity,
        allowUsePin: Boolean = true,
        callback: (biometricStatus: BiometricStatus) -> Unit,
    ) {
        val biometricCallback =
            object : BiometricPrompt.AuthenticationCallback() {
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
                    Timber.d(getString(R.string.msg_biometric_failed_unknown))
                    callback(BiometricStatus.Failed)
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    biometricVerified = true
                    callback(BiometricStatus.Success)
                }
            }

        val executor = ContextCompat.getMainExecutor(activity)
        val biometricPrompt = BiometricPrompt(activity, executor, biometricCallback)
        biometricPrompt.authenticate(createPromptInfo(allowUsePin))
    }

    private fun createPromptInfo(allowUsePin: Boolean): BiometricPrompt.PromptInfo {
        return BiometricPrompt.PromptInfo.Builder()
            .setTitle(getString(R.string.app_name))
            .setConfirmationRequired(false)
            .setNegativeButtonText(
                if (allowUsePin) {
                    getString(R.string.label_use_pin)
                } else {
                    getString(
                        R.string.label_cancel,
                    )
                },
            )
            .build()
    }
}
