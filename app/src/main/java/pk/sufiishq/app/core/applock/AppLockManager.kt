package pk.sufiishq.app.core.applock

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pk.sufiishq.app.models.AutoLockDuration
import pk.sufiishq.app.models.SecurityQuestion
import pk.sufiishq.app.utils.getFromStorage
import pk.sufiishq.app.utils.instantAutoLockDuration
import pk.sufiishq.app.utils.putInStorage

class AppLockManager @Inject constructor(
    private val gson: Gson,
    private val biometricManager: BiometricManager
){

    private val activeState = MutableLiveData<AppLockState>(AppLockState.Setup)
    private val autoLockDuration = MutableLiveData(instantAutoLockDuration())

    init { setUpState() }

    private fun setUpState() {
        if (userHasAlreadyLockSetUp()) {
            postAutoLockDuration(fetchAutoLockDuration())
            setState(AppLockState.AuthenticateWithPinOrBiometric(getSavedPin()))
        } else {
            setState(AppLockState.Setup)
        }
    }

    fun getActiveState(): LiveData<AppLockState> {
        return activeState
    }

    fun setState(newState: AppLockState) {
        activeState.postValue(newState)
    }

    fun registerNewPin(securityQuestion: SecurityQuestion, generatedPin: String, biometricEnable: Boolean) {
        savePin(generatedPin)
        setBiometric(biometricEnable)
        setSecurityQuestion(securityQuestion)
        setAutoLockDuration(instantAutoLockDuration())
        setSettingState(
            message = "Pin successfully generated, you can also change the app lock time from settings.",
            biometricEnable = biometricEnable
        )
    }

    fun removeAppLock() {
        removePin()
        setBiometric(false)
        BACKUP_SECURITY_QUESTION.putInStorage("")
        BACKUP_SECURITY_ANSWER.putInStorage("")
        AUTO_LOCK_DURATION.putInStorage("")
        setState(AppLockState.Setup)
    }

    fun changePinConfirmed(pin: String){
        SAVED_PIN.putInStorage(pin)
        setSettingState(
            message = "Pin successfully changed",
            biometricEnable = isBiometricEnabled()
        )
    }

    fun toggleBiometric(fragmentActivity: FragmentActivity) {
        if (isBiometricEnabled()) {
            setBiometric(false)
            setSettingState("Fingerprint disable successfully.", false)
        } else {
            if (biometricManager.userHasBiometricCapability()) {

                biometricManager.prompt(fragmentActivity, false) {
                    if (it is BiometricStatus.Success) {
                        setBiometric(true)
                        setSettingState("Fingerprint enable successfully.", true)
                    } else {
                        setSettingState(null, false)
                    }
                }
            } else {
               setSettingState(
                   message = "You need to enable the fingerprint option from the mobile setting first.",
                   biometricEnable = false
               )
            }
        }
    }

    fun updateSecurityQuestion(securityQuestion: SecurityQuestion) {
        setSecurityQuestion(securityQuestion)
        setSettingState(
            message = "Security question successfully updated",
            biometricEnable = isBiometricEnabled()
        )

    }

    fun updateAutoLockDuration(autoLockDuration: AutoLockDuration) {
        setAutoLockDuration(autoLockDuration)
        setSettingState(
            message = "Auto lock duration set to ${autoLockDuration.label}",
            biometricEnable = isBiometricEnabled()
        )
    }

    fun gotoSetting() {
        setSettingState(
            biometricEnable = isBiometricEnabled()
        )
    }

    fun getAutoLockDuration(): MutableLiveData<AutoLockDuration> {
        return autoLockDuration
    }

    private fun savePin(pin: String) {
        SAVED_PIN.putInStorage(pin)
    }

    private fun getSavedPin(): String {
        return SAVED_PIN.getFromStorage("")
    }

    private fun removePin() {
        SAVED_PIN.putInStorage("")
    }

    fun isBiometricEnabled(): Boolean {
        return biometricManager.userHasBiometricCapability() && HAS_BIOMETRIC_ENABLE.getFromStorage(false)
    }

    private fun setBiometric(biometricEnable: Boolean) {
        HAS_BIOMETRIC_ENABLE.putInStorage(biometricEnable)
    }

    private fun setSettingState(message: String? = null, biometricEnable: Boolean) {
        setState(
            AppLockState.Setting(
                message = message,
                biometricEnable = biometricEnable
            )
        )
    }

    private fun fetchAutoLockDuration(): AutoLockDuration {
        return AUTO_LOCK_DURATION
            .getFromStorage("")
            .takeIf { it.isNotEmpty() }
            ?.let {
                gson.fromJson(it, AutoLockDuration::class.java)
            } ?: instantAutoLockDuration()
    }

    private fun postAutoLockDuration(duration: AutoLockDuration) {
        autoLockDuration.postValue(duration)
    }

    private fun setSecurityQuestion(securityQuestion: SecurityQuestion) {
        BACKUP_SECURITY_QUESTION.putInStorage(securityQuestion.question)
        BACKUP_SECURITY_ANSWER.putInStorage(securityQuestion.answer)
    }

    private fun setAutoLockDuration(autoLockDuration: AutoLockDuration) {
        AUTO_LOCK_DURATION.putInStorage(gson.toJson(autoLockDuration))
        postAutoLockDuration(autoLockDuration)
    }

    private fun userHasAlreadyLockSetUp(): Boolean {
        return SAVED_PIN.getFromStorage("").trim().isNotEmpty()
    }

    companion object {
        private const val SAVED_PIN = "si_saved_pin"
        private const val HAS_BIOMETRIC_ENABLE = "si_has_biometric_enable"
        private const val BACKUP_SECURITY_QUESTION = "si_backup_security_question"
        private const val BACKUP_SECURITY_ANSWER = "si_backup_security_answer"
        private const val AUTO_LOCK_DURATION = "si_auto_lock_duration"
    }
}