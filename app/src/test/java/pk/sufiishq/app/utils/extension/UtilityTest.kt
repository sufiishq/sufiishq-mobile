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

package pk.sufiishq.app.utils.extension

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import pk.sufiishq.app.SufiIshqTest
import pk.sufiishq.app.feature.kalam.model.Kalam
import pk.sufiishq.app.utils.getApp
import pk.sufiishq.app.utils.optValue

class UtilityTest : SufiIshqTest() {

    @Test
    fun testApp_shouldReturn_nonNull() {
        assertNotNull(getApp())
    }

    @Test
    fun testOptValue_shouldReturn_defaultValue() {
        val testState = mutableStateOf<String?>(null)
        assertEquals("test", testState.optValue("test"))
    }

    @Test
    fun testOptValue_shouldReturn_actualValue() {
        val testState = mutableStateOf("test")
        assertEquals("test", testState.optValue("test"))
    }

    @Test
    fun testCopyAsNew_shouldReturn_deepCopyAlongWithDefaultValue() {
        val kalam = sampleKalam()
        val copyKalam = kalam.copy()

        assertEquals(kalam.hashCode(), copyKalam.hashCode())

        // verify kalam assertion
        verifyKalamAssertion(kalam, copyKalam, true)
    }

    @Test
    fun testCopyAsNew_shouldReturn_deepCopyAlongWithUserDefinedValue() {
        val kalam = sampleKalam()
        val copyKalam =
            kalam.copy(
                id = 2,
                title = "new-title",
                code = 2,
                recordeDate = "1999",
                location = "Lahore",
                onlineSource = "online-src",
                offlineSource = "offline-src",
                isFavorite = 0,
                playlistId = 2,
            )

        // both object should point different memory reference
        assertNotEquals(kalam.hashCode(), copyKalam.hashCode())

        // verify kalam assertion
        verifyKalamAssertion(kalam, copyKalam, false)
    }

    @Test
    fun testOptValue_shouldReturn_actualValueFromLiveData() {
        val testData = MutableLiveData(10)
        assertEquals(10, testData.optValue(5))
    }

    @Test
    fun testOptValue_shouldReturn_defaultValueFromLiveData() {
        val testData = MutableLiveData<Int>(null)
        assertEquals(5, testData.optValue(5))
    }

    private fun verifyKalamAssertion(
        expectedKalam: Kalam,
        actualKalam: Kalam,
        checkEquality: Boolean,
    ) {
        if (checkEquality) {
            assertEquals(expectedKalam.id, actualKalam.id)
            assertEquals(expectedKalam.title, actualKalam.title)
            assertEquals(expectedKalam.code, actualKalam.code)
            assertEquals(expectedKalam.recordeDate, actualKalam.recordeDate)
            assertEquals(expectedKalam.location, actualKalam.location)
            assertEquals(expectedKalam.onlineSource, actualKalam.onlineSource)
            assertEquals(expectedKalam.offlineSource, actualKalam.offlineSource)
            assertEquals(expectedKalam.isFavorite, actualKalam.isFavorite)
            assertEquals(expectedKalam.playlistId, actualKalam.playlistId)
        } else {
            assertNotEquals(expectedKalam.id, actualKalam.id)
            assertNotEquals(expectedKalam.title, actualKalam.title)
            assertNotEquals(expectedKalam.code, actualKalam.code)
            assertNotEquals(expectedKalam.recordeDate, actualKalam.recordeDate)
            assertNotEquals(expectedKalam.location, actualKalam.location)
            assertNotEquals(expectedKalam.onlineSource, actualKalam.onlineSource)
            assertNotEquals(expectedKalam.offlineSource, actualKalam.offlineSource)
            assertNotEquals(expectedKalam.isFavorite, actualKalam.isFavorite)
            assertNotEquals(expectedKalam.playlistId, actualKalam.playlistId)
        }
    }
}
