package pk.sufiishq.app.ui.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import org.junit.Rule
import org.junit.Test
import pk.sufiishq.app.SufiIshqTest
import pk.sufiishq.app.utils.assetsToBitmap

class AssetImageTest : SufiIshqTest() {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `test verify bitmap image`() {
        mockkStatic(Context::assetsToBitmap)

        val context = mockk<Context>()
        every { context.assetsToBitmap(any()) } returns sampleBitmap()

        composeTestRule.setContent {
            AssetImage(
                path = "my_path",
                modifier = Modifier.testTag("test_tag"),
                context = context
            )
        }

        composeTestRule.onNode(hasTestTag("test_tag")).assertExists().assertIsDisplayed()

    }

    private fun sampleBitmap(): Bitmap {
        val decodedString: ByteArray = Base64.decode(IMAGE_STRING, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    }

    companion object {
        private const val IMAGE_STRING =
            "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNk+A8AAQUBAScY42YAAAAASUVORK5CYII="
    }
}