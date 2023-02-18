/*
 * Copyright 2022-2023 SufiIshq
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pk.sufiishq.app.utils

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.LiveData
import pk.sufiishq.app.R
import pk.sufiishq.app.SufiIshqApp
import pk.sufiishq.app.feature.admin.model.Highlight
import pk.sufiishq.app.feature.applock.model.AutoLockDuration
import pk.sufiishq.app.feature.kalam.model.Kalam
import pk.sufiishq.app.helpers.ScreenType
import pk.sufiishq.app.utils.extention.toastShort
import pk.sufiishq.aurora.models.DataMenuItem
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Locale

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
                } else {
                    true
                }
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
    } else {
        this
    }
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

fun Highlight?.contactsAsListPair(): List<Pair<String, String>>? {
    return this?.contacts
        ?.map { it.value.toList().map { data -> data.second } }
        ?.flatten()
        ?.let {
            val size = it.size
            val cut = (size + 1) / 2
            val first = it.subList(0, cut)
            val second = it.subList(cut, size)
            first.zip(second)
        }
}

fun String.addCharAtIndex(char: Char, index: Int): String {
    return tryWithDefault(this) { StringBuilder(this).apply { insert(index, char) }.toString() }
}

suspend fun <T> tryAsyncWithDefault(default: T, block: suspend () -> T): T {
    return try {
        block()
    } catch (ex: Exception) {
        Timber.e(ex)
        default
    }
}

fun <T> tryWithDefault(default: T, block: () -> T): T {
    return try {
        block()
    } catch (ex: Exception) {
        Timber.e(ex)
        default
    }
}

fun instantAutoLockDuration(label: String) =
    AutoLockDuration(
        code = 0,
        label = label,
        durationInMillis = 0,
    )