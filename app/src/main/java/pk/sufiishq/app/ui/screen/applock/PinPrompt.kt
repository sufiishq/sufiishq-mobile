package pk.sufiishq.app.ui.screen.applock

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.esentsov.PackagePrivate
import pk.sufiishq.app.data.providers.AppLockController
import pk.sufiishq.aurora.components.SIHeightSpace
import pk.sufiishq.aurora.components.SIText
import pk.sufiishq.aurora.components.TextSize
import pk.sufiishq.aurora.layout.SIBox
import pk.sufiishq.aurora.layout.SIColumn

@PackagePrivate
@Composable
fun PinPrompt(
    appLockController: AppLockController
) {
    SIBox(modifier = Modifier.fillMaxSize()) {

        AppLockHeader(
            modifier = Modifier.align(Alignment.TopCenter),
            onButtonClick = {
                appLockController.cancelFlow()
            }
        )

        SIColumn(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SIText(text = "Set PIN", textColor = it, textSize = TextSize.Large)
            SIHeightSpace(value = 24)
            AppLockKeyboardWithPinView(
                onPinGenerated = { generatedPin ->
                    appLockController.pinGenerated(generatedPin)
                }
            )
        }
    }
}