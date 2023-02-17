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

package pk.sufiishq.app.data.controller

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import pk.sufiishq.app.core.applock.AppLockState
import pk.sufiishq.app.models.AutoLockDuration
import pk.sufiishq.app.models.SecurityQuestion

interface AppLockController {

    // -------------------------------------------------------------------- //
    // signatures checking and setting the active state. #AppLockState
    // -------------------------------------------------------------------- //

    fun getActiveState(): LiveData<AppLockState>
    fun setState(newState: AppLockState)

    // -------------------------------------------------------------------- //
    // signatures other controlling functionality
    // -------------------------------------------------------------------- //

    fun userHasBiometricCapability(): Boolean
    fun setupBiometricOrPin(fragmentActivity: FragmentActivity)
    fun cancelFlow()
    fun gotoSetting()
    fun pinGenerated(pin: String)
    fun pinConfirmed(pin: String)
    fun registerNewPin(securityQuestion: SecurityQuestion, generatedPin: String)
    fun userWantChangePin(cameFromForgotPin: Boolean = false)
    fun toggleBiometric(fragmentActivity: FragmentActivity)
    fun userWantUpdateSecurityQuestion()
    fun updateSecurityQuestion(securityQuestion: SecurityQuestion)
    fun updateAutoLockDuration(autoLockDuration: AutoLockDuration)
    fun getAutoLockDuration(): LiveData<AutoLockDuration>
    fun removeAppLock()
    fun changePinGenerated(pin: String, cameFromForgotPin: Boolean)
    fun changePinConfirmed(pin: String)
    fun forgotPin()
    fun promptBiometric(fragmentActivity: FragmentActivity)
}
