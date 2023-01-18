package pk.sufiishq.app.core.help

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import pk.sufiishq.app.SufiIshqTest

class HelpContentResolverTest : SufiIshqTest() {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Test
    fun test_convertHelpJson_inListOfHelpContent() = runBlocking {

        val helpJson = JSONObject(HELP_JSON)
        val helpContentResolver = HelpContentResolver(helpJson)

        helpContentResolver.resolve().collectLatest {
            assertEquals(1, it.size)

            with(it.first()) {
                assertEquals("Accordion Title", title)

                content.onEach { helpData ->
                    when (helpData) {
                        is HelpData.Photo -> assertEquals("help/images/test.jpg", helpData.path)
                        is HelpData.Divider -> assertEquals(1, helpData.height)
                        is HelpData.Spacer -> assertEquals(20, helpData.height)
                        is HelpData.Paragraph -> assertEquals(
                            "paragraph with some bold and italic words",
                            helpData.text.text
                        )
                    }
                }
            }
        }
    }

    companion object {
        private const val HELP_JSON = """
            {
                "data": [
                    {
                        "title": "Accordion Title",
                        "content": [
                            "{'image' : 'help/images/test.jpg'}",
                            "{'divider' : 1}",
                            "{'spacer': 20}",
                            "paragraph with some **bold** and __italic__ words"
                        ]
                    }
                ]
            }        
        """
    }
}