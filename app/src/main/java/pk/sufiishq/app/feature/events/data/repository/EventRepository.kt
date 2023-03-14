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

package pk.sufiishq.app.feature.events.data.repository

import androidx.paging.PagingSource
import org.json.JSONObject
import pk.sufiishq.app.feature.events.api.EventService
import pk.sufiishq.app.feature.events.data.dao.EventDao
import pk.sufiishq.app.feature.events.model.Event
import pk.sufiishq.app.feature.events.transformation.EventTransformer
import pk.sufiishq.app.utils.asObjectList
import javax.inject.Inject

class EventRepository @Inject constructor(
    private val eventDao: EventDao,
    private val eventService: EventService,
    private val eventTransformer: EventTransformer,
) {

    fun getAllWithPaging(): PagingSource<Int, Event> {
        return eventDao.getAllWithPaging()
    }

    suspend fun getAll(): List<Event> {
        return eventDao.getAll()
    }

    suspend fun getUpcomingEvents(): List<Event> {
        return eventDao.getUpcomingEvents()
    }

    suspend fun update(event: Event) {
        eventDao.update(event)
    }

    suspend fun count(): Int {
        return eventDao.count()
    }

    suspend fun invalidateAllEvents() {
        eventDao.updateAll(
            eventTransformer.transform(
                getAll(),
            ),
        )
    }

    suspend fun fetchAllEvents() {
        eventService
            .fetchAllEvents()
            .execute()
            .takeIf { it.isSuccessful && it.body() != null }
            ?.let { it.body()!!.string() }
            ?.let(::transform)
            ?.apply {
                eventDao.addAll(eventTransformer.transform(this))
            }
    }

    private fun transform(response: String): List<Event> {
        return JSONObject(response)
            .getJSONArray("data")
            .asObjectList()
            .map(::jsonToEvent)
    }

    private fun jsonToEvent(jsonObject: JSONObject): Event {
        return with(jsonObject) {
            Event(
                id = getLong("id"),
                uuid = getString("uuid"),
                title = getString("title"),
                date = "",
                hijriMonthAndDay = getString("hijriMonthAndDay"),
                hijriDate = getString("hijriDate"),
                remainingDays = 0,
                enableAlert = true,
            )
        }
    }
}
