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
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import pk.sufiishq.app.SufiIshqTest
import pk.sufiishq.app.di.qualifier.MainDispatcher
import pk.sufiishq.app.feature.hijridate.api.HijriDateService
import pk.sufiishq.app.feature.hijridate.model.HijriDate
import pk.sufiishq.app.utils.extention.getFromStorage
import pk.sufiishq.app.utils.extention.getTodayDate
import pk.sufiishq.app.utils.extention.putInStorage
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltAndroidTest
class HijriDateResolverTest : SufiIshqTest() {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var gson: Gson

    @MainDispatcher
    @Inject
    lateinit var dispatcher: CoroutineContext
    private val hijriDateService = mockk<HijriDateService>()
    private lateinit var hijriDateResolver: HijriDateResolver

    @Before
    fun setUp() {
        hiltRule.inject()
        hijriDateResolver = HijriDateResolver(dispatcher, hijriDateService, gson)
    }

    @Test
    fun testFetchHijriDate_fromServer(): Unit = runBlocking {
        clearSavedRecord()

        coEvery { hijriDateService.getHijriDate(any()) } returns mockk {
            every { string() } returns HIJRI_DATE_RAW_JSON
        }

        assertHijriDate(HijriDate("01", "Shaban", "Shaban", "1444"), hijriDateResolver.resolve()!!)
    }

    @Test
    fun testLoadOffline_hijriDate_whenTodayRecordFound(): Unit = runBlocking {
        putDefaultHijriDate()
        assertHijriDate(getDefaultHijriDate(), hijriDateResolver.resolve()!!)
    }

    @Test
    fun testLoadOffline_hijriDate_whenExceptionOccur(): Unit = runBlocking {
        putDefaultHijriDate("08-11-1989")

        coEvery { hijriDateService.getHijriDate(any()) } throws RuntimeException("i hope you understand :)")
        assertHijriDate(getDefaultHijriDate(), hijriDateResolver.resolve()!!)
    }

    @Test
    fun testVerify_hijriDate_constants() {
        assertEquals("http://api.aladhan.com/v1/gToH?date=%s", HIJRI_DATE_HOST)
        assertEquals("sp_hijri_date", SP_HIJRI_DATE_KEY)
        assertEquals("sp_hijri_data", SP_HIJRI_DATA_KEY)
    }

    private fun assertHijriDate(expected: HijriDate, actual: HijriDate) {
        assertEquals(expected.day, actual.day)
        assertEquals(expected.monthEn, actual.monthEn)
        assertEquals(expected.monthAr, actual.monthAr)
        assertEquals(expected.year, actual.year)
    }

    private fun clearSavedRecord() {
        SP_HIJRI_DATE_KEY.putInStorage("")
        SP_HIJRI_DATA_KEY.putInStorage("")
    }

    private fun putDefaultHijriDate(date: String = getTodayDate()) {
        val hijriDate = HijriDate("01", "Rajab", "Rajab", "1444")
        SP_HIJRI_DATE_KEY.putInStorage(date)
        SP_HIJRI_DATA_KEY.putInStorage(gson.toJson(hijriDate))
    }

    private fun getDefaultHijriDate(): HijriDate {
        return SP_HIJRI_DATA_KEY.getFromStorage(getTodayDate()).let {
            gson.fromJson(it, HijriDate::class.java)
        }
    }

    companion object {
        const val HIJRI_DATE_RAW_JSON = """
            {
              "code": 200,
              "status": "OK",
              "data": {
                "hijri": {
                  "date": "01-08-1444",
                  "format": "DD-MM-YYYY",
                  "day": "01",
                  "weekday": {
                    "en": "Al Arba'a",
                    "ar": "الاربعاء"
                  },
                  "month": {
                    "number": 8,
                    "en": "Shaban",
                    "ar": "Shaban"
                  },
                  "year": "1444",
                  "designation": {
                    "abbreviated": "AH",
                    "expanded": "Anno Hegirae"
                  },
                  "holidays": []
                },
                "gregorian": {
                  "date": "22-02-2023",
                  "format": "DD-MM-YYYY",
                  "day": "22",
                  "weekday": {
                    "en": "Wednesday"
                  },
                  "month": {
                    "number": 2,
                    "en": "February"
                  },
                  "year": "2023",
                  "designation": {
                    "abbreviated": "AD",
                    "expanded": "Anno Domini"
                  }
                }
              }
            }
        """
    }
}
