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

package pk.sufiishq.app.feature.personalize.controller

import androidx.lifecycle.LiveData
import pk.sufiishq.app.feature.personalize.model.LogoPath
import pk.sufiishq.app.feature.personalize.model.Personalize
import pk.sufiishq.app.feature.theme.model.AutoChangeColorDuration

interface PersonalizeController {

    suspend fun isAutoDownloadKalam(): Boolean
    fun setAutoDownloadKalam(isEnable: Boolean)
    suspend fun resolveLogo(logoPath: LogoPath): LogoPath?
    fun get(): LiveData<Personalize?>
    fun reset()
    fun update(personalize: Personalize)
}
