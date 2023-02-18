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

package pk.sufiishq.app.core.help.repository

import kotlinx.coroutines.flow.Flow
import pk.sufiishq.app.core.help.di.qualifier.OnlineResolver
import pk.sufiishq.app.core.help.model.HelpContent
import pk.sufiishq.app.core.help.resolver.HelpContentResolver
import javax.inject.Inject

class HelpContentRepository
@Inject
constructor(
    @OnlineResolver private val helpContentResolver: HelpContentResolver,
) {
    fun loadContent(): Flow<List<HelpContent>> = helpContentResolver.resolve()
}
