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

package pk.sufiishq.app.utils

import org.junit.Assert.assertEquals
import org.junit.Test
import pk.sufiishq.app.SufiIshqTest

class TimeExtTest : SufiIshqTest() {

    @Test
    fun testFormatTime_shouldReturn_defaultFormattedTime() {
        assertEquals("00:00:00", 0.formatTime)
    }

    @Test
    fun testFormatTime_shouldReturn_validFormattedTime() {
        val dayInMills = 86400000
        assertEquals("24:00:00", dayInMills.formatTime)
    }
}
