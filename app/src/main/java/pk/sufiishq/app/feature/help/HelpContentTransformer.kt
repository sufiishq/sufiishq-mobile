/*
 * Copyright 2022-2023 SufiIshq
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pk.sufiishq.app.feature.help

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import pk.sufiishq.app.di.qualifier.IoDispatcher
import pk.sufiishq.app.feature.help.model.HelpContent
import pk.sufiishq.app.feature.help.model.TagInfo
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

private const val IMAGE_PATTERN =
    """^\{\s*\'image\'\s*:\s*\'(http(s?)):\/\/([\w\-\/\.])*(?:png|jpeg|jpg)\'\s*\}${'$'}"""
private const val DIVIDER_PATTERN = """^\{\s*\'divider\'\s*:\s*\d+\s*\}${'$'}"""
private const val SPACER_PATTERN = """^\{\s*\'spacer\'\s*:\s*\d+\s*\}${'$'}"""

class HelpContentTransformer
@Inject
constructor(
    @IoDispatcher private val dispatcher: CoroutineContext,
) {

    suspend fun transform(jsonObject: JSONObject): List<HelpContent> {
        return withContext(dispatcher) {
            jsonObject.getJSONObjectAsList("data").map { item ->
                HelpContent(
                    title = item.getString("title"),
                    content = item.getJSONArray("content").asStringList().map { transform(it) },
                )
            }
        }
    }

    private fun transform(data: String): HelpDataType {
        return data.find(IMAGE_PATTERN) { HelpDataType.Photo(it.getString("image")) }
            ?: data.find(DIVIDER_PATTERN) { HelpDataType.Divider(it.getInt("divider")) }
            ?: data.find(SPACER_PATTERN) { HelpDataType.Spacer(it.getInt("spacer")) }
            ?: HelpDataType.Paragraph(annotateParagraph(data))
    }

    private fun String.find(pattern: String, found: (JSONObject) -> HelpDataType): HelpDataType? {
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
                    style = it.getStyle(),
                    start = it.first,
                    end = it.last,
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
        (0..jsonArray.length().minus(1)).onEach { list.add(jsonArray.getJSONObject(it)) }
        return list
    }

    private fun JSONArray.asStringList(): List<String> {
        val list = mutableListOf<String>()
        (0..length().minus(1)).onEach { list.add(getString(it)) }
        return list
    }
}
