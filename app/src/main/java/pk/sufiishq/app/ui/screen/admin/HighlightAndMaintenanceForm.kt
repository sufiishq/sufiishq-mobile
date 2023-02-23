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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import io.github.esentsov.PackagePrivate
import pk.sufiishq.app.feature.admin.controller.AdminController
import pk.sufiishq.app.ui.components.OutlinedTextField
import pk.sufiishq.app.ui.components.dialogs.ConfirmDialogParam
import pk.sufiishq.app.ui.components.dialogs.ConfirmationDialog
import pk.sufiishq.app.utils.TextRes
import pk.sufiishq.app.utils.extention.nextYear
import pk.sufiishq.app.utils.extention.optString
import pk.sufiishq.app.utils.getString
import pk.sufiishq.app.utils.optValue
import pk.sufiishq.app.utils.rem
import pk.sufiishq.aurora.components.SIButton
import pk.sufiishq.aurora.components.SIHeightSpace
import pk.sufiishq.aurora.components.SIIcon
import pk.sufiishq.aurora.components.SIText
import pk.sufiishq.aurora.components.SIWidthSpace
import pk.sufiishq.aurora.components.TextSize
import pk.sufiishq.aurora.layout.SICard
import pk.sufiishq.aurora.layout.SIColumn
import pk.sufiishq.aurora.layout.SIRow
import pk.sufiishq.aurora.theme.AuroraColor
import java.util.Calendar

@PackagePrivate
@Composable
fun HighlightAndMaintenanceForm(
    adminController: AdminController,
    isDeveloper: Boolean,
) {
    val context = LocalContext.current
    val title = adminController.getTitle().observeAsState().optValue("")
    val detail = adminController.getDetail().observeAsState().optValue("")
    val contacts = adminController.getContacts().observeAsState()
    val confirmDialog = rem<ConfirmDialogParam?>(null)
    val saveBtnText = rem("")
    saveBtnText.value = optString(TextRes.label_save)

    LaunchedEffect(Unit) { adminController.fetchHighlight() }

    ConfirmationDialog(state = confirmDialog)

    SIColumn(
        modifier =
        Modifier
            .verticalScroll(rememberScrollState())
            .padding(start = 12.dp, top = 12.dp, end = 12.dp, bottom = 18.dp),
    ) {
        adminController.highlightStatus().observeAsState().value?.let { status ->
            saveBtnText.value = optString(TextRes.label_update)
            SICard(
                bgColor = status.bgColor,
                modifier = Modifier.fillMaxWidth(),
            ) {
                SIRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    SIRow(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        SIIcon(resId = status.leadingIcon)
                        SIWidthSpace(value = 8)
                        SIText(
                            text = status.label,
                            textColor = AuroraColor.White,
                            textSize = TextSize.Small,
                        )
                    }
                    SIButton(
                        bgColor = AuroraColor.White,
                        text = optString(TextRes.label_remove),
                        onClick = {
                            confirmDialog.value =
                                ConfirmDialogParam(getString(TextRes.msg_confirm_delete_highlight)) {
                                    adminController.deleteHighlight()
                                }
                        },
                    )
                }
            }
            SIHeightSpace(value = 12)
        }

        AdminHeader(optString(TextRes.title_highlight))

        StartDateTimeView(adminController)
        EndDateTimeView(adminController)

        AdminTextView(
            modifier = Modifier.fillMaxWidth(),
            label = optString(TextRes.label_title),
            text = title,
            maxLength = 256,
            onValueChange = adminController::setTitle,
        )

        DetailView(
            text = detail,
            onValueChange = adminController::setDetail,
        )

        contacts.value?.forEachIndexed { index, pair ->
            val name = rem("")
            val number = rem("")
            name.value = pair?.first ?: ""
            number.value = pair?.second ?: ""

            HighlightContactRow(
                label = optString(TextRes.dynamic_contact, index.plus(1)),
                contactName = name.value,
                contactNumber = number.value,
                onValueChange = { updatedName, updatedNumber ->
                    name.value = updatedName
                    number.value = updatedNumber
                    contacts.value!![index] = Pair(updatedName, updatedNumber)
                },
            )
        }

        SIHeightSpace(value = 12)
        SIRow(modifier = Modifier.fillMaxWidth()) {
            SIButton(
                modifier = Modifier.weight(1f),
                text = optString(TextRes.label_logout),
                onClick = {
                    confirmDialog.value =
                        ConfirmDialogParam(getString(TextRes.msg_confirm_logout)) {
                            adminController.signOut(context as ComponentActivity)
                        }
                },
            )
            SIWidthSpace(value = 8)
            SIButton(
                modifier = Modifier.weight(2f),
                text = saveBtnText.value,
                onClick = {
                    confirmDialog.value =
                        ConfirmDialogParam(
                            getString(
                                TextRes.dynamic_confirm_save_highlight,
                                saveBtnText.value,
                            ),
                        ) {
                            adminController.addOrUpdateHighlight()
                        }
                },
            )
        }

        if (isDeveloper) MaintenanceView(adminController)
    }
}

@Composable
private fun DetailView(
    text: String,
    onValueChange: (value: String) -> Unit,
) {
    SIHeightSpace(value = 8)
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        value = text,
        label = optString(TextRes.label_detail),
        maxLines = 20,
        singleLine = false,
        keyboardOptions = KeyboardOptions.Default,
        onValueChange = onValueChange,
    )
}

@Composable
private fun StartDateTimeView(
    adminController: AdminController,
) {
    val minDate = rem(Calendar.getInstance())
    val maxDate = rem(Calendar.getInstance().nextYear())
    val selectedDate = adminController.startDate().observeAsState()
    val startTime = adminController.startTime().observeAsState()

    HighlightDateTimeView(
        label = optString(TextRes.label_start),
        selectedDate = selectedDate,
        selectedTime = startTime,
        minDate = minDate,
        maxDate = maxDate,
        onDateChanged = adminController::startDateChanged,
        onTimeChanged = adminController::startTimeChanged,
    )
}

@Composable
private fun EndDateTimeView(
    adminController: AdminController,
) {
    val selectedDate = adminController.endDate().observeAsState()
    val minDate = adminController.minEndDate().observeAsState()
    val maxDate = rem(Calendar.getInstance().nextYear(2))
    val endTime = adminController.endTime().observeAsState()

    HighlightDateTimeView(
        label = optString(TextRes.label_end),
        selectedDate = selectedDate,
        selectedTime = endTime,
        minDate = minDate,
        maxDate = maxDate,
        onDateChanged = adminController::endDateChanged,
        onTimeChanged = adminController::endTimeChanged,
    )
}
