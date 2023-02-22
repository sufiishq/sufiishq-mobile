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

package pk.sufiishq.app

import android.content.Context
import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import org.junit.AfterClass
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import pk.sufiishq.app.fake.TestSharedPreferences
import pk.sufiishq.app.feature.kalam.helper.TrackListType
import pk.sufiishq.app.feature.kalam.model.Kalam
import pk.sufiishq.app.feature.kalam.model.KalamInfo
import pk.sufiishq.app.feature.player.PlayerState
import java.io.InputStream

typealias StringRes = R.string

@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1], application = HiltTestApplication::class)
abstract class SufiIshqTest {

    @get:Rule
    val rules = InstantTaskExecutorRule()

    private val sufiIshqApp = mockk<SufiIshqApp>()
    internal val appContext = ApplicationProvider.getApplicationContext<Context>()

    @Before
    fun baseSetUp() {
        mockApp()
    }

    private fun mockApp() {
        mockkObject(SufiIshqApp)

        every { sufiIshqApp.keyValueStorage } returns TestSharedPreferences(appContext)
        every { sufiIshqApp.getString(any()) } answers {
            appContext.getString(firstArg())
        }
        every { SufiIshqApp.getInstance() } returns sufiIshqApp
    }

    protected fun testHelpFileInputStream(): InputStream {
        return javaClass.classLoader.getResourceAsStream("help/help.json")
    }

    // WARNING - do not change or remove any property in this method
    protected fun sampleKalam() =
        Kalam(
            1,
            "kalam-title",
            1,
            "1990",
            "Karachi",
            "kalam-online-src",
            "kalam-offline-src",
            1,
            1,
        )

    // WARNING - do not change, remove or modify any property in this method
    protected fun sampleKalamInfo() = KalamInfo(
        PlayerState.IDLE,
        sampleKalam(),
        0,
        100,
        false,
        TrackListType.All(),
    )

    fun List<suspend CoroutineScope.() -> Unit>.invoke() {
        runBlocking { forEach { it.invoke(mockk()) } }
    }

    companion object {

        @JvmStatic
        @AfterClass
        fun resetMocks() {
            clearAllMocks()
        }
    }
}
