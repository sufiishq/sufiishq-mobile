package pk.sufiishq.app.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*
import org.jetbrains.annotations.Range
import timber.log.Timber

fun getTodayDate(pattern: String = "dd-MM-yyyy"): String {
    val time = Calendar.getInstance().time
    val dateFormatter = SimpleDateFormat(pattern, Locale.getDefault())
    return dateFormatter.format(time).also { Timber.d(it) }
}

fun getCurrentTime(pattern: String = "s-m-H"): String = getTodayDate(pattern)

fun getCurrentYear(): Int {
    return getTodayDate().substringAfterLast("-").toInt()
}

fun getCurrentMonth(): Int {
    return getTodayDate().substringAfter("-").substringBefore("-").toInt()
}

fun getCurrentDay(): Int {
    return getTodayDate().substringBefore("-").toInt()
}

fun getCurrentHour(): Int {
    return getCurrentTime().substringAfterLast("-").toInt()
}

fun getCurrentMinute(): Int {
    return getCurrentTime().substringAfter("-").substringBefore("-").toInt()
}

fun getCurrentSecond(): Int {
    return getCurrentTime().substringBefore("-").toInt()
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
fun @Range(from = 0, to = 11) Int.shortMonthName(): String {
    return Calendar.getInstance().apply {
        set(Calendar.MONTH, this@shortMonthName)
    }.let {
        SimpleDateFormat("MMM").format(it.time)
    }
}

fun Calendar.nextYear(count: Int = 1): Calendar {
    add(Calendar.YEAR, count)
    return this
}