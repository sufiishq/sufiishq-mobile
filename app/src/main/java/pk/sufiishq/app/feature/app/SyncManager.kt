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

package pk.sufiishq.app.feature.app

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import pk.sufiishq.app.di.qualifier.IoDispatcher
import pk.sufiishq.app.di.qualifier.SharedPreferences
import pk.sufiishq.app.feature.events.worker.EventSyncWorker
import pk.sufiishq.app.feature.events.worker.EventUpdateWorker
import pk.sufiishq.app.feature.occasions.worker.OccasionSyncWorker
import pk.sufiishq.app.feature.storage.KeyValueStorage
import pk.sufiishq.app.feature.theme.controller.ThemeController
import pk.sufiishq.app.feature.theme.model.AutoChangeColorDuration
import pk.sufiishq.app.worker.CacheRemoveWorker
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Singleton
class SyncManager @Inject constructor(
    @ApplicationContext private val context: Context,
    @IoDispatcher private val dispatcher: CoroutineContext,
    @SharedPreferences private val keyValueStorage: KeyValueStorage,
) {

    fun sync(themeController: ThemeController) {
        CoroutineScope(dispatcher).launch {
            CacheRemoveWorker.init(context)
            // FIXME temporary disable event and occasion sync
            //EventUpdateWorker.init(context)
            //EventSyncWorker.init(context, keyValueStorage)
            //OccasionSyncWorker.init(context, keyValueStorage)
            initAutoColorChangeWorker(themeController)
        }
    }

    private suspend fun initAutoColorChangeWorker(themeController: ThemeController) {
        if (!themeController.isAutoChangeColorEnable() && themeController.getActiveAutoColorChangeDuration() == null) {
            themeController.setAutoChangeColor(true, AutoChangeColorDuration.every1Hour())
        }
    }
}
