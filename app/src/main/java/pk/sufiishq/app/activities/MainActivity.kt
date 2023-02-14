package pk.sufiishq.app.activities

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import pk.sufiishq.app.core.firebase.MaintenanceManager
import pk.sufiishq.app.ui.main.MainHostView
import pk.sufiishq.aurora.theme.Aurora


@AndroidEntryPoint
class MainActivity : BaseActivity() {

    @Inject
    lateinit var maintenanceManager: MaintenanceManager

    override fun onCreate(savedInstanceState: Bundle?) {

        installSplashScreen()
        super.onCreate(savedInstanceState)

        setContent {
            Aurora {
                MainHostView(maintenanceManager)
            }
        }
    }

}
