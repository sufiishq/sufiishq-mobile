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

import android.content.Context
import android.content.res.AssetManager
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.TRANSPORT_BLUETOOTH
import android.net.NetworkCapabilities.TRANSPORT_CELLULAR
import android.net.NetworkCapabilities.TRANSPORT_ETHERNET
import android.net.NetworkCapabilities.TRANSPORT_USB
import android.net.NetworkCapabilities.TRANSPORT_VPN
import android.net.NetworkCapabilities.TRANSPORT_WIFI
import android.widget.Toast
import androidx.test.core.app.ApplicationProvider
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import pk.sufiishq.app.SufiIshqTest

class ContextExtTest : SufiIshqTest() {

    @Test
    fun testIsNetworkAvailable_shouldReturn_true() {
        mockkStatic(NetworkCapabilities::hasAnyOneTransport)

        val context = mockContext(true)
        assertTrue(context.isNetworkAvailable())
    }

    @Test
    fun testIsNetworkAvailable_shouldReturn_false() {
        mockkStatic(NetworkCapabilities::hasAnyOneTransport)

        val context = mockContext(false)
        assertFalse(context.isNetworkAvailable())
    }

    @Test
    fun testIsNetworkAvailable_shouldReturn_falseWhenGetNullCapabilities() {
        val context = mockk<Context>()

        every { context.getSystemService(any()) } returns
            mockk<ConnectivityManager> {
                every { activeNetwork } returns mockk()
                every { getNetworkCapabilities(any()) } returns null
            }

        assertFalse(context.isNetworkAvailable())
    }

    @Test
    fun testHasAnyOneTransport_shouldReturn_true() {
        val capabilities = mockNetworkCapabilities()

        assertTrue(capabilities.hasAnyOneTransport(TRANSPORT_CELLULAR))
        assertTrue(capabilities.hasAnyOneTransport(TRANSPORT_WIFI))
        assertTrue(capabilities.hasAnyOneTransport(TRANSPORT_ETHERNET))
    }

    @Test
    fun testHasAnyOneTransport_shouldReturn_false() {
        val capabilities = mockNetworkCapabilities()

        assertFalse(capabilities.hasAnyOneTransport(TRANSPORT_USB))
        assertFalse(capabilities.hasAnyOneTransport(TRANSPORT_BLUETOOTH))
        assertFalse(capabilities.hasAnyOneTransport(TRANSPORT_VPN))
    }

    @Test
    fun testDpToPx_shouldReturn_convertedValue() {
        val context =
            mockk<Context> {
                every { resources } returns
                    mockk { every { displayMetrics } returns mockk { density = 2f } }
            }

        assertEquals(20f, context.dpToPx(10f))
    }

    @Test
    fun testToast_shouldVerify_makeTextAndShowMethod() {
        mockkStatic(Toast::class)

        var toastLength = Toast.LENGTH_SHORT
        var toastMessage = ""

        val toast = mockk<Toast>()
        every { toast.show() } returns Unit
        every { Toast.makeText(any(), any<String>(), any()) } answers
            {
                toastLength = thirdArg()
                toastMessage = secondArg()
                toast
            }

        val context = mockk<Context>()
        context.toast("toast showing")

        verify(exactly = 1) { toast.show() }

        assertEquals(toastLength, Toast.LENGTH_LONG)
        assertEquals("toast showing", toastMessage)
    }

    @Test
    fun test_assetsToBitmap_shouldReturn_bitmap() {
        mockkStatic(BitmapFactory::class)
        val assetManager = mockk<AssetManager>()

        every { BitmapFactory.decodeStream(any()) } returns mockk()

        every { assetManager.open(any()) } returns mockk()
        val context = mockk<Context> { every { assets } returns assetManager }

        assertNotNull(context.assetsToBitmap("test_file"))
        verify { assetManager.open("test_file") }

        every { context.assets } returns ApplicationProvider.getApplicationContext<Context>().assets
        assertNull(context.assetsToBitmap(""))
    }

    private fun mockNetworkCapabilities(): NetworkCapabilities {
        return mockk {
            every { hasTransport(any()) } answers
                {
                    when (firstArg<Int>()) {
                        TRANSPORT_CELLULAR,
                        TRANSPORT_WIFI,
                        TRANSPORT_ETHERNET,
                        -> true
                        else -> false
                    }
                }
        }
    }

    private fun mockContext(supportAnyOneTransport: Boolean): Context {
        val capabilities = mockk<NetworkCapabilities>()
        every { capabilities.hasAnyOneTransport(*anyIntVararg()) } returns supportAnyOneTransport

        val connectivityManager = mockk<ConnectivityManager>()
        every { connectivityManager.activeNetwork } returns mockk()
        every { connectivityManager.getNetworkCapabilities(any()) } returns capabilities

        val context = mockk<Context>()
        every { context.getSystemService(any()) } returns connectivityManager

        return context
    }
}
