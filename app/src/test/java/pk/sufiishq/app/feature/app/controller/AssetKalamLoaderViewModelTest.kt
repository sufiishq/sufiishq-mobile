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

package pk.sufiishq.app.feature.app.controller

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.google.gson.Gson
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import javax.inject.Inject
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import pk.sufiishq.app.SufiIshqTest
import pk.sufiishq.app.feature.kalam.data.repository.KalamRepository
import pk.sufiishq.app.utils.getOrAwaitValue

@HiltAndroidTest
class AssetKalamLoaderViewModelTest : SufiIshqTest() {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject lateinit var kalamRepository: KalamRepository
    @Inject lateinit var gson: Gson
    private lateinit var assetKalamLoaderViewModel: AssetKalamLoaderViewModel

    @Before
    fun setUp() {
        hiltRule.inject()
        assetKalamLoaderViewModel = AssetKalamLoaderViewModel(appContext, gson, kalamRepository)
    }

    @Test
    fun testLoadAllKalam_shouldLoad_allKalamFromAssetAndInsertInDB_andLoadDefaultKalam() =
        runBlocking {
            assertTrue(assetKalamLoaderViewModel.countAll().getOrAwaitValue() == 0)
            assetKalamLoaderViewModel.loadAllKalam()
            assertTrue(assetKalamLoaderViewModel.countAll().getOrAwaitValue() == 467)
        }

}
