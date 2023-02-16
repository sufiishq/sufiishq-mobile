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

package pk.sufiishq.app.di.modules

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import pk.sufiishq.app.SufiIshqTest

class AppModuleTest : SufiIshqTest() {

    private lateinit var appModule: AppModule
    private lateinit var appContext: Context

    @Before
    fun setUp() {
        appModule = AppModule()
        appContext = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun test_providesHandler_shouldReturn_AndroidHandler() {
        assertNotNull(appModule.providesHandler())
    }

    @Test
    fun test_helpContentJson_shouldReturn_helpJson() {
        val mockContext = mockk<Context>()

        every { mockContext.assets } returns
            mockk {
                every { open(any()) } answers {
                    javaClass.classLoader.getResourceAsStream(
                        firstArg(),
                    )
                }
            }

        val helpJson = appModule.helpContentJson(mockContext)
        assertNotNull(helpJson)

        val data = helpJson.getJSONArray("data").getJSONObject(0)
        assertEquals("test title", data.getString("title"))
        assertEquals("help content", data.getJSONArray("content").getString(0))
    }
}
