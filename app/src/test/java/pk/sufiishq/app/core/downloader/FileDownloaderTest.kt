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

package pk.sufiishq.app.core.downloader

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import pk.sufiishq.app.SufiIshqTest
import pk.sufiishq.app.feature.kalam.downloader.FileDownloader
import pk.sufiishq.app.feature.kalam.downloader.FileInfo
import java.io.File

class FileDownloaderTest : SufiIshqTest() {

    private lateinit var fileDownloader: FileDownloader
    private lateinit var okHttpClient: OkHttpClient
    private lateinit var requestBuilder: Request.Builder
    private lateinit var context: Context

    @Before
    fun setUp() {
        okHttpClient = mockk()

        every { okHttpClient.newBuilder() } returns
            mockk {
                every { connectTimeout(any(), any()) } returns this
                every { readTimeout(any(), any()) } returns this
                every { build() } returns okHttpClient
            }

        requestBuilder = mockk()
        context = ApplicationProvider.getApplicationContext()
        // fileDownloader = FileDownloader(okHttpClient, requestBuilder)
    }

    @Test
    fun testDownload_shouldDownload_fileFromUrl() {
        verifyFileDownload(200)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testDownload_shouldThrow_IllegalArgumentException() {
        verifyFileDownload(404)
    }

    private fun verifyFileDownload(responseCode: Int) {
        mockkStatic(Observable::class)

        val request = mockk<Request>()
        val response = mockk<Response>()
        val call = mockk<Call>()
        val responseBody = mockk<ResponseBody>()
        val observableOnSubscribeSlot = slot<ObservableOnSubscribe<FileInfo>>()
        val urlSlot = slot<String>()

        every { requestBuilder.url(capture(urlSlot)) } returns requestBuilder
        every { requestBuilder.build() } returns request
        every { okHttpClient.newCall(request) } returns call
        every { call.execute() } returns response
        every { response.body } returns responseBody
        every { response.code } returns responseCode
        every { responseBody.contentLength() } returns 1
        every { responseBody.byteStream() } returns "data".byteInputStream()
        every { Observable.create(capture(observableOnSubscribeSlot)) } returns mockk()

        val file = File(context.cacheDir, "test.txt")
        fileDownloader.download("https://www.sufiishq.pk", file)

        val fileInfoSlot = slot<FileInfo>()
        observableOnSubscribeSlot.captured.subscribe(
            mockk {
                every { onNext(capture(fileInfoSlot)) } returns Unit
                every { onComplete() } returns Unit
            },
        )

        assertEquals("https://www.sufiishq.pk", urlSlot.captured)
        // assertEquals(1, fileInfoSlot.captured.totalSize.toInt())
    }
}
