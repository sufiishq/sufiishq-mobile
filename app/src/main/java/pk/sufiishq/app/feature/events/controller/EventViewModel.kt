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

package pk.sufiishq.app.feature.events.controller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import pk.sufiishq.app.di.qualifier.IoDispatcher
import pk.sufiishq.app.feature.events.EventController
import pk.sufiishq.app.feature.events.data.repository.EventRepository
import pk.sufiishq.app.feature.events.model.Event
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class EventViewModel @Inject constructor(
    @IoDispatcher private val dispatcher: CoroutineContext,
    private val eventRepository: EventRepository,
) : ViewModel(), EventController {

    private var eventPagingData: Flow<PagingData<Event>>? = null

    private fun pagingSource(): () -> PagingSource<Int, Event> {
        return { eventRepository.getAllWithPaging() }
    }

    override fun loadAllEvents(): Flow<PagingData<Event>> {
        return eventPagingData ?: Pager(
            PagingConfig(pageSize = 20),
            pagingSourceFactory = pagingSource(),
        )
            .flow
            .also {
                eventPagingData = it
            }
    }

    override fun toggleNotification(event: Event) {
        viewModelScope.launch(dispatcher) {
            eventRepository.update(
                event.copy(
                    enableAlert = !event.enableAlert,
                ),
            )
        }
    }
}
