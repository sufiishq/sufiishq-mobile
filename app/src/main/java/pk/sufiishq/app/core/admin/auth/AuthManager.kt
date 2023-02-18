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

package pk.sufiishq.app.core.admin.auth

import android.app.Activity
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import pk.sufiishq.app.R
import pk.sufiishq.app.core.admin.repository.AdminRepository
import pk.sufiishq.app.di.qualifier.IoDispatcher
import pk.sufiishq.app.utils.getString
import pk.sufiishq.app.utils.tryAsyncWithDefault
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Singleton
class AuthManager
@Inject
constructor(
    private val adminRepository: AdminRepository,
    private val auth: FirebaseAuth,
    private val gso: GoogleSignInOptions,
    private val authUI: AuthUI,
    @IoDispatcher private val dispatcher: CoroutineContext,
) {

    private val authState = MutableLiveData<AuthState>(AuthState.Cancelled(null))

    private var resultLauncher: ActivityResultLauncher<Intent>? = null
    private var activity: ComponentActivity? = null

    fun tryToAuthLogin() {
        safeCall {
            auth.currentUser?.let {
                authState.postValue(
                    AuthState.Success(it, userIsDeveloper(it)),
                )
            }
        }
    }

    fun registerActivityResultListener(activity: ComponentActivity) {
        resultLauncher =
            activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                    result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    safeCall { onActivityResult(result.data) }
                } else {
                    setState(AuthState.Cancelled(getString(R.string.label_cancelled)))
                }
            }
    }

    fun checkAuthentication(): LiveData<AuthState> {
        return authState
    }

    fun signIn(activity: ComponentActivity) {
        setState(AuthState.InProgress)
        GoogleSignIn.getClient(activity, gso).apply { resultLauncher?.launch(signInIntent) }
    }

    fun signOut(componentActivity: ComponentActivity?) {
        componentActivity?.apply {
            authUI
                .signOut(this)
                .addOnSuccessListener { setState(AuthState.Cancelled()) }
                .addOnFailureListener { Timber.e(it) }
                .also { activity = this }
        }
    }

    private suspend fun onActivityResult(data: Intent?) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        val account = task.getResult(ApiException::class.java)!!
        firebaseAuthWithGoogle(account.idToken!!)
    }

    private suspend fun firebaseAuthWithGoogle(idToken: String) {
        // start authenticate with firebase
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).await()

        // get valid emails
        val userIsValid =
            adminRepository.getValidEmails().split(",").any {
                it.trim() == auth.currentUser?.email
            }

        if (userIsValid) {
            setState(
                AuthState.Success(
                    auth.currentUser!!,
                    userIsDeveloper = userIsDeveloper(auth.currentUser),
                ),
            )
        } else {
            setState(AuthState.Error(null, getString(R.string.msg_identification_failed)))
            signOut(activity)
        }
    }

    private fun setState(state: AuthState) {
        authState.postValue(state)
    }

    private suspend fun userIsDeveloper(firebaseUser: FirebaseUser?): Boolean {
        return tryAsyncWithDefault(false) {
            adminRepository.getValidDeveloperEmails().split(",").any {
                it.trim() == firebaseUser?.email
            }
        }
    }

    private fun safeCall(block: suspend CoroutineScope.() -> Unit) {
        CoroutineScope(dispatcher).launch {
            try {
                block()
            } catch (ex: Exception) {
                setState(AuthState.Error(ex))
            }
        }
    }
}
