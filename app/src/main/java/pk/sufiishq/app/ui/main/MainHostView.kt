package pk.sufiishq.app.ui.main

import android.app.Activity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.launch
import pk.sufiishq.app.R
import pk.sufiishq.app.core.firebase.MaintenanceManager
import pk.sufiishq.app.models.Maintenance
import pk.sufiishq.app.ui.components.ContentBackground
import pk.sufiishq.app.ui.main.player.Player
import pk.sufiishq.app.ui.main.topbar.AboutIconButton
import pk.sufiishq.app.ui.main.topbar.AppBarOverflowMenu
import pk.sufiishq.app.utils.optValue
import pk.sufiishq.app.utils.rem
import pk.sufiishq.aurora.components.SIButton
import pk.sufiishq.aurora.components.SIHeightSpace
import pk.sufiishq.aurora.components.SIText
import pk.sufiishq.aurora.layout.SIBox
import pk.sufiishq.aurora.layout.SIColumn
import pk.sufiishq.aurora.layout.SIDialog
import pk.sufiishq.aurora.layout.SIScaffold
import pk.sufiishq.aurora.widgets.SITopAppBar

@Composable
fun MainHostView(
    maintenanceManager: MaintenanceManager
) {

    val navController = rememberNavController()
    val maintenance = maintenanceManager.getMaintenance().observeAsState().optValue(Maintenance())

    // block the user if the app has under maintenance with strict mode ON
    if (maintenance.activeStatus && maintenance.strictMode) {
        val context = LocalContext.current
        val coroutineScope = rememberCoroutineScope()
        val maintenanceAnimation =
            rememberLottieComposition(LottieCompositionSpec.Asset("animations/lottie_maintenance.json"))

        ContentBackground {
            SIBox(
                modifier = Modifier.fillMaxSize(),
                padding = 12
            ) {
                SIColumn(horizontalAlignment = Alignment.CenterHorizontally) {

                    LottieAnimation(
                        maintenanceAnimation.value,
                        iterations = LottieConstants.IterateForever,
                    )
                    SIText(
                        textAlign = TextAlign.Center,
                        text = "Application is currently under maintenance.",
                        textColor = it
                    )
                    SIHeightSpace(value = 12)
                    SIButton(
                        text = "Exit",
                        onClick = {
                            coroutineScope.launch {
                                (context as Activity).finish()
                            }
                        }
                    )
                }
            }
        }
    }

    // partial block show the simple message but not block
    val showDialog = rem(true)
    if (maintenance.activeStatus && !maintenance.strictMode && showDialog.value) {
        SIDialog(
            title = "Under Maintenance",
            onYesText = "OK",
            onYesClick = {
                showDialog.value = false
            },
            onDismissRequest = {
                showDialog.value = false
            }
        ) {
            SIText(
                text = "Application is currently under maintenance some of the features may not work properly.",
                textColor = it
            )
        }
    }

    // do not show the app ui if the app has active maintenance with strict mode ON
    if (!maintenance.activeStatus || !maintenance.strictMode) {
        SIScaffold(
            topBar = {
                SITopAppBar(
                    navigationIcon = { fgColor ->
                        AboutIconButton(fgColor)
                    },
                    actions = { fgColor ->
                        AppBarOverflowMenu(navController, fgColor)
                    },
                    centerDrawable = R.drawable.hp_logo
                )
            },
            bottomBar = {
                Player()
            }
        ) {
            ContentBackground {
                NavigationHost(navController)
            }
        }
    }
}
