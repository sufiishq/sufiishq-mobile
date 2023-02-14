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
import pk.sufiishq.app.data.providers.AppLockController
import pk.sufiishq.app.ui.components.dialogs.ConfirmDialogParam
import pk.sufiishq.app.ui.components.dialogs.ConfirmationDialog
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
            title = "Change PIN",
            detail = "Update your pin any time and also make sure to use a strong pin and avoid using a simple pin so that anyone can't guess.",
            actionButtonTitle = "Change",
            actionButtonClick = {
                appLockController.userWantChangePin()
            }
        )
        SIHeightSpace(value = 12)
        AppLockServiceCardWithToggle(
            infoDrawableId = R.drawable.toggle_fingerprint,
            title = "On/Off Fingerprint",
            detail = "You can enable or disable the fingerprint option at any time from here.",
            isCheck = biometricEnable,
            onCheckedChanged = {
                appLockController.toggleBiometric(fragmentActivity)
            }
        )
        SIHeightSpace(value = 12)
        AppLockServiceCardWithButton(
            infoDrawableId = R.drawable.update_security_question,
            title = "Update Security Question",
            detail = "You can update your security question here at any time from here.",
            actionButtonTitle = "Update",
            actionButtonClick = {
                appLockController.userWantUpdateSecurityQuestion()
            }
        )
        SIHeightSpace(value = 12)
        AppLockDurationServiceCard(
            infoDrawableId = R.drawable.lock_time,
            title = "Auto Lock",
            detail = "You can change the auto app lock duration at any time.",
            selectedDurationCode = autoLockDuration.value!!.code,
            onDurationChanged = {
                appLockController.updateAutoLockDuration(it)
            }
        )
        SIHeightSpace(value = 12)
        AppLockServiceCardWithButton(
            infoDrawableId = R.drawable.remove_app_lock,
            title = "Remove App Lock",
            detail = "You can remove your pin and fingerprint lock here at any time.",
            actionButtonTitle = "Remove",
            actionButtonClick = {
                param.value = ConfirmDialogParam("Are you sure you want to remove App Lock?") {
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
            text = "Setting",
            textColor = it,
            fontWeight = FontWeight.Bold
        )
    }
}