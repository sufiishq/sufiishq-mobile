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

package pk.sufiishq.app.feature.help.resolver

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.json.JSONObject
import pk.sufiishq.app.di.qualifier.IoDispatcher
import pk.sufiishq.app.feature.help.HelpContentTransformer
import pk.sufiishq.app.feature.help.api.HelpContentService
import pk.sufiishq.app.feature.help.di.qualifier.HelpJson
import pk.sufiishq.app.feature.help.model.HelpContent
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

private const val HELP_URL =
    "https://raw.githubusercontent.com/sufiishq/sufiishq-mobile/master/app/src/main/assets/help/help.json"

class OnlineHelpContentResolver
@Inject
constructor(
    @IoDispatcher private val dispatcher: CoroutineContext,
    @HelpJson private val helpJson: JSONObject,
    private val helpContentService: HelpContentService,
    private val transformer: HelpContentTransformer,
) : HelpContentResolver {

    override fun resolve(): Flow<List<HelpContent>> {
        return flow {
            try {
                helpContentService
                    .getHelp(HELP_URL)
                    .execute()
                    .takeIf { it.isSuccessful }
                    ?.let {
                        emit(transformer.transform(JSONObject(it.body()!!.string())))
                    } ?: emit(transformer.transform(helpJson))
            } catch (ex: Exception) {
                Timber.e(ex)
                emit(transformer.transform(helpJson))
            }
        }
            .flowOn(dispatcher)
    }
}
