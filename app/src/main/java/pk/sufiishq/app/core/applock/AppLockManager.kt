package pk.sufiishq.app.core.applock

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import java.util.*
import javax.inject.Inject
import pk.sufiishq.app.models.AppLockStatus
import pk.sufiishq.app.models.AutoLockDuration
import pk.sufiishq.app.models.SecurityQuestion
import pk.sufiishq.app.utils.getFromStorage
import pk.sufiishq.app.utils.instantAutoLockDuration
import pk.sufiishq.app.utils.putInStorage

class AppLockManager @Inject constructor(
    private val gson: Gson,
    private val biometricManager: BiometricManager
) {

    private val activeState = MutableLiveData<AppLockState>(AppLockState.Setup)
    private val autoLockDuration = MutableLiveData(instantAutoLockDuration())
    private val appLockStatus = MutableLiveData<AppLockStatus?>(null)

    init {
        setUpState()
    }

    private fun setUpState() {
        if (userHasAlreadyLockSetUp()) {
            checkAppLockStatus()
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

    fun registerNewPin(
        securityQuestion: SecurityQuestion,
        generatedPin: String,
        biometricEnable: Boolean
    ) {
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
        AUTO_LOCK_DURATION.putInStorage("")
        setState(AppLockState.Setup)
    }

    fun changePinConfirmed(pin: String) {
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
        return biometricManager.userHasBiometricCapability() && HAS_BIOMETRIC_ENABLE.getFromStorage(
            false
        )
    }

    fun forgotPin() {
        setState(AppLockState.ForgotPin(fetchSecurityQuestion()))
    }

    fun setExitAppTime() {
        EXIT_APP_TIME.putInStorage(Calendar.getInstance().timeInMillis)
    }

    fun getAppLockStatus(): LiveData<AppLockStatus?> {
        return appLockStatus
    }

    fun promptBiometricForVerification(fragmentActivity: FragmentActivity) {
        biometricManager.prompt(fragmentActivity) {
            if (it is BiometricStatus.Success) {
                setAppLockStatus(null)
            }
        }
    }

    private fun getExitAppTime(): Long {
        return EXIT_APP_TIME.getFromStorage(0L)
    }

    private fun checkAppLockStatus() {
        val autoLockDuration = fetchAutoLockDuration()
        val exitAppTime = getExitAppTime()
        val currentTime = Calendar.getInstance().timeInMillis

        if (autoLockDuration.code == 0) {
            setAppLockStatus(
                AppLockStatus(
                    isBiometricEnable = isBiometricEnabled(),
                    savedPin = getSavedPin(),
                    securityQuestion = fetchSecurityQuestion()
                )
            )
        } else if (exitAppTime == 0L) {
            setAppLockStatus(null)
        } else {
            if ((autoLockDuration.durationInMillis + exitAppTime) <= currentTime) {
                setAppLockStatus(
                    AppLockStatus(
                        isBiometricEnable = isBiometricEnabled(),
                        savedPin = getSavedPin(),
                        securityQuestion = fetchSecurityQuestion()
                    )
                )
            } else {
                setAppLockStatus(null)
            }
        }
    }

    fun setAppLockStatus(status: AppLockStatus?) {
        appLockStatus.value = status
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
        BACKUP_SECURITY_QUESTION.putInStorage(
            gson.toJson(securityQuestion)
        )
        //BACKUP_SECURITY_ANSWER.putInStorage(securityQuestion.answer)
    }

    private fun setAutoLockDuration(autoLockDuration: AutoLockDuration) {
        AUTO_LOCK_DURATION.putInStorage(gson.toJson(autoLockDuration))
        postAutoLockDuration(autoLockDuration)
    }

    private fun userHasAlreadyLockSetUp(): Boolean {
        return SAVED_PIN.getFromStorage("").trim().isNotEmpty()
    }

    private fun fetchSecurityQuestion(): SecurityQuestion {
        return BACKUP_SECURITY_QUESTION
            .getFromStorage("")
            .let {
                gson.fromJson(it, SecurityQuestion::class.java)
            }
    }

    companion object {
        private const val SAVED_PIN = "si_saved_pin"
        private const val HAS_BIOMETRIC_ENABLE = "si_has_biometric_enable"
        private const val BACKUP_SECURITY_QUESTION = "si_backup_security_question"
        private const val AUTO_LOCK_DURATION = "si_auto_lock_duration"
        private const val EXIT_APP_TIME = "si_exit_app_time"
    }
}