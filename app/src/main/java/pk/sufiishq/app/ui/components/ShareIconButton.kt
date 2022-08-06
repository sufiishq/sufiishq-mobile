package pk.sufiishq.app.ui.components

import android.content.Intent
import android.content.Intent.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import pk.sufiishq.app.R

@Composable
fun ShareIconButton() {
    val context = LocalContext.current
    IconButton(onClick = {
        val appName = context.getString(R.string.app_name)
        Intent(ACTION_SEND).apply {
            type = "text/plain"
            putExtra(EXTRA_SUBJECT, appName)
            putExtra(EXTRA_TEXT, "https://play.google.com/store/apps/details?id=pk.sufiishq.app")
        }.also {
            context.startActivity(createChooser(it, "Share $appName"))
        }
    }) {
        Icon(
            painter = painterResource(id = R.drawable.ic_round_share_24),
            contentDescription = null,
            tint = MaterialTheme.colors.primary
        )
    }
}