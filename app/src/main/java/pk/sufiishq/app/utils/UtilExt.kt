package pk.sufiishq.app.utils

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.LiveData
import java.text.SimpleDateFormat
import java.util.*
import pk.sufiishq.app.R
import pk.sufiishq.app.SufiIshqApp
import pk.sufiishq.app.helpers.ScreenType
import pk.sufiishq.app.models.Kalam
import pk.sufiishq.aurora.models.DataMenuItem

fun getApp(): SufiIshqApp = SufiIshqApp.getInstance()

fun <T> State<T?>.optValue(default: T) = value ?: default

fun <T> LiveData<T>.optValue(default: T): T {
    return value ?: default
}

fun String.formatDateAs(format: String = "d MMM, yyyy", prefix: String = ""): String {
    return when {
        this.isEmpty() -> ""
        this.length == 4 -> prefix + this
        else -> {
            val date = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse(this)
            prefix + SimpleDateFormat(format, Locale.getDefault()).format(date!!)
        }
    }
}

@Composable
fun <T> rem(value: T): MutableState<T> {
    return remember { mutableStateOf(value) }
}

fun List<DataMenuItem>.filterItems(kalam: Kalam, trackType: String? = null): List<DataMenuItem> {

    return filter {
        when (it.resId) {
            R.drawable.ic_round_favorite_24 -> {
                kalam.isFavorite == 0
            }
            R.drawable.ic_round_favorite_border_24 -> {
                kalam.isFavorite == 1
            }
            R.drawable.ic_round_cloud_download_24 -> {
                kalam.offlineSource.isEmpty()
            }
            R.drawable.ic_round_share_24 -> {
                kalam.onlineSource.isNotEmpty()
            }
            R.drawable.ic_round_call_split_24 -> {
                trackType == ScreenType.Tracks.DOWNLOADS
            }
            R.drawable.ic_outline_delete_24 -> {
                if (trackType == ScreenType.Tracks.ALL) {
                    kalam.onlineSource.isEmpty()
                } else true
            }
            R.drawable.ic_round_playlist_add_24 -> {
                trackType != ScreenType.Tracks.PLAYLIST
            }
            else -> true
        }
    }

}

fun String.maxLength(length: Int, endWith: String): String {

    return if (this.length > length) {
        this.substring(0, length) + endWith
    } else this
}

fun quickToast(msg: String, vararg args: Any?) {
    getApp().toastShort(msg.format(*args))
}

fun quickToast(@StringRes resId: Int, vararg args: Any?) {
    quickToast(getString(resId, *args))
}

fun getString(@StringRes resId: Int, vararg args: Any?): String {
    return getApp().getString(resId).format(*args)
}
