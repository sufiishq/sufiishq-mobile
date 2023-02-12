package pk.sufiishq.app.ui.screen.admin

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import io.github.esentsov.PackagePrivate
import pk.sufiishq.app.R
import pk.sufiishq.app.data.providers.AdminSettingsDataProvider
import pk.sufiishq.app.utils.optString
import pk.sufiishq.aurora.components.SIButton
import pk.sufiishq.aurora.components.SIHeightSpace
import pk.sufiishq.aurora.components.SIImage
import pk.sufiishq.aurora.layout.SIBox
import pk.sufiishq.aurora.layout.SIColumn

@PackagePrivate
@Composable
fun AdminSignIn(
    adminSettingsDataProvider: AdminSettingsDataProvider
) {

    val context = LocalContext.current

    SIBox(modifier = Modifier.fillMaxSize()) {
        SIColumn(horizontalAlignment = Alignment.CenterHorizontally) {

            SIImage(
                resId = R.drawable.authentication_full, tintColor = it
            )
            SIHeightSpace(value = 12)
            SIButton(
                text = optString(R.string.label_verify_identity),
                leadingIcon = R.drawable.key,
                onClick = {
                    adminSettingsDataProvider.signIn(context as ComponentActivity)
                }
            )
        }
    }
}