package pk.sufiishq.app.ui.screen.applock

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.esentsov.PackagePrivate
import pk.sufiishq.app.R
import pk.sufiishq.app.data.providers.AppLockController
import pk.sufiishq.app.utils.optString
import pk.sufiishq.aurora.components.SIHeightSpace
import pk.sufiishq.aurora.components.SIText
import pk.sufiishq.aurora.components.TextSize
import pk.sufiishq.aurora.layout.SIBox
import pk.sufiishq.aurora.layout.SIColumn

@PackagePrivate
@Composable
fun ChangePinPrompt(
    appLockController: AppLockController,
    cameFromForgotPin: Boolean
) {
    SIBox(modifier = Modifier.fillMaxSize()) {

        if (!cameFromForgotPin) {
            AppLockHeader(
                modifier = Modifier.align(Alignment.TopCenter),
                onButtonClick = {
                    appLockController.gotoSetting()
                }
            )
        }

        SIColumn(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SIText(
                text = optString(R.string.label_set_pin),
                textColor = it,
                textSize = TextSize.Large
            )
            SIHeightSpace(value = 24)
            AppLockKeyboardWithPinView(
                onPinGenerated = { generatedPin ->
                    appLockController.changePinGenerated(generatedPin, cameFromForgotPin)
                }
            )
        }
    }
}