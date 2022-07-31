package pk.sufiishq.app

import android.os.Build
import androidx.lifecycle.*
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import io.mockk.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.AfterClass
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import pk.sufiishq.app.models.Kalam

@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1], application = HiltTestApplication::class)
abstract class SufiIshqTest {

    protected fun mockLifecycleOwner(): LifecycleOwner {
        val owner = mockk<LifecycleOwner>()
        val lifecycle = LifecycleRegistry(owner)
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        every { owner.lifecycle } returns lifecycle
        return owner
    }

    protected fun launchViewModelScope(
        viewModel: ViewModel,
        block: (slot: List<suspend CoroutineScope.() -> Unit>) -> Unit
    ) {
        mockkStatic(ViewModel::viewModelScope)
        mockkStatic(CoroutineScope::launch)

        runBlocking {
            val slot = mutableListOf<suspend CoroutineScope.() -> Unit>()
            val coroutineScope = mockk<CoroutineScope>()
            every {
                coroutineScope.launch(
                    context = any(),
                    block = capture(slot)
                )
            } returns mockk()
            every { viewModel.viewModelScope } returns coroutineScope
            block(slot)
        }
    }

    // WARNING - do not change or remove any property in this method
    protected fun sampleKalam() = Kalam(
        1,
        "kalam-title",
        1,
        "1990",
        "Karachi",
        "kalam-online-src",
        "kalam-offline-src",
        1,
        1
    )

    fun List<suspend CoroutineScope.() -> Unit>.invoke() {
        runBlocking {
            forEach {
                it.invoke(mockk())
            }
        }
    }

    companion object {

        @JvmStatic
        @AfterClass
        fun resetMocks() {
            clearAllMocks()
        }
    }
}