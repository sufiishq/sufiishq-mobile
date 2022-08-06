package pk.sufiishq.app.ui.components

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import pk.sufiishq.app.R
import pk.sufiishq.app.activities.MainActivity
import pk.sufiishq.app.utils.DARK_THEME
import pk.sufiishq.app.utils.getFromStorage
import pk.sufiishq.app.utils.isDeviceSupportDarkMode
import pk.sufiishq.app.utils.putInStorage

@Composable
fun ThemeChangeButton() {
    if (!isDeviceSupportDarkMode()) {
        val context = LocalContext.current
        IconButton(onClick = {
            val isNightTheme = DARK_THEME.getFromStorage(false)
            DARK_THEME.putInStorage(!isNightTheme)
            if (context is MainActivity) {
                context.recreate()
            }
        }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_round_dark_mode_24),
                contentDescription = null,
                tint = MaterialTheme.colors.primary
            )
        }
    }
}