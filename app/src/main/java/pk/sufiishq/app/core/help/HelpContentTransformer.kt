package pk.sufiishq.app.core.help

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import javax.inject.Inject
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import pk.sufiishq.app.di.qualifier.IoDispatcher
import pk.sufiishq.app.models.HelpContent
import pk.sufiishq.app.models.TagInfo
import kotlin.coroutines.CoroutineContext

private const val IMAGE_PATTERN =
    """^\{\s*\'image\'\s*:\s*\'(http(s?)):\/\/([\w\-\/\.])*(?:png|jpeg|jpg)\'\s*\}${'$'}"""
private const val DIVIDER_PATTERN = """^\{\s*\'divider\'\s*:\s*\d+\s*\}${'$'}"""
private const val SPACER_PATTERN = """^\{\s*\'spacer\'\s*:\s*\d+\s*\}${'$'}"""

class HelpContentTransformer @Inject constructor(
    @IoDispatcher private val dispatcher: CoroutineContext
) {

    suspend fun transform(jsonObject: JSONObject): List<HelpContent> {

        return withContext(dispatcher) {
            jsonObject.getJSONObjectAsList("data").map { item ->
                HelpContent(
                    title = item.getString("title"),
                    content = item.getJSONArray("content")
                        .asStringList()
                        .map {
                            transform(it)
                        }
                )
            }
        }
    }

    private fun transform(data: String): HelpData {

        return data.find(IMAGE_PATTERN) {
            HelpData.Photo(it.getString("image"))
        } ?: data.find(DIVIDER_PATTERN) {
            HelpData.Divider(it.getInt("divider"))
        } ?: data.find(SPACER_PATTERN) {
            HelpData.Spacer(it.getInt("spacer"))
        } ?: HelpData.Paragraph(annotateParagraph(data))
    }

    private fun String.find(pattern: String, found: (JSONObject) -> HelpData): HelpData? {
        return if (Regex(pattern).containsMatchIn(this)) found(JSONObject(this)) else null
    }

    private fun annotateParagraph(text: String): AnnotatedString {
        val regex = Regex("__.*?__|\\*\\*.*?\\*\\*")
        var results: MatchResult? = regex.find(text)
        val tagInfo = mutableListOf<TagInfo>()
        var finalText = text

        while (results != null) {
            val indexOf = finalText.indexOf(results.value)
            val tagStyle = results.value.getStyle()

            val newKeyWord = results.value.replace(Regex("__|\\*"), "")
            finalText = finalText.replace(results.value, newKeyWord)

            val item = TagInfo(indexOf, indexOf + newKeyWord.length, tagStyle)
            tagInfo.add(item)
            results = results.next()
        }

        return buildAnnotatedString {
            append(finalText)
            tagInfo.forEach {
                addStyle(
                    style = it.getStyle(), start = it.first, end = it.last
                )
            }
        }
    }

    private fun String.getStyle(): String {
        return when (this.substring(0, 2)) {
            "**" -> "bold"
            "__" -> "italic"
            else -> ""
        }
    }

    private fun JSONObject.getJSONObjectAsList(key: String): List<JSONObject> {
        val list = mutableListOf<JSONObject>()
        val jsonArray = getJSONArray(key)
        (0..jsonArray.length().minus(1)).onEach {
            list.add(jsonArray.getJSONObject(it))
        }
        return list
    }

    private fun JSONArray.asStringList(): List<String> {
        val list = mutableListOf<String>()
        (0..length().minus(1)).onEach {
            list.add(getString(it))
        }
        return list
    }
}