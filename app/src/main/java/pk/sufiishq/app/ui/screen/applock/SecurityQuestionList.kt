package pk.sufiishq.app.ui.screen.applock

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.esentsov.PackagePrivate
import kotlinx.coroutines.launch
import pk.sufiishq.app.R
import pk.sufiishq.app.models.SecurityQuestion
import pk.sufiishq.app.ui.components.OutlinedTextField
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
    doneButtonLabel: String = "Done",
    onDoneClick: (item: SecurityQuestion) -> Unit,
    headerButtonClick: () -> Unit
) {

    val coroutineScope = rememberCoroutineScope()
    val isExpanded = rem(false)
    val data = rem(getList())
    val selection = rem(data.value[0])
    val answer = rem("")

    SIColumn(modifier = modifier.fillMaxWidth()) {

        AppLockHeader(
            onButtonClick = headerButtonClick
        )

        SIHeightSpace(value = 12)

        SIDataRow(
            title = selection.value.label,
            trailingIcon = R.drawable.baseline_arrow_drop_down_24,
            onClick = {
                isExpanded.value = true
            }
        )

        SIHeightSpace(value = 12)

        if (selection.value.index != 0) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = "Answer",
                value = answer.value,
                onValueChange = {
                    answer.value = it
                },
                maxLength = 30,
                emptyFieldError = "Answer is required"
            )

            SIBox(modifier = Modifier.fillMaxWidth()) {
                SIButton(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    text = doneButtonLabel,
                    onClick = {
                        if (answer.value.trim().isEmpty()) {
                            coroutineScope.launch {
                                scaffoldState.snackbarHostState.showSnackbar("Answer should not be empty")
                            }
                        } else {
                            selection.value.answer = answer.value
                            onDoneClick(selection.value)
                        }
                    }
                )
            }
        }


        SIPopupMenu(
            isExpanded = isExpanded,
            modifier = Modifier.fillMaxWidth(),
            data = data.value,
            onClick = {
                selection.value = it as SecurityQuestion
            }
        )
    }
}

private fun getList(): List<SecurityQuestion> {
    return listOf(
        SecurityQuestion(0, "Select Security Question"),
        SecurityQuestion(1, "What is your mother's maiden name?"),
        SecurityQuestion(2, "What is the name of your first pet?"),
        SecurityQuestion(3, "What was your first car?"),
        SecurityQuestion(4, "What elementary school did you attend?"),
        SecurityQuestion(5, "What is the name of the town where you were born?"),
        SecurityQuestion(6, "When you were young, what did you want to be when you grew up?"),
        SecurityQuestion(7, "Who was your childhood hero?"),
        SecurityQuestion(8, "Where was your best family vacation as a kid?"),
        SecurityQuestion(9, "What was your favorite subject in high school?"),
        SecurityQuestion(10, "What is your employee ID number?"),
        SecurityQuestion(11, "What is your favorite color?"),
    )
}