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
import pk.sufiishq.app.feature.applock.controller.AppLockController
import pk.sufiishq.app.ui.components.dialogs.ConfirmDialogParam
import pk.sufiishq.app.ui.components.dialogs.ConfirmationDialog
import pk.sufiishq.app.utils.ImageRes
import pk.sufiishq.app.utils.TextRes
import pk.sufiishq.app.utils.extention.optString
import pk.sufiishq.app.utils.getString
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
    biometricEnable: Boolean,
) {
    val fragmentActivity = LocalContext.current as FragmentActivity
    val autoLockDuration = appLockController.getAutoLockDuration()

    snackbarMessage?.let {
        val scope = rememberCoroutineScope()
        scope.launch { scaffoldState.snackbarHostState.showSnackbar(snackbarMessage) }
    }

    Header()

    SIColumn(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
    ) {
        val param = rem<ConfirmDialogParam?>(null)
        ConfirmationDialog(state = param)

        SIHeightSpace(value = 12)
        AppLockServiceCardWithButton(
            infoDrawableId = ImageRes.change_pin,
            title = optString(TextRes.title_change_pin),
            detail = optString(TextRes.detail_change_pin),
            actionButtonTitle = optString(TextRes.label_change),
            actionButtonClick = { appLockController.userWantChangePin() },
        )
        SIHeightSpace(value = 12)
        AppLockServiceCardWithToggle(
            infoDrawableId = ImageRes.toggle_fingerprint,
            title = optString(TextRes.title_toggle_biometric),
            detail = optString(TextRes.detail_toggle_biometric),
            isCheck = biometricEnable,
            onCheckedChanged = { appLockController.toggleBiometric(fragmentActivity) },
        )
        SIHeightSpace(value = 12)
        AppLockServiceCardWithButton(
            infoDrawableId = ImageRes.update_security_question,
            title = optString(TextRes.title_update_security_question),
            detail = optString(TextRes.detail_update_security_question),
            actionButtonTitle = optString(TextRes.label_update),
            actionButtonClick = { appLockController.userWantUpdateSecurityQuestion() },
        )
        SIHeightSpace(value = 12)
        AppLockDurationServiceCard(
            infoDrawableId = ImageRes.lock_time,
            title = optString(TextRes.title_app_lock),
            detail = optString(TextRes.detail_change_app_lock_time),
            selectedDurationCode = autoLockDuration.value!!.code,
            onDurationChanged = { appLockController.updateAutoLockDuration(it) },
        )
        SIHeightSpace(value = 12)
        AppLockServiceCardWithButton(
            infoDrawableId = ImageRes.remove_app_lock,
            title = optString(TextRes.title_remove_app_lock),
            detail = optString(TextRes.detail_remove_app_lock),
            actionButtonTitle = optString(TextRes.label_remove),
            actionButtonClick = {
                param.value =
                    ConfirmDialogParam(getString(TextRes.msg_confirm_remove_app_lock)) {
                        appLockController.removeAppLock()
                    }
            },
        )
    }
}

@Composable
private fun Header() {
    SIRow(
        modifier = Modifier.fillMaxWidth(),
        bgColor = AuroraColor.SecondaryVariant,
        padding = 12,
        radius = 4,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SIImage(
            modifier = Modifier.height(30.dp),
            resId = ImageRes.shield,
        )
        SIWidthSpace(value = 8)
        SIText(
            text = optString(TextRes.label_setting),
            textColor = it,
            fontWeight = FontWeight.Bold,
        )
    }
}
