package pk.sufiishq.app.feature.help.resolver

import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import pk.sufiishq.app.SufiIshqTest
import pk.sufiishq.app.feature.help.HelpContentTransformer
import pk.sufiishq.app.feature.help.api.HelpContentService
import pk.sufiishq.app.feature.help.di.qualifier.HelpJson
import retrofit2.Response

@HiltAndroidTest
class OnlineHelpContentResolverTest : SufiIshqTest() {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var transformer: HelpContentTransformer

    @HelpJson
    @Inject
    lateinit var helpJson: JSONObject
    private lateinit var helpContentService: HelpContentService
    private lateinit var onlineHelpContentResolver: HelpContentResolver

    @Before
    fun setUp() {
        hiltRule.inject()
        helpContentService = mockk()
        onlineHelpContentResolver = OnlineHelpContentResolver(
            Dispatchers.Main.immediate,
            helpJson,
            helpContentService,
            transformer
        )
    }

    @Test
    fun testResolve_shouldReturn_HelpContentList() = runBlocking {
        val helpUrlSlot = slot<String>()
        every { helpContentService.getHelp(capture(helpUrlSlot)) } returns mockk {
            every { execute() } returns Response.success(
                helpJson.toString().toResponseBody()
            )
        }

        val result = onlineHelpContentResolver.resolve().first()
        assertEquals(1, result.size)
        assertEquals(HELP_URL, helpUrlSlot.captured)
    }

    @Test
    fun testResolve_shouldReturn_offlineHelpJson_whenUnsuccessful() = runBlocking {
        every { helpContentService.getHelp(any()) } returns mockk {
            every { execute() } returns mockk {
                every { isSuccessful } returns false
            }
        }

        val result = onlineHelpContentResolver.resolve().first()
        assertEquals(1, result.size)
    }

    @Test
    fun testResolve_shouldReturn_offlineHelpJson_whenCaughtException() = runBlocking {
        every { helpContentService.getHelp(any()) } throws RuntimeException("Oops")
        val result = onlineHelpContentResolver.resolve().first()
        assertEquals(1, result.size)
    }

    companion object {
        private const val HELP_URL =
            "https://raw.githubusercontent.com/sufiishq/sufiishq-mobile/master/app/src/main/assets/help/help.json"
    }
}