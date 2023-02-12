package pk.sufiishq.app.core.firebase

import com.google.firebase.auth.FirebaseUser
import pk.sufiishq.app.R
import pk.sufiishq.app.utils.getString

sealed class AuthState(val message: String?) {

    class Error(
        val ex: Exception?,
        message: String = getString(R.string.msg_authorization_failed)
    ) : AuthState(message)

    class Cancelled(message: String? = null) : AuthState(message)
    object InProgress : AuthState(null)
    class Success(val firebaseUser: FirebaseUser, val userIsDeveloper: Boolean = false) :
        AuthState("")
}