package pk.sufiishq.app.data.providers

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import pk.sufiishq.app.core.applock.AppLockState
import pk.sufiishq.app.models.AutoLockDuration
import pk.sufiishq.app.models.SecurityQuestion

interface AppLockController {

    fun getActiveState(): LiveData<AppLockState>
    fun setState(newState: AppLockState)

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