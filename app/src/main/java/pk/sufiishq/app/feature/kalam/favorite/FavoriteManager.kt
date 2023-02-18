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

package pk.sufiishq.app.feature.kalam.favorite

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import pk.sufiishq.app.R
import pk.sufiishq.app.di.qualifier.IoDispatcher
import pk.sufiishq.app.feature.kalam.data.repository.KalamRepository
import pk.sufiishq.app.feature.kalam.model.Kalam
import pk.sufiishq.app.utils.quickToast
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class FavoriteManager
@Inject
constructor(
    private val kalamRepository: KalamRepository,
    @IoDispatcher private val dispatcher: CoroutineContext,
) {

    fun markAsFavorite(kalam: Kalam) {
        updateFavorite(
            kalam.apply { isFavorite = 1 },
            R.string.dynamic_favorite_added,
        )
    }

    fun removeFavorite(kalam: Kalam) {
        updateFavorite(
            kalam.apply { isFavorite = 0 },
            R.string.dynamic_favorite_removed,
        )
    }

    private fun updateFavorite(kalam: Kalam, toastMessageResId: Int) {
        CoroutineScope(dispatcher).launch {
            kalamRepository.update(kalam).also { quickToast(toastMessageResId, kalam.title) }
        }
    }
}
