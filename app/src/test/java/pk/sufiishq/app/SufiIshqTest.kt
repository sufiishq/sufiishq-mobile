package pk.sufiishq.app

import android.os.Build
import androidx.lifecycle.*
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import io.mockk.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.AfterClass
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import pk.sufiishq.app.core.event.dispatcher.EventDispatcher
import pk.sufiishq.app.models.Kalam

@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1], application = HiltTestApplication::class)
abstract class SufiIshqTest {

    protected fun mockApp(): SufiIshqApp {
        mockkObject(SufiIshqApp)

        val sufiIshqApp = mockk<SufiIshqApp>()
        every { SufiIshqApp.getInstance() } returns sufiIshqApp
        return sufiIshqApp
    }

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

    fun getKalam() = Kalam(
        id = 1,
        title = "My Kalam",
        code = 1,
        recordeDate = "20-01-1989",
        location = "Karachi",
        onlineSource = "https://sufiishq.pk/media/kalam/karachi/karachi_01.mp3",
        offlineSource = "",
        isFavorite = 0,
        playlistId = 0
    )

    companion object {

        @JvmStatic
        @AfterClass
        fun resetMocks() {
            clearAllMocks()
        }
    }
}