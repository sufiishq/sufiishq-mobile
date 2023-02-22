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

package pk.sufiishq.app.feature.kalam.downloader

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import java.io.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.apache.commons.io.IOUtils
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import pk.sufiishq.app.SufiIshqTest
import pk.sufiishq.app.utils.extention.appendPath

class FileDownloaderTest : SufiIshqTest() {

    private lateinit var fileDownloader: FileDownloader
    private lateinit var downloadFileService: DownloadFileService

    @Before
    fun setUp() {

        downloadFileService = mockk()
        fileDownloader = FileDownloader(Dispatchers.Main.immediate, downloadFileService)
    }

    @Test
    fun testDownload_shouldVerify_downloadedFileInfo() = runBlocking {
        val urlSlot = slot<String>()
        val outFile = File(appContext.cacheDir.appendPath("/test.txt").absolutePath)

        coEvery { downloadFileService.downloadFile(capture(urlSlot)) } returns mockk {
            every { byteStream() } returns testHelpFileInputStream()
            every { contentLength() } returns IOUtils.toByteArray(testHelpFileInputStream()).size.toLong()
        }

        val result = fileDownloader.download("test_url", outFile).toList()

        assertEquals("test_url", urlSlot.captured)
        assertEquals(2, result.size)
        assertEquals(
            testHelpFileInputStream().bufferedReader().readText(),
            outFile.inputStream().bufferedReader().readText()
        )
    }

    @Test
    fun testDownload_shouldFailed_Exception() = runBlocking {
        coEvery { downloadFileService.downloadFile(any()) } throws RuntimeException("my will")
        val result = fileDownloader.download("", mockk()).first() as FileInfo.Failed
        assertEquals("my will", result.throwable.message)
    }
}
