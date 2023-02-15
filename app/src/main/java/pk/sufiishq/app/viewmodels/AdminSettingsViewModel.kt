package pk.sufiishq.app.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import androidx.activity.ComponentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DatabaseException
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import pk.sufiishq.app.R
import pk.sufiishq.app.core.firebase.AuthManager
import pk.sufiishq.app.core.firebase.AuthState
import pk.sufiishq.app.core.firebase.FirebaseDatabaseStatus
import pk.sufiishq.app.core.firebase.HighlightManager
import pk.sufiishq.app.core.firebase.HighlightStatus
import pk.sufiishq.app.core.firebase.MaintenanceManager
import pk.sufiishq.app.data.controller.AdminController
import pk.sufiishq.app.models.Maintenance
import pk.sufiishq.app.utils.getString
import pk.sufiishq.app.utils.isNetworkAvailable

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class AdminSettingsViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val authManager: AuthManager,
    private val highlightManager: HighlightManager,
    private val maintenanceManager: MaintenanceManager
) : ViewModel(), AdminController {

    private val showSnackbar = MutableLiveData<String?>(null)
    private val showLoader = MutableLiveData(false)

    init {
        launchWithOnline {
            authManager.tryToAuthLogin()
        }
    }

    override fun showLoader(): LiveData<Boolean> {
        return showLoader
    }

    override fun showSnackbar(message: String?) {
        showLoader(false)
        showSnackbar.postValue(message)
    }

    override fun showSnackbar(): LiveData<String?> {
        return showSnackbar
    }

    override fun showLoader(isShow: Boolean) {
        showLoader.postValue(isShow)
    }

    private fun callIfOnline(block: () -> Unit) {
        context
            .takeIf { it.isNetworkAvailable() }
            ?.apply { block() }
            ?: showSnackbar(getString(R.string.msg_no_network_connection))
    }

    private fun launchWithOnline(block: () -> Unit) {
        callIfOnline {
            showLoader(true)
            block()
        }
    }

    private fun launchAsyncWithOnline(block: suspend CoroutineScope.() -> Unit) {
        callIfOnline {
            viewModelScope.launch {
                showLoader(true)
                block()
            }
        }
    }

    // -------------------------------------------------------------------- //
    // authentication / authorization functionality
    // -------------------------------------------------------------------- //

    override fun checkAuthentication(): LiveData<AuthState> {
        return authManager.checkAuthentication()
    }

    override fun signIn(activity: ComponentActivity) {
        launchWithOnline {
            authManager.signIn(activity)
        }
    }

    override fun signOut(activity: ComponentActivity) {
        authManager.signOut(activity)
    }

    // -------------------------------------------------------------------- //
    // highlight form functionality
    // -------------------------------------------------------------------- //

    override fun highlightStatus(): LiveData<HighlightStatus?> {
        return highlightManager.highlightStatus()
    }

    override fun deleteHighlight() {
        launchAsyncWithOnline {
            highlightManager.delete().let(::verifyDelete)
        }
    }

    override fun startDate(): LiveData<Calendar> {
        return highlightManager.startDate()
    }

    override fun endDate(): LiveData<Calendar> {
        return highlightManager.endDate()
    }

    override fun minEndDate(): LiveData<Calendar> {
        return highlightManager.minEndDate()
    }

    override fun startDateChanged(calendar: Calendar) {
        highlightManager.startDateChanged(calendar)
    }

    override fun endDateChanged(calendar: Calendar) {
        highlightManager.endDateChanged(calendar)
    }

    override fun startTimeChanged(calendar: Calendar) {
        highlightManager.startTimeChanged(calendar)
    }

    override fun endTimeChanged(calendar: Calendar) {
        highlightManager.endTimeChanged(calendar)
    }

    override fun startTime(): LiveData<Calendar> {
        return highlightManager.startTime()
    }

    override fun endTime(): LiveData<Calendar> {
        return highlightManager.endTime()
    }

    override fun getTitle(): LiveData<String?> {
        return highlightManager.getTitle()
    }

    override fun getDetail(): LiveData<String?> {
        return highlightManager.getDetail()
    }

    override fun getContacts(): LiveData<MutableList<Pair<String, String>?>> {
        return highlightManager.getContacts()
    }

    override fun setTitle(newTitle: String) {
        highlightManager.setTitle(newTitle)
    }

    override fun setDetail(newDetail: String) {
        highlightManager.setDetail(newDetail)
    }

    override fun fetchHighlight() {
        launchAsyncWithOnline {
            highlightManager.fetch().apply(::resolveHighlight)
        }
    }

    override fun addOrUpdateHighlight() {
        launchWithValidation {
            highlightManager
                .addOrUpdate()
                .let(::resolveMessage)
                .apply(::showSnackbar)
        }
    }

    private fun resolveMessage(status: FirebaseDatabaseStatus): String? {
        return if (status is FirebaseDatabaseStatus.Failed && status.ex is DatabaseException) {
            getString(R.string.msg_authorization_failed)
        } else {
            fetchHighlight()
            status.message
        }
    }

    private fun resolveHighlight(status: FirebaseDatabaseStatus) {
        if (status is FirebaseDatabaseStatus.ReadHighlight) {
            highlightManager.load(status.highlight)
            showLoader(false)
        } else {
            showSnackbar(status.message)
        }
    }

    private fun verifyDelete(status: FirebaseDatabaseStatus) {
        showSnackbar(status.message)
        if (status is FirebaseDatabaseStatus.Delete) {
            highlightManager.clear()
        }
    }

    private fun validateTitle(block: () -> Unit) {
        getTitle()
            .takeIf { it.value?.trim()?.isNotEmpty() == true }
            ?.apply { block() }
            ?: showSnackbar(getString(R.string.msg_title_required))
    }

    private fun validateDetail(block: () -> Unit) {
        getDetail()
            .takeIf { it.value?.trim()?.isNotEmpty() == true }
            ?.apply { block() }
            ?: showSnackbar(getString(R.string.msg_detail_required))
    }

    private fun launchWithValidation(block: suspend CoroutineScope.() -> Unit) {
        validateTitle {
            validateDetail {
                launchAsyncWithOnline {
                    block()
                }
            }
        }
    }

    // -------------------------------------------------------------------- //
    // maintenance control functionality
    // -------------------------------------------------------------------- //

    override fun setMaintenanceStatus(status: Boolean) {
        callIfOnline {
            launchAsyncWithOnline {
                maintenanceManager.setActiveStatus(status).message.apply(::showSnackbar)
            }
        }
    }

    override fun setMaintenanceStrict(mode: Boolean) {
        callIfOnline {
            launchAsyncWithOnline {
                maintenanceManager.setStrictMode(mode).message.apply(::showSnackbar)
            }
        }
    }

    override fun getMaintenance(): LiveData<Maintenance?> {
        return maintenanceManager.getMaintenance()
    }
}