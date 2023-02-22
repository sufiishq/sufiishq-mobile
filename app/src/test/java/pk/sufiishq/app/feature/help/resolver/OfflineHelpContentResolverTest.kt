package pk.sufiishq.app.feature.help.resolver

import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import pk.sufiishq.app.SufiIshqTest
import pk.sufiishq.app.feature.help.HelpContentTransformer
import pk.sufiishq.app.feature.help.di.qualifier.HelpJson

@HiltAndroidTest
class OfflineHelpContentResolverTest : SufiIshqTest() {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var transformer: HelpContentTransformer
    @HelpJson
    @Inject
    lateinit var helpJson: JSONObject
    private lateinit var offlineHelpContentResolver: HelpContentResolver

    @Before
    fun setUp() {
        hiltRule.inject()

        offlineHelpContentResolver = OfflineHelpContentResolver(
            Dispatchers.Main.immediate,
            helpJson,
            transformer
        )
    }

    @Test
    fun testResolve_shouldReturn_HelpContentList() = runBlocking {
        val result = offlineHelpContentResolver.resolve().first()
        assertEquals(1, result.size)
    }

}