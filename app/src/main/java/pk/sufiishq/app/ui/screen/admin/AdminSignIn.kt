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

package pk.sufiishq.app.ui.screen.admin

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import io.github.esentsov.PackagePrivate
import pk.sufiishq.app.feature.admin.controller.AdminController
import pk.sufiishq.app.utils.ImageRes
import pk.sufiishq.app.utils.TextRes
import pk.sufiishq.app.utils.extention.optString
import pk.sufiishq.aurora.components.SIButton
import pk.sufiishq.aurora.components.SIHeightSpace
import pk.sufiishq.aurora.components.SIImage
import pk.sufiishq.aurora.layout.SIBox
import pk.sufiishq.aurora.layout.SIColumn

@PackagePrivate
@Composable
fun AdminSignIn(
    adminController: AdminController,
) {
    val context = LocalContext.current

    SIBox(modifier = Modifier.fillMaxSize()) {
        SIColumn(horizontalAlignment = Alignment.CenterHorizontally) {
            SIImage(
                resId = ImageRes.authentication_full,
                tintColor = it,
            )
            SIHeightSpace(value = 12)
            SIButton(
                text = optString(TextRes.label_verify_identity),
                leadingIcon = ImageRes.key,
                onClick = { adminController.signIn(context as ComponentActivity) },
            )
        }
    }
}
