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

package pk.sufiishq.app.core.storage

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import androidx.test.core.app.ApplicationProvider
import io.mockk.every
import io.mockk.mockkStatic
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import pk.sufiishq.app.SufiIshqTest

class SecureSharedPreferencesStorageTest : SufiIshqTest() {

    private lateinit var storage: SecureSharedPreferencesStorage

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        mockkStatic(EncryptedSharedPreferences::class)
        mockkStatic(MasterKeys::getOrCreate)

        every { MasterKeys.getOrCreate(any()) } returns ""

        every {
            EncryptedSharedPreferences.create(
                any(),
                any(),
                any(),
                any(),
                any(),
            )
        } returns context.getSharedPreferences("sufiishq", Context.MODE_PRIVATE)

        storage = SecureSharedPreferencesStorage(context)
    }

    @Test
    fun test_verifyAll_getAndPutMethods() {
        verify("str_key", "", "")
        verify("int_key", 0, 0)
        verify("float_key", 0f, 0f)
        verify("long_key", 0L, 0L)
        verify("boolean_key", false, false)

        storage.put("str_key", "test")
        storage.put("int_key", 10)
        storage.put("float_key", 1f)
        storage.put("long_key", 100L)
        storage.put("boolean_key", true)

        verify("str_key", "test", "")
        verify("int_key", 10, 0)
        verify("float_key", 1f, 0f)
        verify("long_key", 100L, 0L)
        verify("boolean_key", true, false)
    }

    @Test
    fun testContains_shouldReturn_false() {
        assertFalse(storage.contains("score"))
    }

    @Test
    fun testContains_shouldReturn_true() {
        storage.put("score", 10)
        assertTrue(storage.contains("score"))
    }

    private fun verify(key: String, expected: Any, default: Any) {
        assertEquals(expected, storage.get(key, default))
    }
}
