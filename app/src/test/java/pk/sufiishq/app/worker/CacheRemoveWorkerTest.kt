package pk.sufiishq.app.worker

import android.content.Context
import androidx.work.ListenableWorker
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import pk.sufiishq.app.SufiIshqTest
import pk.sufiishq.app.utils.deleteContent
import java.io.File

class CacheRemoveWorkerTest : SufiIshqTest() {

    private lateinit var cacheRemoveWorker: CacheRemoveWorker
    private lateinit var context: Context

    @Before
    fun setUp() {
        context = mockk()
        cacheRemoveWorker = CacheRemoveWorker(context, mockk())
    }

    @Test
    fun testDoWork_shouldReturn_successResult() {

        mockkStatic(File::deleteContent)

        val file = mockk<File> {
            every { deleteContent() } returns Unit
        }

        every { context.cacheDir } returns file

        val result = cacheRemoveWorker.doWork()

        assertTrue(result is ListenableWorker.Result.Success)
        verify(exactly = 1) { file.deleteContent() }
    }
}