package pk.sufiishq.app.core.help

import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.json.JSONArray
import org.json.JSONObject
import pk.sufiishq.app.di.qualifier.HelpJson
import pk.sufiishq.app.models.HelpContent

class InAppHelpContentResolver @Inject constructor(
    @HelpJson private val helpJson: JSONObject
) {

    fun resolve() = flow {

        val data =
            helpJson
                .getJSONObjectAsList("data")
                .map { item ->
                    HelpContent(
                        title = item.getString("title"),
                        content = item
                            .getJSONArray("content")
                            .asStringList()
                            .map {
                                transform(it)
                            }
                    )
                }

        emit(data)

    }.flowOn(Dispatchers.IO)

    private fun transform(data: String): HelpData {

        // for example transform text to paragraph based on data
        return HelpData.Paragraph(data)

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