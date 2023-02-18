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

package pk.sufiishq.app.core.hijridate.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import pk.sufiishq.app.core.hijridate.resolver.HijriDateResolver
import pk.sufiishq.app.di.qualifier.IoDispatcher
import pk.sufiishq.app.core.hijridate.model.HijriDate
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class HijriDateRepository
@Inject
constructor(
    @IoDispatcher private val dispatcher: CoroutineContext,
    private val hijriDateResolver: HijriDateResolver,
) {
    fun getHijriDate(): Flow<HijriDate?> =
        flow { emit(hijriDateResolver.resolve()) }.flowOn(dispatcher)
}
