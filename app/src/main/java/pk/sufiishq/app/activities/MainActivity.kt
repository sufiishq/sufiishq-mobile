package pk.sufiishq.app.activities

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import pk.sufiishq.app.ui.screen.MainView
import pk.sufiishq.app.ui.theme.SufiIshqTheme

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        installSplashScreen()

        setContent {
            SufiIshqTheme {
                MainView(homeViewModel)
            }
        }

        super.onCreate(savedInstanceState)
    }
}
