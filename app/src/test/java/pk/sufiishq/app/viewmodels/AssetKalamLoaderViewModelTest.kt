package pk.sufiishq.app.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.test.core.app.ApplicationProvider
import io.mockk.*
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.robolectric.util.ReflectionHelpers.setField
import pk.sufiishq.app.SufiIshqTest
import pk.sufiishq.app.data.repository.KalamRepository
import pk.sufiishq.app.models.Kalam

class AssetKalamLoaderViewModelTest : SufiIshqTest() {

    private lateinit var assetKalamLoaderViewModel: AssetKalamLoaderViewModel
    private lateinit var kalamRepository: KalamRepository
    private lateinit var context: Context

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        kalamRepository = mockk()
        assetKalamLoaderViewModel = spyk(AssetKalamLoaderViewModel(context, kalamRepository))
    }

    @Test
    fun testLoadAllKalam_shouldLoad_allKalamFromAssetAndInsertInDB() = runBlocking {

        launchViewModelScope(assetKalamLoaderViewModel) { slot ->

            val consumerSlot = slot<Consumer<List<Kalam>>>()
            every { kalamRepository.loadAllFromAssets(any()) } returns mockk {
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
        }
    }

    @Test
    fun testLoadAllKalam_shouldLoad_nothing() {
        assetKalamLoaderViewModel.loadAllKalam(10) { dataInserted ->
            assertFalse(dataInserted)
        }
    }

    @Test
    fun testRelease_shouldVerify_disposeCall() {
        val disposable = mockk<Disposable>()

        every { disposable.dispose() } returns Unit
        setField(assetKalamLoaderViewModel, "disposable", disposable)

        assetKalamLoaderViewModel.release()
        verify(exactly = 1) { disposable.dispose() }
    }

}