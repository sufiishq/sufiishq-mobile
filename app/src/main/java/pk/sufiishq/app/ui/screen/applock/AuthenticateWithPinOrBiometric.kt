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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import io.github.esentsov.PackagePrivate
import pk.sufiishq.app.R
import pk.sufiishq.app.core.applock.controller.AppLockController
import pk.sufiishq.app.utils.optString
import pk.sufiishq.aurora.components.SIHeightSpace
import pk.sufiishq.aurora.components.SIText
import pk.sufiishq.aurora.components.TextSize
import pk.sufiishq.aurora.layout.SIBox
import pk.sufiishq.aurora.layout.SIColumn

@PackagePrivate
@Composable
fun AuthenticateWithPinOrBiometric(
    appLockController: AppLockController,
    validPin: String,
) {
    SIBox(modifier = Modifier.fillMaxSize()) {
        AppLockHeader(
            modifier = Modifier.align(Alignment.TopCenter),
            buttonTitle = optString(R.string.label_forgot_pin),
            onButtonClick = { appLockController.forgotPin() },
        )

        SIColumn(
            modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            SIHeightSpace(value = 36)
            SIText(
                text = optString(R.string.label_enter_pin),
                textColor = it,
                textSize = TextSize.Large,
            )
            SIHeightSpace(value = 24)
            AppLockKeyboardWithPinView(
                onPinGenerated = { appLockController.gotoSetting() },
                validPin = validPin,
            )
        }
    }

    val fragmentActivity = LocalContext.current as FragmentActivity
    LaunchedEffect(Unit) { appLockController.promptBiometric(fragmentActivity) }
}
