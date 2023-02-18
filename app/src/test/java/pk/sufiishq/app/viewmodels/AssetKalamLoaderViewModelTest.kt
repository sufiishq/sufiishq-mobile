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

package pk.sufiishq.app.viewmodels

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.robolectric.util.ReflectionHelpers.setField
import pk.sufiishq.app.SufiIshqTest
import pk.sufiishq.app.core.app.controller.AssetKalamLoaderViewModel
import pk.sufiishq.app.core.kalam.data.repository.KalamRepository

class AssetKalamLoaderViewModelTest : SufiIshqTest() {

    private lateinit var assetKalamLoaderViewModel: AssetKalamLoaderViewModel
    private lateinit var kalamRepository: KalamRepository
    private lateinit var context: Application

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        kalamRepository = mockk()
        // assetKalamLoaderViewModel = spyk(AssetKalamLoaderViewModel(context, kalamRepository))
    }

    @Test
    fun testLoadAllKalam_shouldLoad_allKalamFromAssetAndInsertInDB() = runBlocking {
        /*launchViewModelScope(assetKalamLoaderViewModel) { slot ->
            val consumerSlot = slot<Consumer<List<Kalam>>>()
            every { kalamRepository.loadAllFromAssets(any()) } returns
                mockk {
                    every { subscribeOn(any()) } returns this
                    every { observeOn(any()) } returns this
                    every { subscribe(capture(consumerSlot)) } returns mockk()
                }

            val kalamSlot = slot<List<Kalam>>()
            coEvery { kalamRepository.insertAll(capture(kalamSlot)) } returns Unit
            every { kalamRepository.countAll() } returns MutableLiveData(0)

            assetKalamLoaderViewModel.loadAllKalam(0) { dataInserted ->
                assertTrue(dataInserted)
                assertEquals(1, kalamSlot.captured.size)
                coVerify(exactly = 1) { kalamRepository.insertAll(kalamSlot.captured) }
            }

            consumerSlot.captured.accept(listOf(sampleKalam()))
            slot.invoke()
        }*/
    }

    /*@Test
    fun testLoadAllKalam_shouldLoad_nothing() {
        assetKalamLoaderViewModel.loadAllKalam(10) { dataInserted -> assertFalse(dataInserted) }
    }*/

    @Test
    fun testRelease_shouldVerify_disposeCall() {
        val disposable = mockk<Disposable>()

        every { disposable.dispose() } returns Unit
        setField(assetKalamLoaderViewModel, "disposable", disposable)

        verify(exactly = 1) { disposable.dispose() }
    }
}
