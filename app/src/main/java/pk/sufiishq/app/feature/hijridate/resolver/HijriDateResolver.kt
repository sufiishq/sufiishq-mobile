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

package pk.sufiishq.app.feature.hijridate.resolver

import com.google.gson.Gson
import kotlinx.coroutines.withContext
import org.json.JSONObject
import pk.sufiishq.app.di.qualifier.IoDispatcher
import pk.sufiishq.app.feature.hijridate.api.HijriDateService
import pk.sufiishq.app.feature.hijridate.model.HijriDate
import pk.sufiishq.app.utils.extention.getFromStorage
import pk.sufiishq.app.utils.extention.getTodayDate
import pk.sufiishq.app.utils.extention.putInStorage
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

const val HIJRI_DATE_HOST = "http://api.aladhan.com/v1/gToH?date=%s"
const val SP_HIJRI_DATE_KEY = "sp_hijri_date"
const val SP_HIJRI_DATA_KEY = "sp_hijri_data"

@Suppress("SimpleRedundantLet")
class HijriDateResolver
@Inject
constructor(
    @IoDispatcher private val dispatcher: CoroutineContext,
    private val hijriDateService: HijriDateService,
    private val gson: Gson,
) {

    suspend fun resolve(): HijriDate? =
        withContext(dispatcher) {
            try {
                loadData()
            } catch (ex: Exception) {
                Timber.e(ex)
                resolveOffline()
            }
        }

    private suspend fun loadData(): HijriDate? {
        return getTodayDate().takeIf(::dateNotMatched)?.let { callApi(getTodayDate()) }
            ?: resolveOffline()
    }

    private suspend fun callApi(currentDate: String): HijriDate {
        val result = hijriDateService.getHijriDate(HIJRI_DATE_HOST.format(currentDate))
        return resolve(JSONObject(result.string()))
    }

    private fun dateNotMatched(currentDate: String): Boolean {
        return SP_HIJRI_DATE_KEY.getFromStorage("") != currentDate
    }

    private fun resolve(jsonObject: JSONObject): HijriDate {
        return transform(jsonObject).let(::hijriToJson).let(::save)
    }

    private fun resolveOffline(): HijriDate? {
        return SP_HIJRI_DATA_KEY.getFromStorage("").takeIf { it.isNotEmpty() }?.let(::jsonToHijri)
    }

    private fun hijriToJson(hijriDate: HijriDate): String {
        return gson.toJson(hijriDate)
    }

    private fun jsonToHijri(jsonString: String): HijriDate {
        return gson.fromJson(jsonString, HijriDate::class.java)
    }

    private fun save(jsonString: String): HijriDate {
        SP_HIJRI_DATE_KEY.putInStorage(getTodayDate())
        SP_HIJRI_DATA_KEY.putInStorage(jsonString)
        return jsonToHijri(jsonString)
    }

    private fun transform(jsonObject: JSONObject): HijriDate {
        return jsonObject
            .getInt("code")
            .takeIf { it == 200 }
            ?.let { jsonObject.getJSONObject("data") }
            ?.let { it.getJSONObject("hijri") }
            ?.let {
                val month = it.getJSONObject("month")
                HijriDate(
                    day = it.getString("day"),
                    monthEn = month.getString("en"),
                    monthAr = month.getString("ar"),
                    year = it.getString("year"),
                )
            }
            ?: throw IllegalStateException("status code should be equal to 200")
    }
}
