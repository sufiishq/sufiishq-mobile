package pk.sufiishq.app.ui.screen.applock

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.esentsov.PackagePrivate
import kotlinx.coroutines.launch
import pk.sufiishq.app.data.providers.AppLockController
import pk.sufiishq.aurora.layout.SIBox

@PackagePrivate
@Composable
fun ChangeSecurityQuestion(
    appLockController: AppLockController,
    scaffoldState: ScaffoldState
) {

    val coroutineScope = rememberCoroutineScope()

    SIBox(modifier = Modifier.fillMaxSize()) {

    SecurityQuestionList(
            modifier = Modifier.align(Alignment.TopCenter),
            scaffoldState = scaffoldState,
            headerButtonClick = {
                appLockController.gotoSetting()
            },
            onDoneClick = {
                coroutineScope.launch {
                    appLockController.updateSecurityQuestion(it)
                }
            }
        )

        SecurityHint(
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}