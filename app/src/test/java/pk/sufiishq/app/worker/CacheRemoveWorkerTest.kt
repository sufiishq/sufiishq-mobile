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

package pk.sufiishq.app.worker

import androidx.work.ListenableWorker.Result
import androidx.work.testing.TestWorkerBuilder
import java.util.concurrent.Executors
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import pk.sufiishq.app.SufiIshqTest

class CacheRemoveWorkerTest : SufiIshqTest() {

    private lateinit var cacheRemoveWorker: CacheRemoveWorker

    @Before
    fun setUp() {
        cacheRemoveWorker = TestWorkerBuilder<CacheRemoveWorker>(
            appContext,
            Executors.newSingleThreadExecutor()
        ).build()
    }

    @Test
    fun testDoWork_shouldReturn_successResult() {
        assertThat(cacheRemoveWorker.doWork(), `is`(Result.success()))
    }

}