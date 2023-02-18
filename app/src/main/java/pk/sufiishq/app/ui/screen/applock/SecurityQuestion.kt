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
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.esentsov.PackagePrivate
import kotlinx.coroutines.launch
import pk.sufiishq.app.feature.applock.controller.AppLockController
import pk.sufiishq.aurora.layout.SIBox

@PackagePrivate
@Composable
fun SecurityQuestion(
    appLockController: AppLockController,
    scaffoldState: ScaffoldState,
    generatedPin: String,
) {
    val coroutineScope = rememberCoroutineScope()

    SIBox(modifier = Modifier.fillMaxSize()) {
        SecurityQuestionList(
            modifier = Modifier.align(Alignment.TopCenter),
            scaffoldState = scaffoldState,
            headerButtonClick = { appLockController.cancelFlow() },
            onDoneClick = {
                coroutineScope.launch { appLockController.registerNewPin(it, generatedPin) }
            },
        )

        SecurityHint(
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }
}
