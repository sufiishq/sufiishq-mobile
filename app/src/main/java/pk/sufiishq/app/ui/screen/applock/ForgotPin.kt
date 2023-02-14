package pk.sufiishq.app.ui.screen.applock

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.esentsov.PackagePrivate
import kotlinx.coroutines.launch
import pk.sufiishq.app.data.providers.AppLockController
import pk.sufiishq.app.models.SecurityQuestion
import pk.sufiishq.app.ui.components.OutlinedTextField
import pk.sufiishq.app.utils.rem
import pk.sufiishq.aurora.components.SIButton
import pk.sufiishq.aurora.components.SIHeightSpace
import pk.sufiishq.aurora.components.SIText
import pk.sufiishq.aurora.layout.SIBox
import pk.sufiishq.aurora.layout.SIRow
import pk.sufiishq.aurora.theme.AuroraColor

@PackagePrivate
@Composable
fun ForgotPin(
    appLockController: AppLockController,
    scaffoldState: ScaffoldState,
    securityQuestion: SecurityQuestion
) {

    val coroutineScope = rememberCoroutineScope()
    val answer = rem("")

    SIRow(
        modifier = Modifier.fillMaxWidth(),
        bgColor = AuroraColor.Background,
        radius = 4,
        padding = 12,
    ) {
        SIText(
            text = "Q. ${securityQuestion.question}",
            textColor = it
        )
    }
    SIHeightSpace(value = 12)
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
            text = "Done",
            onClick = {
                coroutineScope.launch {
                    if (answer.value.trim().isEmpty()) {
                        scaffoldState.snackbarHostState.showSnackbar("Answer should not be empty")
                    } else if (answer.value.trim().lowercase() != securityQuestion.answer.trim()
                            .lowercase()
                    ) {
                        scaffoldState.snackbarHostState.showSnackbar("Answer not matched")
                    } else {
                        appLockController.userWantChangePin(cameFromForgotPin = true)
                    }
                }
            }
        )
    }
}