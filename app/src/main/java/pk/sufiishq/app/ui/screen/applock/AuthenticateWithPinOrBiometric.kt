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
import pk.sufiishq.app.data.providers.AppLockController
import pk.sufiishq.aurora.components.SIButton
import pk.sufiishq.aurora.components.SIHeightSpace
import pk.sufiishq.aurora.components.SIText
import pk.sufiishq.aurora.components.TextSize
import pk.sufiishq.aurora.layout.SIBox
import pk.sufiishq.aurora.layout.SIColumn

@PackagePrivate
@Composable
fun AuthenticateWithPinOrBiometric(
    appLockController: AppLockController,
    validPin: String
) {
    SIBox(modifier = Modifier.fillMaxSize()) {

        SIColumn(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SIButton(
                text = "Forgot PIN",
                onClick = {
                    appLockController.forgotPin()
                }
            )
            SIHeightSpace(value = 36)
            SIText(text = "Verify PIN", textColor = it, textSize = TextSize.Large)
            SIHeightSpace(value = 24)
            AppLockKeyboardWithPinView(
                onPinGenerated = {
                    appLockController.gotoSetting()
                },
                validPin = validPin
            )
        }
    }

    val fragmentActivity = LocalContext.current as FragmentActivity
    LaunchedEffect(Unit) {
        appLockController.promptBiometric(fragmentActivity)
    }
}