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

package pk.sufiishq.app.viewmodels

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import pk.sufiishq.app.core.applock.AppLockManager
import pk.sufiishq.app.core.applock.AppLockState
import pk.sufiishq.app.core.applock.BiometricManager
import pk.sufiishq.app.core.applock.BiometricStatus
import pk.sufiishq.app.data.controller.AppLockController
import pk.sufiishq.app.models.AutoLockDuration
import pk.sufiishq.app.models.SecurityQuestion
import javax.inject.Inject

@HiltViewModel
class AppLockViewModel
@Inject
constructor(
    private val appLockManager: AppLockManager,
    private val bioMetricManager: BiometricManager,
) : ViewModel(), AppLockController {

    private var biometricVerified = false

    override fun getActiveState(): LiveData<AppLockState> {
        return appLockManager.getActiveState()
    }

    override fun setState(newState: AppLockState) {
        appLockManager.setState(newState)
    }

    override fun userHasBiometricCapability(): Boolean {
        return bioMetricManager.userHasBiometricCapability()
    }

    override fun setupBiometricOrPin(fragmentActivity: FragmentActivity) {
        if (userHasBiometricCapability()) {
            bioMetricManager.prompt(fragmentActivity) {
                biometricVerified = false
                when (it) {
                    is BiometricStatus.Success -> {
                        biometricVerified = true
                        setState(AppLockState.NewPinPrompt)
                    }
                    is BiometricStatus.UsePin -> {
                        setState(AppLockState.NewPinPrompt)
                    }
                    else -> {
                        /* do nothing otherwise */
                    }
                }
            }
        } else {
            setState(AppLockState.NewPinPrompt)
        }
    }

    override fun cancelFlow() {
        biometricVerified = false
        appLockManager.setState(AppLockState.Setup)
    }

    override fun gotoSetting() {
        appLockManager.gotoSetting()
    }

    override fun pinGenerated(pin: String) {
        appLockManager.setState(AppLockState.ConfirmNewPinPrompt(pin))
    }

    override fun pinConfirmed(pin: String) {
        setState(AppLockState.SecurityQuestionPrompt(pin))
    }

    override fun registerNewPin(securityQuestion: SecurityQuestion, generatedPin: String) {
        appLockManager.registerNewPin(securityQuestion, generatedPin, biometricVerified)
    }

    override fun userWantChangePin(cameFromForgotPin: Boolean) {
        appLockManager.setState(AppLockState.ChangePinPrompt(cameFromForgotPin = cameFromForgotPin))
    }

    override fun toggleBiometric(fragmentActivity: FragmentActivity) {
        appLockManager.toggleBiometric(fragmentActivity)
    }

    override fun userWantUpdateSecurityQuestion() {
        appLockManager.setState(AppLockState.ChangeSecurityQuestion)
    }

    override fun updateSecurityQuestion(securityQuestion: SecurityQuestion) {
        appLockManager.updateSecurityQuestion(securityQuestion)
    }

    override fun updateAutoLockDuration(autoLockDuration: AutoLockDuration) {
        appLockManager.updateAutoLockDuration(autoLockDuration)
    }

    override fun getAutoLockDuration(): LiveData<AutoLockDuration> {
        return appLockManager.getAutoLockDuration()
    }

    override fun removeAppLock() {
        appLockManager.removeAppLock()
    }

    override fun changePinGenerated(pin: String, cameFromForgotPin: Boolean) {
        appLockManager.setState(
            AppLockState.ConfirmChangePinPrompt(
                previousPin = pin,
                cameFromForgotPin = cameFromForgotPin,
            ),
        )
    }

    override fun changePinConfirmed(pin: String) {
        appLockManager.changePinConfirmed(pin)
    }

    override fun forgotPin() {
        appLockManager.forgotPin()
    }

    override fun promptBiometric(fragmentActivity: FragmentActivity) {
        if (appLockManager.isBiometricEnabled()) {
            bioMetricManager.prompt(fragmentActivity, true) {
                if (it is BiometricStatus.Success) {
                    appLockManager.gotoSetting()
                }
            }
        }
    }
}
