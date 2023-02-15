package pk.sufiishq.app.ui.screen.applock

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import io.github.esentsov.PackagePrivate
import kotlinx.coroutines.launch
import pk.sufiishq.app.R
import pk.sufiishq.app.data.controller.AppLockController
import pk.sufiishq.app.ui.components.dialogs.ConfirmDialogParam
import pk.sufiishq.app.ui.components.dialogs.ConfirmationDialog
import pk.sufiishq.app.utils.getString
import pk.sufiishq.app.utils.optString
import pk.sufiishq.app.utils.rem
import pk.sufiishq.aurora.components.SIHeightSpace
import pk.sufiishq.aurora.components.SIImage
import pk.sufiishq.aurora.components.SIText
import pk.sufiishq.aurora.components.SIWidthSpace
import pk.sufiishq.aurora.layout.SIColumn
import pk.sufiishq.aurora.layout.SIRow
import pk.sufiishq.aurora.theme.AuroraColor

@PackagePrivate
@Composable
fun AppLockSetting(
    appLockController: AppLockController,
    scaffoldState: ScaffoldState,
    snackbarMessage: String?,
    biometricEnable: Boolean
) {

    val fragmentActivity = LocalContext.current as FragmentActivity
    val autoLockDuration = appLockController.getAutoLockDuration()

    snackbarMessage?.let {
        val scope = rememberCoroutineScope()
        scope.launch {
            scaffoldState.snackbarHostState.showSnackbar(snackbarMessage)
        }
    }

    SIColumn(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {

        val param = rem<ConfirmDialogParam?>(null)
        ConfirmationDialog(state = param)

        Header()
        SIHeightSpace(value = 12)
        AppLockServiceCardWithButton(
            infoDrawableId = R.drawable.change_pin,
            title = optString(R.string.title_change_pin),
            detail = optString(R.string.detail_change_pin),
            actionButtonTitle = optString(R.string.label_change),
            actionButtonClick = {
                appLockController.userWantChangePin()
            }
        )
        SIHeightSpace(value = 12)
        AppLockServiceCardWithToggle(
            infoDrawableId = R.drawable.toggle_fingerprint,
            title = optString(R.string.title_toggle_biometric),
            detail = optString(R.string.detail_toggle_biometric),
            isCheck = biometricEnable,
            onCheckedChanged = {
                appLockController.toggleBiometric(fragmentActivity)
            }
        )
        SIHeightSpace(value = 12)
        AppLockServiceCardWithButton(
            infoDrawableId = R.drawable.update_security_question,
            title = optString(R.string.title_update_security_question),
            detail = optString(R.string.detail_update_security_question),
            actionButtonTitle = optString(R.string.label_update),
            actionButtonClick = {
                appLockController.userWantUpdateSecurityQuestion()
            }
        )
        SIHeightSpace(value = 12)
        AppLockDurationServiceCard(
            infoDrawableId = R.drawable.lock_time,
            title = optString(R.string.title_app_lock),
            detail = optString(R.string.detail_change_app_lock_time),
            selectedDurationCode = autoLockDuration.value!!.code,
            onDurationChanged = {
                appLockController.updateAutoLockDuration(it)
            }
        )
        SIHeightSpace(value = 12)
        AppLockServiceCardWithButton(
            infoDrawableId = R.drawable.remove_app_lock,
            title = optString(R.string.title_remove_app_lock),
            detail = optString(R.string.detail_remove_app_lock),
            actionButtonTitle = optString(R.string.label_remove),
            actionButtonClick = {
                param.value = ConfirmDialogParam(getString(R.string.msg_confirm_remove_app_lock)) {
                    appLockController.removeAppLock()
                }
            }
        )
    }
}

@Composable
private fun Header() {
    SIRow(
        modifier = Modifier
            .fillMaxWidth(),
        bgColor = AuroraColor.SecondaryVariant,
        padding = 12,
        radius = 4,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SIImage(
            modifier = Modifier.height(30.dp),
            resId = R.drawable.shield
        )
        SIWidthSpace(value = 8)
        SIText(
            text = optString(R.string.label_setting),
            textColor = it,
            fontWeight = FontWeight.Bold
        )
    }
}