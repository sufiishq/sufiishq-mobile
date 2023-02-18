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

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import io.github.esentsov.PackagePrivate
import kotlinx.coroutines.launch
import pk.sufiishq.app.R
import pk.sufiishq.app.feature.applock.model.SecurityQuestion
import pk.sufiishq.app.ui.components.OutlinedTextField
import pk.sufiishq.app.utils.extention.optString
import pk.sufiishq.app.utils.getString
import pk.sufiishq.app.utils.rem
import pk.sufiishq.aurora.components.SIButton
import pk.sufiishq.aurora.components.SIHeightSpace
import pk.sufiishq.aurora.layout.SIBox
import pk.sufiishq.aurora.layout.SIColumn
import pk.sufiishq.aurora.widgets.SIDataRow
import pk.sufiishq.aurora.widgets.SIPopupMenu

@PackagePrivate
@Composable
fun SecurityQuestionList(
    modifier: Modifier = Modifier,
    scaffoldState: ScaffoldState,
    doneButtonLabel: String = optString(R.string.label_done),
    onDoneClick: (item: SecurityQuestion) -> Unit,
    headerButtonClick: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val isExpanded = rem(false)
    val data =
        rem(
            stringArrayResource(R.array.array_security_questions).mapIndexed { index, item ->
                SecurityQuestion(index, item)
            },
        )
    val selection = rem(data.value[0])
    val answer = rem("")

    SIColumn(modifier = modifier.fillMaxWidth()) {
        AppLockHeader(
            onButtonClick = headerButtonClick,
        )

        SIHeightSpace(value = 12)

        SIDataRow(
            title = selection.value.label,
            trailingIcon = R.drawable.baseline_arrow_drop_down_24,
            onClick = { isExpanded.value = true },
        )

        SIHeightSpace(value = 12)

        if (selection.value.index != 0) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = optString(R.string.label_answer),
                value = answer.value,
                onValueChange = { answer.value = it },
                maxLength = 30,
                emptyFieldError = optString(R.string.msg_ans_required),
            )

            SIBox(modifier = Modifier.fillMaxWidth()) {
                SIButton(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    text = doneButtonLabel,
                    onClick = {
                        if (answer.value.trim().isEmpty()) {
                            coroutineScope.launch {
                                scaffoldState.snackbarHostState.showSnackbar(
                                    getString(R.string.msg_ans_not_empty),
                                )
                            }
                        } else {
                            selection.value.answer = answer.value
                            onDoneClick(selection.value)
                        }
                    },
                )
            }
        }

        SIPopupMenu(
            isExpanded = isExpanded,
            modifier = Modifier.fillMaxWidth(),
            data = data.value,
            onClick = { selection.value = it as SecurityQuestion },
        )
    }
}
