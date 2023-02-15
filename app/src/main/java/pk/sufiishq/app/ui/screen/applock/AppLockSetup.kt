package pk.sufiishq.app.ui.screen.applock

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import io.github.esentsov.PackagePrivate
import kotlinx.coroutines.launch
import pk.sufiishq.app.R
import pk.sufiishq.app.data.controller.AppLockController
import pk.sufiishq.app.utils.optString
import pk.sufiishq.aurora.components.SIButton
import pk.sufiishq.aurora.components.SIHeightSpace
import pk.sufiishq.aurora.components.SIImage
import pk.sufiishq.aurora.components.SIText
import pk.sufiishq.aurora.components.SIWidthSpace
import pk.sufiishq.aurora.components.TextSize
import pk.sufiishq.aurora.layout.SIBox
import pk.sufiishq.aurora.layout.SIColumn
import pk.sufiishq.aurora.layout.SIRow
import pk.sufiishq.aurora.theme.AuroraColor

@PackagePrivate
@Composable
fun AppLockSetup(
    appLockController: AppLockController
) {

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    SIBox(modifier = Modifier.fillMaxSize()) {

        SIRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.TopCenter),
            bgColor = AuroraColor.Background,
            radius = 4
        ) { textColor ->
            SIImage(resId = R.drawable.shield)
            SIWidthSpace(value = 12)
            SIColumn(modifier = Modifier.fillMaxWidth()) {
                SIText(
                    text = optString(R.string.title_app_lock),
                    textColor = textColor,
                    fontWeight = FontWeight.Bold
                )
                SIHeightSpace(value = 12)
                SIText(
                    text = optString(R.string.detail_setup_app_lock),
                    textColor = textColor,
                    textSize = TextSize.Small
                )
                SIHeightSpace(value = 12)
                SIBox(modifier = Modifier.fillMaxWidth()) {
                    SIButton(
                        modifier = Modifier.align(Alignment.BottomEnd),
                        text = optString(R.string.label_setup),
                        onClick = {
                            coroutineScope.launch {
                                appLockController.setupBiometricOrPin(context as FragmentActivity)
                            }
                        }
                    )
                }
            }
        }

        SIImage(
            modifier = Modifier
                .align(Alignment.Center)
                .alpha(0.05f),
            resId = R.drawable.shield_full,
        )
    }
}