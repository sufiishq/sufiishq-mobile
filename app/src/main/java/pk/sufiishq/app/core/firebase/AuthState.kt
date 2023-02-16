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

package pk.sufiishq.app.core.firebase

import com.google.firebase.auth.FirebaseUser
import pk.sufiishq.app.R
import pk.sufiishq.app.utils.getString

sealed class AuthState(val message: String?) {

    class Error(
        val ex: Exception?,
        message: String = getString(R.string.msg_authorization_failed),
    ) : AuthState(message)

    class Cancelled(message: String? = null) : AuthState(message)
    object InProgress : AuthState(null)
    class Success(val firebaseUser: FirebaseUser, val userIsDeveloper: Boolean = false) :
        AuthState("")
}
