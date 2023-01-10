package pk.sufiishq.app.core.help

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.json.JSONArray
import org.json.JSONObject
import pk.sufiishq.app.models.HelpContent

class InAppHelpContentResolver @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun resolve() = flow {

        val data = context
            .assets
            .open("help/help.json")
            .bufferedReader()
            .use { it.readText() }
            .asJSONObject()
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

        Column {
            Text(text = data)
            Divider()
        }
    }

    fun String.asJSONObject(): JSONObject {
        return JSONObject(this)
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