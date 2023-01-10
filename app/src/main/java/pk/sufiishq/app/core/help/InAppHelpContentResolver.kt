package pk.sufiishq.app.core.help

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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

    fun transform(data: String): @Composable () -> Unit = {

        /*
            transform each line here and build composable according to the tag
            for example: simple text = Text() @divider = Divider()
            for simplicity create separate function for each transformation
         */

        // for example
        Column {
            Text(text = data)
            Divider()
        }
    }

    fun JSONObject.getJSONObjectAsList(key: String): List<JSONObject> {
        val list = mutableListOf<JSONObject>()
        val jsonArray = getJSONArray(key)
        (0..jsonArray.length().minus(1)).onEach {
            list.add(jsonArray.getJSONObject(it))
        }
        return list
    }

    fun JSONArray.asStringList(): List<String> {
        val list = mutableListOf<String>()
        (0..length().minus(1)).onEach {
            list.add(getString(it))
        }
        return list
    }
}