package pk.sufiishq.app.core.firebase

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
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import pk.sufiishq.app.R
import pk.sufiishq.app.data.repository.AdminSettingsRepository
import pk.sufiishq.app.di.qualifier.IoDispatcher
import pk.sufiishq.app.utils.getString
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

@Singleton
class AuthManager @Inject constructor(
    private val adminSettingsRepository: AdminSettingsRepository,
    private val auth: FirebaseAuth,
    private val gso: GoogleSignInOptions,
    private val authUI: AuthUI,
    @IoDispatcher private val dispatcher: CoroutineContext
) {

    private val authState = MutableLiveData<AuthState>(AuthState.Cancelled(null))

    private var resultLauncher: ActivityResultLauncher<Intent>? = null
    private var activity: ComponentActivity? = null

    fun tryToAuthLogin() {
        safeCall {
            auth.currentUser?.let {
                authState.postValue(
                    AuthState.Success(it, userIsDeveloper(it))
                )
            }
        }
    }

    fun registerActivityResultListener(activity: ComponentActivity) {
        resultLauncher =
            activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    safeCall {
                        onActivityResult(result.data)
                    }
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
        GoogleSignIn.getClient(activity, gso)
            .apply { resultLauncher?.launch(signInIntent) }
    }

    fun signOut(componentActivity: ComponentActivity?) {
        componentActivity?.apply {
            authUI
                .signOut(this)
                .addOnSuccessListener {
                    setState(AuthState.Cancelled())
                }
                .addOnFailureListener {
                    Timber.e(it)
                }
                .also {
                    activity = this
                }
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
        val userIsValid = adminSettingsRepository.getValidEmails()
            .split(",")
            .any { it.trim() == auth.currentUser?.email }

        if (userIsValid) {
            setState(
                AuthState.Success(
                    auth.currentUser!!,
                    userIsDeveloper = userIsDeveloper(auth.currentUser)
                )
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
            adminSettingsRepository.getValidDeveloperEmails()
                .split(",")
                .any { it.trim() == firebaseUser?.email }
        }
    }

    private suspend fun <T> tryAsyncWithDefault(default: T, block: suspend () -> T): T {
        return try {
            block()
        } catch (ex: Exception) {
            Timber.e(ex)
            default
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