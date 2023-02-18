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

package pk.sufiishq.app.core.applock

import pk.sufiishq.app.core.applock.model.SecurityQuestion

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
    class ConfirmChangePinPrompt(val previousPin: String, val cameFromForgotPin: Boolean = false) :
        AppLockState
}
