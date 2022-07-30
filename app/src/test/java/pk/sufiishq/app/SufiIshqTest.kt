package pk.sufiishq.app

import android.os.Build
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import dagger.hilt.android.testing.HiltTestApplication
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.AfterClass
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

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

    companion object {

        @JvmStatic
        @AfterClass
        fun resetMocks() {
            clearAllMocks()
        }
    }
}