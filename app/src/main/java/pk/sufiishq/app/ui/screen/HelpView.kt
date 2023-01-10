package pk.sufiishq.app.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import pk.sufiishq.app.data.providers.HelpDataProvider
import pk.sufiishq.app.utils.optValue

@Composable
fun HelpView(
    helpDataProvider: HelpDataProvider
) {

    val helpContent = helpDataProvider.getHelpContent().observeAsState().optValue(listOf())
    val matColors = MaterialTheme.colors

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(matColors.secondaryVariant)
    ) {

        itemsIndexed(helpContent) { index, item ->
            Text(
                text = item.title,
                style = MaterialTheme.typography.h4
            )

            Column {
                item.content.onEach {
                    it.invoke()
                }
            }
        }
    }

}