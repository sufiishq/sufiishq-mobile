package pk.sufiishq.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import pk.sufiishq.app.R
import pk.sufiishq.app.activities.MainActivity
import pk.sufiishq.app.utils.*

@Composable
fun ThemeChangeButton(
    modifier: Modifier
) {
    if (isDeviceSupportDarkMode().not()) {

        val context = LocalContext.current
        val matColors by rem(MaterialTheme.colors)

        Box(
            modifier = modifier
                .clip(CircleShape)
                .background(matColors.primaryVariant),
        ) {
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
                    tint = matColors.primary
                )
            }
        }
    }
}