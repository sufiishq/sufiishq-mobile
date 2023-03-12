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

package pk.sufiishq.app.feature.events.transformation

import org.joda.time.Days
import org.joda.time.LocalDate
import org.joda.time.chrono.ISOChronology
import org.joda.time.chrono.IslamicChronology
import pk.sufiishq.app.feature.events.model.Event
import java.util.Calendar
import javax.inject.Inject

class EventTransformer @Inject constructor() {

    fun transform(events: List<Event>): List<Event> {
        val transformedList = mutableListOf<Event>()

        events.forEach { event ->

            val iso = ISOChronology.getInstanceUTC()
            val hijri = IslamicChronology.getInstance()
            val todayHijriDate = LocalDate(Calendar.getInstance(), hijri).toDateTimeAtStartOfDay()

            val (hijriMonth, hijriDay) = event.hijriMonthAndDay.split("-").map { it.toInt() }

            var eventHijriDate = LocalDate(todayHijriDate.year, hijriMonth, hijriDay, hijri).toDateTimeAtStartOfDay()

            if (todayHijriDate.millis > eventHijriDate.millis) {
                eventHijriDate = eventHijriDate.plusYears(1)
            }

            val isoDate = eventHijriDate.withChronology(iso).toLocalDate()

            transformedList.add(
                event.copy(
                    date = isoDate.toString(),
                    hijriDate = "${event.hijriDate.split(",").firstOrNull() ?: event.hijriDate}, ${eventHijriDate.year}",
                    remainingDays = Days.daysBetween(todayHijriDate, eventHijriDate).days,
                ),
            )
        }

        return transformedList
    }
}
