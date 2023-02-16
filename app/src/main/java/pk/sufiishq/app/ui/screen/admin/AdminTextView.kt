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

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.esentsov.PackagePrivate
import pk.sufiishq.app.ui.components.OutlinedTextField
import pk.sufiishq.aurora.components.SIHeightSpace

@PackagePrivate
@Composable
fun AdminTextView(
    modifier: Modifier = Modifier,
    label: String,
    text: String,
    maxLength: Int = 10000,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onValueChange: (value: String) -> Unit,
) {
    SIHeightSpace(value = 8)
    OutlinedTextField(
        modifier = modifier,
        value = text,
        label = label,
        onValueChange = onValueChange,
        keyboardOptions = keyboardOptions,
        maxLength = maxLength,
    )
}
