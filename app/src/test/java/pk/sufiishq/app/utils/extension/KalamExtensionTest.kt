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

import android.content.Context
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import pk.sufiishq.app.SufiIshqTest
import pk.sufiishq.app.feature.kalam.extension.canPlay
import pk.sufiishq.app.feature.kalam.extension.hasOfflineSource
import pk.sufiishq.app.feature.kalam.model.Kalam
import pk.sufiishq.app.utils.extention.isNetworkAvailable
import pk.sufiishq.app.utils.extention.toast

class KalamExtensionTest : SufiIshqTest() {

    @Test
    fun testHasOfflineSource_shouldReturn_true() {
        val kalam = mockk<Kalam> { every { offlineSource } returns "offline-src" }
        assertTrue(kalam.hasOfflineSource())
    }

    @Test
    fun testHasOfflineSource_shouldReturn_false() {
        val kalam = mockk<Kalam> { every { offlineSource } returns "" }
        assertFalse(kalam.hasOfflineSource())
    }

    @Test
    fun testCanPlay_shouldReturn_true() {
        mockkStatic(Context::isNetworkAvailable)
        mockkStatic(Kalam::hasOfflineSource)

        val context = mockk<Context> { every { isNetworkAvailable() } returns true }

        val kalam = mockk<Kalam> { every { hasOfflineSource() } returns true }

        assertTrue(kalam.canPlay(context))
    }

    @Test
    fun testCanPlay_shouldReturn_false() {
        mockkStatic(Context::isNetworkAvailable)
        mockkStatic(Context::toast)
        mockkStatic(Kalam::hasOfflineSource)

        val context =
            mockk<Context> {
                every { isNetworkAvailable() } returns false
                every { toast(any()) } returns mockk()
            }

        val kalam = mockk<Kalam> { every { hasOfflineSource() } returns false }

        assertFalse(kalam.canPlay(context))
    }
}
