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

package pk.sufiishq.app.utils.extention

import android.annotation.SuppressLint
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

fun getTodayDate(pattern: String = "dd-MM-yyyy"): String {
    val time = Calendar.getInstance().time
    val dateFormatter = SimpleDateFormat(pattern, Locale.getDefault())
    return dateFormatter.format(time).also { Timber.d(it) }
}

@SuppressLint("SimpleDateFormat")
fun timeAs12HoursFormat(hour: Int, minute: Int, am: String, pm: String): String {
    return SimpleDateFormat("hh:mm").let {
        val time = "${"%02d".format(hour)}:${"%02d".format(minute)}"
        val formattedTime = it.format(it.parse(time)!!)
        val amPm = if (formattedTime.equals(time)) am else pm
        "$formattedTime $amPm"
    }
}

@SuppressLint("SimpleDateFormat")
fun Int.shortMonthName(): String {
    return Calendar.getInstance()
        .apply { set(Calendar.MONTH, this@shortMonthName) }
        .let { SimpleDateFormat("MMM").format(it.time) }
}

@SuppressLint("SimpleDateFormat")
fun Int.fullMonthName(): String {
    return Calendar.getInstance()
        .apply { set(Calendar.MONTH, this@fullMonthName) }
        .let { SimpleDateFormat("MMMM").format(it.time) }
}

fun Calendar.nextYear(count: Int = 1): Calendar {
    add(Calendar.YEAR, count)
    return this
}

fun Long.formatTime(pattern: String): String {
    val time = Calendar.getInstance().apply {
        timeInMillis = this@formatTime
    }.time
    val dateFormatter = SimpleDateFormat(pattern, Locale.getDefault())
    return dateFormatter.format(time)
}
