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

package pk.sufiishq.app.feature.storage

import com.google.gson.Gson
import pk.sufiishq.app.di.qualifier.SecureSharedPreferences
import pk.sufiishq.app.feature.kalam.model.KalamInfo
import pk.sufiishq.app.utils.LAST_PLAY_KALAM
import javax.inject.Inject

class LastKalamPlayLiveData
@Inject
constructor(
    private val gson: Gson,
    @SecureSharedPreferences private val keyValueStorage: KeyValueStorage,
) : SPLiveData<KalamInfo?>(keyValueStorage.getSharedPreferences(), LAST_PLAY_KALAM, null) {

    override fun getValueFromPreferences(key: String, defValue: KalamInfo?): KalamInfo? {
        return keyValueStorage
            .get(key, "")
            .takeIf { it.isNotEmpty() }
            ?.let { gson.fromJson(it, KalamInfo::class.java) }
    }
}
