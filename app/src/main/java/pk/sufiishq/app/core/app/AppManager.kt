package pk.sufiishq.app.core.app

import android.content.Intent
import androidx.activity.ComponentActivity
import javax.inject.Inject
import pk.sufiishq.app.R
import pk.sufiishq.app.models.Kalam
import pk.sufiishq.app.utils.getString
import pk.sufiishq.app.utils.metaInfo

class AppManager @Inject constructor() {

    fun shareKalam(kalam: Kalam, componentActivity: ComponentActivity) {

        val appName = componentActivity.getString(R.string.app_name)
        val shareText = getString(R.string.msg_kalam_meta_info, kalam.title, kalam.metaInfo())
        Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, kalam.title)
            putExtra(Intent.EXTRA_TEXT, shareText)
        }.also {
            componentActivity.startActivity(
                Intent.createChooser(
                    it,
                    getString(R.string.dynamic_share_kalam, appName)
                )
            )
        }
    }

    fun shareApp(activity: ComponentActivity) {
        val appName = activity.getString(R.string.app_name)
        Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, appName)
            putExtra(
                Intent.EXTRA_TEXT,
                "https://play.google.com/store/apps/details?id=pk.sufiishq.app"
            )
        }.also {
            activity.startActivity(
                Intent.createChooser(
                    it,
                    getString(R.string.dynamic_share_kalam, appName)
                )
            )
        }
    }
}